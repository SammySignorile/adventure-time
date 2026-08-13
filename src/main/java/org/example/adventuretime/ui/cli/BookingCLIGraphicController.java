package org.example.adventuretime.ui.cli;

import org.example.adventuretime.AppContext;
import org.example.adventuretime.bean.BookingQuoteBean;
import org.example.adventuretime.bean.BookingRequestBean;
import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.bean.SearchCriteriaBean;
import org.example.adventuretime.bean.PaymentDetailsBean;
import org.example.adventuretime.exception.AdventureTimeException;
import org.example.adventuretime.model.ExtraService;

import java.util.EnumSet;

public final class BookingCLIGraphicController {

    private final CliIO io;

    public BookingCLIGraphicController(CliIO io) {
        this.io = io;
    }

    public void execute(HotelBean hotel) {
        SearchCriteriaBean criteria;
        try {
            criteria = AppContext.getInstance()
                    .manageBookingsController()
                    .getCurrentSearchCriteria();
        } catch (AdventureTimeException e) {
            io.error(e.getMessage());
            return;
        }

        EnumSet<ExtraService> extras = EnumSet.noneOf(ExtraService.class);
        if (io.readYesNo("Aggiungere assicurazione annullamento (+80€)?")) {
            extras.add(ExtraService.CANCELLATION_INSURANCE);
        }
        if (io.readYesNo("Aggiungere assicurazione medica (+50€)?")) {
            extras.add(ExtraService.HEALTH_INSURANCE);
        }
        if (io.readYesNo("Aggiungere cambio data flessibile (+40€)?")) {
            extras.add(ExtraService.FLEXIBLE_DATE);
        }
        boolean usePoints = io.readYesNo("Usare i punti disponibili?");

        BookingRequestBean request = new BookingRequestBean(
                hotel.getId(),
                criteria.getCheckIn(),
                criteria.getCheckOut(),
                criteria.getPeople(),
                extras,
                usePoints
        );

        try {
            BookingQuoteBean quote = AppContext.getInstance()
                    .manageBookingsController()
                    .getQuote(request);

            io.title("RIEPILOGO");
            io.info("Hotel: " + hotel.getName());
            io.info("Notti: " + quote.getNights());
            io.info("Prezzo base: €" + quote.getBasePrice());
            io.info("Extra: €" + quote.getExtrasPrice());
            io.info("Sconto punti: -€" + quote.getPointsDiscount());
            io.info("TOTALE: €" + quote.getTotalPrice());

            if (!io.readYesNo("Inviare la richiesta di prenotazione?")) {
                io.info("Prenotazione annullata.");
                return;
            }

            request.setPaymentDetails(new PaymentDetailsBean(
                    io.readText("Numero carta: "),
                    io.readText("Scadenza MM/AA: "),
                    io.readText("CVV: "),
                    io.readText("Nome intestatario: ")
            ));

            var booking = AppContext.getInstance()
                    .manageBookingsController()
                    .requestBooking(request);
            io.info("Richiesta inviata. Codice: " + booking.getId());
            io.info("Il pagamento avverra dopo l'approvazione del venditore.");
        } catch (AdventureTimeException e) {
            io.error(e.getMessage());
        }
    }
}
