package org.example.adventuretime.application_controller;

import org.example.adventuretime.bean.BookingBean;
import org.example.adventuretime.bean.BookingQuoteBean;
import org.example.adventuretime.bean.BookingRequestBean;
import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.bean.SearchCriteriaBean;
import org.example.adventuretime.bean.UserBean;
import org.example.adventuretime.dao.BookingDAO;
import org.example.adventuretime.dao.HotelDAO;
import org.example.adventuretime.exception.AuthorizationException;
import org.example.adventuretime.exception.HotelUnavailableException;
import org.example.adventuretime.exception.PersistenceException;
import org.example.adventuretime.exception.ValidationException;
import org.example.adventuretime.facade.BookingFacade;
import org.example.adventuretime.mapper.HotelMapper;
import org.example.adventuretime.model.Booking;
import org.example.adventuretime.model.HotelSearchCriteria;
import org.example.adventuretime.session.FlowContext;
import org.example.adventuretime.session.UserSession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller applicativo del caso d'uso "Gestire prenotazioni".
 * Ricerca, selezione, preventivo, conferma e storico sono nello stesso flusso.
 */
public final class ManageBookingsApplicationController {

    private final HotelDAO hotelDAO;
    private final BookingDAO bookingDAO;
    private final UserSession userSession;
    private final FlowContext flowContext;
    private final BookingFacade bookingFacade;

    public ManageBookingsApplicationController(
            HotelDAO hotelDAO,
            BookingDAO bookingDAO,
            UserSession userSession,
            FlowContext flowContext,
            BookingFacade bookingFacade
    ) {
        this.hotelDAO = hotelDAO;
        this.bookingDAO = bookingDAO;
        this.userSession = userSession;
        this.flowContext = flowContext;
        this.bookingFacade = bookingFacade;
    }

    public List<HotelBean> search(SearchCriteriaBean criteriaBean)
            throws ValidationException, PersistenceException,
            AuthorizationException {

        requireTraveler();

        if (criteriaBean == null) {
            throw new ValidationException(
                    "I criteri di ricerca non sono stati forniti."
            );
        }

        criteriaBean.validateSyntax();
        validateSearchSemantics(criteriaBean);

        /*
         * HotelDAO lavora con il tipo di dominio HotelSearchCriteria,
         * non direttamente con SearchCriteriaBean.
         */
        HotelSearchCriteria domainCriteria = new HotelSearchCriteria(
                criteriaBean.getCity().trim(),
                criteriaBean.getCheckIn(),
                criteriaBean.getCheckOut(),
                criteriaBean.getPeople(),
                criteriaBean.getMaximumPricePerNight()
        );

        List<HotelBean> availableHotels = new ArrayList<>();

        for (var room : hotelDAO.findByCriteria(domainCriteria)) {
            boolean available = bookingDAO.isHotelAvailable(
                    room.getId(),
                    domainCriteria.checkIn(),
                    domainCriteria.checkOut()
            );

            if (available) {
                availableHotels.add(HotelMapper.toBean(room));
            }
        }

        flowContext.setSearch(criteriaBean, availableHotels);

        return availableHotels.stream()
                .map(HotelBean::new)
                .toList();
    }

    public void selectHotel(long hotelId)
            throws ValidationException, AuthorizationException {

        requireTraveler();

        if (hotelId <= 0) {
            throw new ValidationException(
                    "Identificativo hotel non valido."
            );
        }

        HotelBean selected = flowContext.getSearchResults().stream()
                .filter(hotel -> hotel.getId() == hotelId)
                .findFirst()
                .orElseThrow(() -> new ValidationException(
                        "Selezionare una struttura presente nei risultati."
                ));

        flowContext.selectHotel(selected);
    }

    public List<HotelBean> getBufferedResults() {
        return flowContext.getSearchResults();
    }

    /**
     * Restituisce l'hotel scelto nella lista dei risultati.
     */
    public HotelBean getSelectedHotel()
            throws ValidationException, AuthorizationException {

        requireTraveler();

        return flowContext.getSelectedHotel()
                .orElseThrow(() -> new ValidationException(
                        "Nessun hotel è stato selezionato."
                ));
    }

    /**
     * Restituisce i nomi delle immagini di un hotel presente
     * nei risultati della ricerca corrente.
     */
    public List<String> getHotelImageNames(long hotelId)
            throws ValidationException, PersistenceException,
            AuthorizationException {

        requireTraveler();

        if (hotelId <= 0) {
            throw new ValidationException(
                    "Identificativo hotel non valido."
            );
        }

        boolean hotelIsInCurrentResults = flowContext.getSearchResults()
                .stream()
                .anyMatch(hotel -> hotel.getId() == hotelId);

        if (!hotelIsInCurrentResults) {
            throw new ValidationException(
                    "L'hotel non appartiene ai risultati della ricerca corrente."
            );
        }

        return hotelDAO.findImageNamesByHotelId(hotelId);
    }

