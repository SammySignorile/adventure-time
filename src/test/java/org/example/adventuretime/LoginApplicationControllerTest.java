package org.example.adventuretime;

import org.example.adventuretime.application_controller.LoginApplicationController;
import org.example.adventuretime.bean.CredentialsBean;
import org.example.adventuretime.dao.memory.InMemoryDAOFactory;
import org.example.adventuretime.exception.AuthenticationException;
import org.example.adventuretime.model.Role;
import org.example.adventuretime.session.FlowContext;
import org.example.adventuretime.session.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Responsabile test: INSERIRE NOME, COGNOME E MATRICOLA. */
class LoginApplicationControllerTest {

    private LoginApplicationController controller;
    private UserSession session;

    @BeforeEach
    void setUp() {
        InMemoryDAOFactory factory = new InMemoryDAOFactory();
        session = new UserSession();
        controller = new LoginApplicationController(
                factory.getUserDAO(), session, new FlowContext());
    }

    @Test
    void loginTravelerCreatesSession() throws Exception {
        var user = controller.login(
                new CredentialsBean("mario@test.com", "1234"));

        assertEquals(Role.CLIENTE, user.getRole());
        assertTrue(session.isTraveler());
    }

    @Test
    void wrongPasswordIsRejected() {
        CredentialsBean credentials =
                new CredentialsBean("mario@test.com", "wrong");

        assertThrows(AuthenticationException.class,
                () -> controller.login(credentials));
        assertFalse(session.isAuthenticated());
    }

    @Test
    void logoutClearsSession() throws Exception {
        controller.login(new CredentialsBean("mario@test.com", "1234"));
        controller.logout();
        assertFalse(session.isAuthenticated());
    }
}
