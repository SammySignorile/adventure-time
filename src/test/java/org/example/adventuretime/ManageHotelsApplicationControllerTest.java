package org.example.adventuretime;

import org.example.adventuretime.application_controller.LoginApplicationController;
import org.example.adventuretime.application_controller.ManageHotelsApplicationController;
import org.example.adventuretime.bean.CredentialsBean;
import org.example.adventuretime.dao.memory.InMemoryDAOFactory;
import org.example.adventuretime.model.BookingStatus;
import org.example.adventuretime.session.FlowContext;
import org.example.adventuretime.session.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManageHotelsApplicationControllerTest {

    private ManageHotelsApplicationController controller;

    @BeforeEach
    void setUp() throws Exception {
        InMemoryDAOFactory factory = new InMemoryDAOFactory();
        UserSession session = new UserSession();

        new LoginApplicationController(
                factory.getUserDAO(),
                session,
                new FlowContext()
        ).login(new CredentialsBean("mike@gmail.com", "1234"));

        controller = new ManageHotelsApplicationController(
                factory.getHotelDAO(),
                factory.getBookingDAO(),
                session
        );
    }

    @Test
    void hotelierCanCancelReceivedBooking() throws Exception {
        controller.cancelReceivedBooking(1);

        var booking = controller.getReceivedBookings().stream()
                .filter(item -> item.getId() == 1)
                .findFirst()
                .orElseThrow();

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }
}
