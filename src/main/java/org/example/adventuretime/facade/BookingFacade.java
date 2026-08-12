package org.example.adventuretime.facade;

import org.example.adventuretime.bean.BookingBean;
import org.example.adventuretime.bean.BookingQuoteBean;
import org.example.adventuretime.bean.BookingRequestBean;
import org.example.adventuretime.bean.UserBean;
import org.example.adventuretime.dao.BookingDAO;
import org.example.adventuretime.dao.HotelDAO;
import org.example.adventuretime.dao.UserDAO;
import org.example.adventuretime.exception.AuthorizationException;
import org.example.adventuretime.exception.HotelUnavailableException;
import org.example.adventuretime.exception.PersistenceException;
import org.example.adventuretime.exception.ValidationException;
import org.example.adventuretime.mapper.BookingMapper;
import org.example.adventuretime.mapper.HotelMapper;
import org.example.adventuretime.model.Booking;
import org.example.adventuretime.model.BookingStatus;
import org.example.adventuretime.model.ExtraService;
import org.example.adventuretime.model.HotelRoom;
import org.example.adventuretime.model.PaymentData;
import org.example.adventuretime.model.User;
import org.example.adventuretime.pattern.booking.BaseBookingPrice;
import org.example.adventuretime.pattern.booking.BookingPriceComponent;
import org.example.adventuretime.pattern.booking.BookingPriceDecoratorFactory;
import org.example.adventuretime.session.UserSession;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Facade del sottosistema di prenotazione.
 */
public final class BookingFacade {

    private static final BigDecimal POINT_VALUE = new BigDecimal("0.01");
    private static final BigDecimal MAXIMUM_DISCOUNT_RATE =
            new BigDecimal("0.20");
    private static final BigDecimal EARN_RATE = new BigDecimal("0.05");

    private final HotelDAO hotelDAO;
    private final BookingDAO bookingDAO;
    private final UserDAO userDAO;
    private final UserSession userSession;

    public BookingFacade(
            HotelDAO hotelDAO,
            BookingDAO bookingDAO,
            UserDAO userDAO,
            UserSession userSession
    ) {
        this.hotelDAO = hotelDAO;
        this.bookingDAO = bookingDAO;
        this.userDAO = userDAO;
        this.userSession = userSession;
    }

