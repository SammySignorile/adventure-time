package org.example.adventuretime.ui.cli;

import org.example.adventuretime.AppContext;
import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.bean.UserBean;

import java.util.Scanner;

/**
 * Main loop of the command-line boundary.
 *
 * The CLI has its own graphic controllers, but it reuses exactly the same
 * application controllers used by JavaFX. This is the key architectural rule:
 * two interfaces, one business flow.
 */
public final class CliApplication {

    private final CliIO io = new CliIO(new Scanner(System.in));

    public void start() {
        boolean running = true;
        while (running) {
            UserBean user = new LoginCLIGraphicController(io).execute();
            if (user.getRole() == org.example.adventuretime.model.Role.CLIENTE) {
                running = travelerLoop();
            } else if (user.getRole() == org.example.adventuretime.model.Role.GESTORE) {
                running = vendorLoop();
            } else {
                io.error("Il ruolo ADMIN non è previsto nei flussi di questa versione.");
                logout();
            }
        }
        io.info("Adventure Time terminato.");
    }

    private boolean travelerLoop() {
        SearchHotelCLIGraphicController search =
                new SearchHotelCLIGraphicController(io);
        BookingCLIGraphicController booking =
                new BookingCLIGraphicController(io);
        ProfileCLIGraphicController profile =
                new ProfileCLIGraphicController(io);

        while (true) {
            io.title("MENU VIAGGIATORE");
            io.info("1) Cerca e prenota un hotel");
            io.info("2) Profilo e prenotazioni");
            io.info("3) Logout");
            io.info("0) Esci");

            switch (io.readInt("Scelta: ")) {
                case 1 -> {
                    HotelBean selected = search.execute();
                    if (selected != null) {
                        booking.execute(selected);
                    }
                }
                case 2 -> profile.execute();
                case 3 -> {
                    logout();
                    return true;
                }
                case 0 -> {
                    logout();
                    return false;
                }
                default -> io.error("Scelta non valida.");
            }
        }
    }

    private boolean vendorLoop() {
        ManageHotelCLIGraphicController manage =
                new ManageHotelCLIGraphicController(io);
        ProfileCLIGraphicController profile =
                new ProfileCLIGraphicController(io);

        while (true) {
            io.title("MENU VENDITORE");
            io.info("1) Elenca le mie strutture");
            io.info("2) Aggiungi una struttura");
            io.info("3) Elimina una struttura");
            io.info("4) Prenotazioni ricevute");
            io.info("5) Annulla una prenotazione ricevuta");
            io.info("6) Profilo");
            io.info("7) Logout");
            io.info("0) Esci");

            switch (io.readInt("Scelta: ")) {
                case 1 -> manage.listHotels();
                case 2 -> manage.addHotel();
                case 3 -> manage.deleteHotel();
                case 4 -> manage.listReceivedBookings();
                case 5 -> manage.cancelReceivedBooking();
                case 6 -> profile.execute();
                case 7 -> {
                    logout();
                    return true;
                }
                case 0 -> {
                    logout();
                    return false;
                }
                default -> io.error("Scelta non valida.");
            }
        }
    }

    private void logout() {
        AppContext.getInstance().loginController().logout();
    }
}
