package org.example.adventuretime.application_controller;

import org.example.adventuretime.bean.BookingBean;
import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.bean.UserBean;
import org.example.adventuretime.dao.BookingDAO;
import org.example.adventuretime.dao.HotelDAO;
import org.example.adventuretime.exception.AuthorizationException;
import org.example.adventuretime.exception.PersistenceException;
import org.example.adventuretime.exception.ValidationException;
import org.example.adventuretime.mapper.BookingMapper;
import org.example.adventuretime.mapper.HotelMapper;
import org.example.adventuretime.model.Booking;
import org.example.adventuretime.model.BookingStatus;
import org.example.adventuretime.model.HotelRoom;
import org.example.adventuretime.session.UserSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller applicativo dell'albergatore.
 * Coordina il catalogo delle strutture e il caso d'uso
 * "Gestire le prenotazioni ricevute".
 */
public final class ManageHotelsApplicationController {

    private final HotelDAO hotelDAO;
    private final BookingDAO bookingDAO;
    private final UserSession userSession;

    public ManageHotelsApplicationController(
            HotelDAO hotelDAO,
            BookingDAO bookingDAO,
            UserSession userSession
    ) {
        this.hotelDAO = hotelDAO;
        this.bookingDAO = bookingDAO;
        this.userSession = userSession;
    }

    public List<HotelBean> getMyHotels()
            throws AuthorizationException, PersistenceException {

        UserBean hotelier = requireHotelier();
        return hotelDAO.findByManagerId(hotelier.getId()).stream()
                .map(HotelMapper::toBean)
                .toList();
    }

    public HotelBean saveHotel(HotelBean hotelBean)
            throws AuthorizationException, ValidationException,
            PersistenceException {

        UserBean hotelier = requireHotelier();

        if (hotelBean == null) {
            throw new ValidationException(
                    "I dati della struttura non sono stati forniti."
            );
        }

        hotelBean.validateSyntax();
        validateHotelOwnershipWhenUpdating(hotelBean, hotelier.getId());
        hotelBean.setManagerId(hotelier.getId());

        HotelRoom savedHotel = hotelDAO.save(
                HotelMapper.toEntity(hotelBean)
        );

        return HotelMapper.toBean(savedHotel);
    }

    public void deleteHotel(long hotelId)
            throws AuthorizationException, ValidationException,
            PersistenceException {

        UserBean hotelier = requireHotelier();

        if (hotelId <= 0) {
            throw new ValidationException(
                    "Identificativo hotel non valido."
            );
        }

        HotelRoom hotel = hotelDAO.findById(hotelId)
                .orElseThrow(() -> new ValidationException(
                        "La struttura selezionata non esiste."
                ));

        if (hotel.getManagerId() != hotelier.getId()) {
            throw new AuthorizationException(
                    "Non puoi eliminare la struttura di un altro albergatore."
            );
        }

        hotelDAO.delete(hotelId, hotelier.getId());
    }

    public List<BookingBean> getReceivedBookings()
            throws AuthorizationException, PersistenceException {

        UserBean hotelier = requireHotelier();
        List<BookingBean> result = new ArrayList<>();

        for (Booking booking : bookingDAO.findByManagerId(hotelier.getId())) {
            HotelBean hotel = hotelDAO.findById(booking.getHotelId())
                    .map(HotelMapper::toBean)
                    .orElseThrow(() -> new PersistenceException(
                            "Prenotazione collegata a una struttura inesistente."
                    ));

            result.add(BookingMapper.toBean(booking, hotel));
        }

        return result;
    }

    public void cancelReceivedBooking(long bookingId)
            throws AuthorizationException, ValidationException,
            PersistenceException {

        UserBean hotelier = requireHotelier();
        if (bookingId <= 0) {
            throw new ValidationException(
                    "Identificativo prenotazione non valido.");
        }

        Booking booking = bookingDAO.findByManagerId(hotelier.getId()).stream()
                .filter(item -> item.getId() == bookingId)
                .findFirst()
                .orElseThrow(() -> new AuthorizationException(
                        "La prenotazione non appartiene alle tue strutture."));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ValidationException(
                    "La prenotazione è già stata annullata.");
        }

        bookingDAO.updateStatus(bookingId, BookingStatus.CANCELLED);
    }

    private void validateHotelOwnershipWhenUpdating(
            HotelBean hotelBean,
            long hotelierId
    ) throws PersistenceException, AuthorizationException,
            ValidationException {

        if (hotelBean.getId() == 0) {
            return;
        }

        HotelRoom existingHotel = hotelDAO.findById(hotelBean.getId())
                .orElseThrow(() -> new ValidationException(
                        "La struttura da modificare non esiste."
                ));

        if (existingHotel.getManagerId() != hotelierId) {
            throw new AuthorizationException(
                    "Non puoi modificare la struttura di un altro albergatore."
            );
        }
    }

    private UserBean requireHotelier() throws AuthorizationException {
        if (!userSession.isVendor()) {
            throw new AuthorizationException(
                    "Solo un albergatore può gestire le proprie strutture."
            );
        }
        return userSession.requireUser();
    }

}