    /**
     * Restituisce soltanto i nomi dei file immagine dell'hotel selezionato.
     */
    public List<String> getSelectedHotelImageNames()
            throws ValidationException, PersistenceException,
            AuthorizationException {

        HotelBean selectedHotel = getSelectedHotel();
        return getHotelImageNames(selectedHotel.getId());
    }

    public BookingQuoteBean getQuote(BookingRequestBean request)
            throws ValidationException, PersistenceException,
            AuthorizationException {

        requireTraveler();
        validateRequestPresenceAndSyntax(request);
        validateBookingSemantics(request);
        return bookingFacade.quote(request);
    }

    public BookingBean confirm(BookingRequestBean request)
            throws ValidationException, PersistenceException,
            AuthorizationException, HotelUnavailableException {

        requireTraveler();
        validateRequestPresenceAndSyntax(request);
        validateBookingSemantics(request);

        BookingBean confirmedBooking = bookingFacade.createBooking(request);
        flowContext.clearBookingFlow();
        return confirmedBooking;
    }

    public List<BookingBean> getMyBookings()
            throws AuthorizationException, PersistenceException {

        UserBean traveler = requireTraveler();
        List<BookingBean> result = new ArrayList<>();

        for (Booking booking : bookingDAO.findByUserId(traveler.getId())) {
            HotelBean hotel = hotelDAO.findById(booking.getHotelId())
                    .map(HotelMapper::toBean)
                    .orElseGet(() -> removedHotel(booking));

            result.add(toBean(booking, hotel));
        }

        return result;
    }

    private static void validateRequestPresenceAndSyntax(
            BookingRequestBean request
    ) throws ValidationException {
        if (request == null) {
            throw new ValidationException(
                    "I dati della prenotazione non sono stati forniti."
            );
        }
        request.validateSyntax();
    }

    private static void validateSearchSemantics(
            SearchCriteriaBean criteria
    ) throws ValidationException {

        if (!criteria.getCheckOut().isAfter(criteria.getCheckIn())) {
            throw new ValidationException(
                    "La partenza deve essere successiva all'arrivo."
            );
        }

        if (criteria.getCheckIn().isBefore(LocalDate.now())) {
            throw new ValidationException(
                    "La data di arrivo non può essere nel passato."
            );
        }
    }

    private void validateBookingSemantics(BookingRequestBean request)
            throws ValidationException {

        if (!request.getCheckOut().isAfter(request.getCheckIn())) {
            throw new ValidationException(
                    "La partenza deve essere successiva all'arrivo."
            );
        }

        if (request.getCheckIn().isBefore(LocalDate.now())) {
            throw new ValidationException(
                    "La data di arrivo non può essere nel passato."
            );
        }

        HotelBean selectedHotel = flowContext.getSelectedHotel()
                .orElseThrow(() -> new ValidationException(
                        "Nessun hotel è stato selezionato."
                ));

        if (selectedHotel.getId() != request.getHotelId()) {
            throw new ValidationException(
                    "La richiesta non corrisponde all'hotel selezionato."
            );
        }

        SearchCriteriaBean criteria = flowContext.getLastCriteria()
                .orElseThrow(() -> new ValidationException(
                        "I criteri della ricerca non sono più disponibili."
                ));

        boolean sameSearch = Objects.equals(
                criteria.getCheckIn(), request.getCheckIn()
        ) && Objects.equals(
                criteria.getCheckOut(), request.getCheckOut()
        ) && criteria.getPeople() == request.getPeople();

        if (!sameSearch) {
            throw new ValidationException(
                    "I dati della prenotazione non corrispondono alla ricerca."
            );
        }
    }

    private UserBean requireTraveler() throws AuthorizationException {
        if (!userSession.isTraveler()) {
            throw new AuthorizationException(
                    "Solo un viaggiatore può gestire le proprie prenotazioni."
            );
        }
        return userSession.requireUser();
    }

    private static BookingBean toBean(
            Booking booking,
            HotelBean hotel
    ) {
        return new BookingBean(
                booking.getId(),
                booking.getUserId(),
                hotel,
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getPeople(),
                booking.getTotalPrice(),
                booking.getExtras(),
                booking.getPointsUsed(),
                booking.getStatus()
        );
    }

    private static HotelBean removedHotel(Booking booking) {
        return new HotelBean(
                booking.getHotelId(),
                0,
                "Struttura rimossa",
                "",
                "",
                "",
                "",
                BigDecimal.ZERO,
                "",
                0
        );
    }

}