    public BookingQuoteBean quote(BookingRequestBean request)
            throws ValidationException, PersistenceException,
            AuthorizationException {

        if (request == null) {
            throw new ValidationException(
                    "I dati della prenotazione non sono stati forniti."
            );
        }

        // La validazione sintattica resta responsabilità del bean di input.
        request.validateSyntax();

        UserBean user = requireTraveler();

        HotelRoom hotel = hotelDAO.findById(request.getHotelId())
                .orElseThrow(() -> new ValidationException(
                        "La struttura selezionata non esiste."
                ));

        if (request.getPeople() > hotel.getCapacity()) {
            throw new ValidationException(
                    "La camera non può ospitare il numero di persone indicato."
            );
        }

        long nights = ChronoUnit.DAYS.between(
                request.getCheckIn(),
                request.getCheckOut()
        );

        if (nights <= 0) {
            throw new ValidationException(
                    "La prenotazione deve comprendere almeno una notte."
            );
        }

        BigDecimal basePrice = hotel.getPricePerNight()
                .multiply(BigDecimal.valueOf(nights))
                .setScale(2, RoundingMode.HALF_UP);

        BookingPriceComponent component =
                BookingPriceDecoratorFactory.decorate(
                        new BaseBookingPrice(basePrice),
                        request.getExtras()
                );

        BigDecimal grossPrice = component.getPrice()
                .setScale(2, RoundingMode.HALF_UP);

        int pointsUsed = request.isUsePoints()
                ? calculateUsablePoints(user.getPoints(), grossPrice)
                : 0;

        BigDecimal pointsDiscount = POINT_VALUE
                .multiply(BigDecimal.valueOf(pointsUsed))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = grossPrice.subtract(pointsDiscount)
                .max(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal extrasPrice = component.getExtras().stream()
                .map(ExtraService::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BookingQuoteBean quote = new BookingQuoteBean();
        quote.setHotel(HotelMapper.toBean(hotel));
        quote.setNights(nights);
        quote.setBasePrice(basePrice);
        quote.setExtras(component.getExtras());
        quote.setExtrasPrice(extrasPrice);
        quote.setPointsUsed(pointsUsed);
        quote.setPointsDiscount(pointsDiscount);
        quote.setTotalPrice(total);
        return quote;
    }

    public BookingBean createBookingRequest(BookingRequestBean request)
            throws ValidationException, PersistenceException,
            AuthorizationException, HotelUnavailableException {

        BookingQuoteBean quote = quote(request);
        UserBean sessionUser = requireTraveler();

        if (!bookingDAO.isHotelAvailable(
                request.getHotelId(),
                request.getCheckIn(),
                request.getCheckOut()
        )) {
            throw new HotelUnavailableException(
                    "La camera è stata prenotata da un altro utente. "
                            + "Ripetere la ricerca."
            );
        }

        Booking booking = new Booking();
        booking.setUserId(sessionUser.getId());
        booking.setHotelId(request.getHotelId());
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        booking.setPeople(request.getPeople());
        booking.setTotalPrice(quote.getTotalPrice());
        booking.setExtras(request.getExtras());
        booking.setPointsUsed(quote.getPointsUsed());
        booking.setPaymentData(createPaymentData(request));
        booking.setPaymentCompleted(false);
        booking.setStatus(BookingStatus.PENDING_APPROVAL);

        Booking created = bookingDAO.save(booking);
        return BookingMapper.toBean(created, quote.getHotel());
    }

    public void approveBooking(Booking booking)
            throws ValidationException, PersistenceException {
        if (booking == null
                || booking.getStatus() != BookingStatus.PENDING_APPROVAL) {
            throw new ValidationException(
                    "Si possono approvare solo le richieste in attesa.");
        }

        User traveler = userDAO.findById(booking.getUserId())
                .orElseThrow(() -> new PersistenceException(
                        "Il viaggiatore della prenotazione non esiste."));
        if (traveler.getPoints() < booking.getPointsUsed()) {
            throw new ValidationException(
                    "Il viaggiatore non ha piu punti sufficienti.");
        }

        bookingDAO.approveBooking(booking.getId());

        int earnedPoints = booking.getTotalPrice()
                .multiply(EARN_RATE)
                .setScale(0, RoundingMode.DOWN)
                .intValue();
        int updatedPoints = traveler.getPoints()
                - booking.getPointsUsed()
                + earnedPoints;
        userDAO.updatePoints(traveler.getId(), updatedPoints);
    }

    private static PaymentData createPaymentData(BookingRequestBean request)
            throws ValidationException {
        var details = request.getPaymentDetails();
        if (details == null) {
            return new PaymentData(
                    UUID.randomUUID().toString(),
                    "Pagamento di prova",
                    "0000");
        }
        details.validate();
        String number = details.normalizedCardNumber();
        return new PaymentData(
                UUID.randomUUID().toString(),
                details.getCardHolder().trim(),
                number.substring(number.length() - 4));
    }

    private UserBean requireTraveler() throws AuthorizationException {
        if (!userSession.isTraveler()) {
            throw new AuthorizationException(
                    "Solo un viaggiatore può effettuare una prenotazione."
            );
        }
        return userSession.requireUser();
    }

    private static int calculateUsablePoints(
            int availablePoints,
            BigDecimal grossPrice
    ) {
        int safeAvailablePoints = Math.max(availablePoints, 0);

        BigDecimal maximumDiscount = grossPrice
                .multiply(MAXIMUM_DISCOUNT_RATE);

        int maximumPoints = maximumDiscount
                .divide(POINT_VALUE, 0, RoundingMode.DOWN)
                .intValue();

        return Math.min(safeAvailablePoints, maximumPoints);
    }
}
