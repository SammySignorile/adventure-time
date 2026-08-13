package org.example.adventuretime.ui.cli;

import org.example.adventuretime.AppContext;
import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.bean.SearchCriteriaBean;
import org.example.adventuretime.exception.AdventureTimeException;

import java.util.List;

public final class SearchHotelCLIGraphicController {

    private final CliIO io;

    public SearchHotelCLIGraphicController(CliIO io) {
        this.io = io;
    }

    public SearchCriteriaBean readCriteria() {
        io.title("CERCA HOTEL");
        return new SearchCriteriaBean(
                io.readText("Città: "),
                io.readDate("Check-in"),
                io.readDate("Check-out"),
                io.readInt("Numero persone: "),
                io.readDecimal("Prezzo massimo per notte: ")
        );
    }

    public HotelBean execute() {
        try {
            SearchCriteriaBean criteria = readCriteria();
            List<HotelBean> hotels = AppContext.getInstance()
                    .manageBookingsController()
                    .search(criteria);

            if (hotels.isEmpty()) {
                io.info("Nessuna struttura disponibile.");
                return null;
            }

            io.title("RISULTATI");
            for (int index = 0; index < hotels.size(); index++) {
                HotelBean hotel = hotels.get(index);
                io.info((index + 1) + ") " + hotel
                        + " | capienza " + hotel.getCapacity());
            }

            int choice = io.readInt("Seleziona un hotel (0 annulla): ");
            if (choice == 0) {
                return null;
            }
            if (choice < 1 || choice > hotels.size()) {
                io.error("Scelta non valida.");
                return null;
            }

            HotelBean selected = hotels.get(choice - 1);
            AppContext.getInstance()
                    .manageBookingsController()
                    .selectHotel(selected.getId());
            return selected;
        } catch (AdventureTimeException e) {
            io.error(e.getMessage());
            return null;
        }
    }
}
