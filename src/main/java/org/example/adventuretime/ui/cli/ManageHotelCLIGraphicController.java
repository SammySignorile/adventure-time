package org.example.adventuretime.ui.cli;

import org.example.adventuretime.AppContext;
import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.exception.AdventureTimeException;

public final class ManageHotelCLIGraphicController {

    private final CliIO io;

    public ManageHotelCLIGraphicController(CliIO io) {
        this.io = io;
    }

    public void listHotels() {
        try {
            var hotels = AppContext.getInstance()
                    .manageHotelsController()
                    .getMyHotels();
            io.title("LE MIE STRUTTURE");
            if (hotels.isEmpty()) {
                io.info("Nessuna struttura inserita.");
            }
            hotels.forEach(hotel -> io.info(
                    "#" + hotel.getId() + " " + hotel));
        } catch (AdventureTimeException e) {
            io.error(e.getMessage());
        }
    }

    public void addHotel() {
        HotelBean bean = new HotelBean();
        bean.setName(io.readText("Nome struttura: "));
        bean.setCity(io.readText("Città: "));
        bean.setRoomType(io.readText("Tipo camera: "));
        bean.setServices(io.readText("Servizi: "));
        bean.setDistanceFromCenter(io.readText("Distanza dal centro: "));
        bean.setPricePerNight(io.readDecimal("Prezzo per notte: "));
        bean.setCapacity(io.readInt("Capienza: "));
        bean.setImageFileName(io.readText(
                "Nome file foto (es. roma 1.jpg): "));

        try {
            HotelBean saved = AppContext.getInstance()
                    .manageHotelsController()
                    .saveHotel(bean);
            io.info("Struttura salvata con id " + saved.getId());
        } catch (AdventureTimeException e) {
            io.error(e.getMessage());
        }
    }

    public void deleteHotel() {
        long id = io.readLong("Id struttura da eliminare: ");
        try {
            AppContext.getInstance()
                    .manageHotelsController()
                    .deleteHotel(id);
            io.info("Struttura eliminata.");
        } catch (AdventureTimeException e) {
            io.error(e.getMessage());
        }
    }

    public void listReceivedBookings() {
        try {
            var bookings = AppContext.getInstance()
                    .manageHotelsController()
                    .getReceivedBookings();
            io.title("PRENOTAZIONI RICEVUTE");
            if (bookings.isEmpty()) {
                io.info("Nessuna prenotazione ricevuta.");
            }
            bookings.forEach(booking -> io.info(
                    "#" + booking.getId()
                            + " | " + booking.getHotel().getName()
                            + " | " + booking.getCheckIn()
                            + " -> " + booking.getCheckOut()
                            + " | persone " + booking.getPeople()
                            + " | €" + booking.getTotalPrice()
                            + " | " + booking.getStatusLabel()));
        } catch (AdventureTimeException e) {
            io.error(e.getMessage());
        }
    }

    public void cancelReceivedBooking() {
        long id = io.readLong("Id prenotazione da annullare: ");
        try {
            AppContext.getInstance()
                    .manageHotelsController()
                    .cancelReceivedBooking(id);
            io.info("Prenotazione annullata.");
        } catch (AdventureTimeException e) {
            io.error(e.getMessage());
        }
    }

    public void approveReceivedBooking() {
        long id = io.readLong("Id richiesta da approvare: ");
        try {
            AppContext.getInstance().manageHotelsController()
                    .approveReceivedBooking(id);
            io.info("Richiesta approvata e prenotazione confermata.");
        } catch (AdventureTimeException e) {
            io.error(e.getMessage());
        }
    }

    public void rejectReceivedBooking() {
        long id = io.readLong("Id richiesta da rifiutare: ");
        try {
            AppContext.getInstance().manageHotelsController()
                    .rejectReceivedBooking(id);
            io.info("Richiesta rifiutata.");
        } catch (AdventureTimeException e) {
            io.error(e.getMessage());
        }
    }
}
