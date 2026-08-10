package org.example.adventuretime;

import org.example.adventuretime.application_controller.LoginApplicationController;
import org.example.adventuretime.application_controller.ManageBookingsApplicationController;
import org.example.adventuretime.bean.BookingRequestBean;
import org.example.adventuretime.bean.CredentialsBean;
import org.example.adventuretime.bean.SearchCriteriaBean;
import org.example.adventuretime.dao.memory.InMemoryDAOFactory;
import org.example.adventuretime.facade.BookingFacade;
import org.example.adventuretime.model.ExtraService;
import org.example.adventuretime.session.FlowContext;
import org.example.adventuretime.session.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Responsabile test: Sammy Signorile (matricola da inserire). */
class ManageBookingsApplicationControllerTest {

    private static final LocalDate CHECK_IN = LocalDate.of(2027, 11, 1);
    private static final LocalDate CHECK_OUT = LocalDate.of(2027, 11, 4);

    private ManageBookingsApplicationController controller;
    private InMemoryDAOFactory factory;

    @BeforeEach
    void setUp() throws Exception {
        factory = new InMemoryDAOFactory();

        UserSession session = new UserSession();
        FlowContext flow = new FlowContext();

        new LoginApplicationController(
                factory.getUserDAO(),
                session,
                flow
        ).login(new CredentialsBean("mario@test.com", "1234"));

        BookingFacade facade = new BookingFacade(
                factory.getHotelDAO(),
                factory.getBookingDAO(),
                factory.getUserDAO(),
                session
        );

        controller = new ManageBookingsApplicationController(
                factory.getHotelDAO(),
                factory.getBookingDAO(),
                session,
                flow,
                facade
        );

        prepareSelectedHotel();
    }

    @Test
    void decoratorAddsSelectedInsuranceToQuote() throws Exception {
        BookingRequestBean request = requestWith(
                Set.of(ExtraService.HEALTH_INSURANCE)
        );

        var quote = controller.getQuote(request);

        assertEquals(new BigDecimal("330.00"), quote.getBasePrice());
        assertEquals(new BigDecimal("50.00"), quote.getExtrasPrice());
        assertEquals(new BigDecimal("380.00"), quote.getTotalPrice());
    }

    @Test
    void confirmationPersistsBooking() throws Exception {
        int before = factory.getBookingDAO().findByUserId(1).size();

        var booking = controller.confirm(requestWith(Set.of()));

        int after = factory.getBookingDAO().findByUserId(1).size();

        assertTrue(booking.getId() > 0);
        assertEquals(before + 1, after);
    }

    @Test
    void searchReturnsOnlyCompatibleHotels() throws Exception {
        var results = controller.search(new SearchCriteriaBean(
                "Roma",
                LocalDate.of(2027, 12, 1),
                LocalDate.of(2027, 12, 4),
                2,
                new BigDecimal("150.00")
        ));

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(hotel -> hotel.getCapacity() >= 2));
    }

    private void prepareSelectedHotel() throws Exception {
        var results = controller.search(new SearchCriteriaBean(
                "Roma",
                CHECK_IN,
                CHECK_OUT,
                2,
                new BigDecimal("150.00")
        ));

        long hotelId = results.stream()
                .filter(hotel -> hotel.getId() == 2)
                .findFirst()
                .orElseThrow()
                .getId();

        controller.selectHotel(hotelId);
    }

    private BookingRequestBean requestWith(Set<ExtraService> extras) {
        return new BookingRequestBean(
                2,
                CHECK_IN,
                CHECK_OUT,
                2,
                extras,
                false
        );
    }
}
