package org.example.adventuretime.ui.cli;

import org.example.adventuretime.AppContext;
import org.example.adventuretime.bean.BookingBean;
import org.example.adventuretime.bean.UserBean;
import org.example.adventuretime.exception.AdventureTimeException;
import org.example.adventuretime.model.Role;

import java.util.List;

public final class ProfileCLIGraphicController {

    private final CliIO io;

    public ProfileCLIGraphicController(CliIO io) {
        this.io = io;
    }

    public void execute() {
        try {
            AppContext context = AppContext.getInstance();

            /*
             * I dati personali dell'utente vengono recuperati
             * dal controller del login e della sessione.
             */
            UserBean user = context
                    .loginController()
                    .getCurrentUser();

            List<BookingBean> bookings;

            /*
             * Il contenuto della sezione prenotazioni cambia
             * in base al ruolo dell'utente autenticato.
             */
            if (user.getRole() == Role.GESTORE) {

                /*
                 * L'albergatore visualizza le prenotazioni
                 * ricevute per le proprie strutture.
                 */
                bookings = context
                        .manageHotelsController()
                        .getReceivedBookings();

            } else {

                /*
                 * Il viaggiatore visualizza le prenotazioni
                 * che ha effettuato.
                 */
                bookings = context
                        .manageBookingsController()
                        .getMyBookings();
            }

            showUserData(user);
            showBookings(bookings);

        } catch (AdventureTimeException e) {
            io.error(e.getMessage());
        }
    }

    private void showUserData(UserBean user) {
        io.title("PROFILO");
        io.info("Nome: " + user.getFullName());
        io.info("Email: " + user.getEmail());
        io.info("Ruolo: " + user.getRoleLabel());
        io.info("Punti: " + user.getPoints());
        io.info("");
    }

    private void showBookings(List<BookingBean> bookings) {
        io.info("Prenotazioni:");

        if (bookings.isEmpty()) {
            io.info("- Nessuna prenotazione.");
            return;
        }

        for (BookingBean booking : bookings) {
            io.info("- #" + booking.getId()
                    + " " + booking.getHotel().getName()
                    + " | " + booking.getCheckIn()
                    + " -> " + booking.getCheckOut()
                    + " | persone: " + booking.getPeople()
                    + " | €" + booking.getTotalPrice()
                    + " | stato: " + booking.getStatusLabel()
                    + " | extra: " + booking.getExtrasLabel());
        }
    }
}
