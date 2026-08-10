package org.example.adventuretime.application_controller;

import org.example.adventuretime.bean.CredentialsBean;
import org.example.adventuretime.bean.UserBean;
import org.example.adventuretime.dao.UserDAO;
import org.example.adventuretime.exception.AuthenticationException;
import org.example.adventuretime.exception.PersistenceException;
import org.example.adventuretime.exception.ValidationException;
import org.example.adventuretime.mapper.UserMapper;
import org.example.adventuretime.model.User;
import org.example.adventuretime.session.FlowContext;
import org.example.adventuretime.session.UserSession;

/**
 * Controller applicativo del caso d'uso di autenticazione.
 */
public final class LoginApplicationController {

    private final UserDAO userDAO;
    private final UserSession userSession;
    private final FlowContext flowContext;

    public LoginApplicationController(
            UserDAO userDAO,
            UserSession userSession,
            FlowContext flowContext
    ) {
        this.userDAO = userDAO;
        this.userSession = userSession;
        this.flowContext = flowContext;
    }

    public UserBean login(CredentialsBean credentials)
            throws ValidationException, PersistenceException,
            AuthenticationException {

        if (credentials == null) {
            throw new ValidationException(
                    "Le credenziali non sono state fornite."
            );
        }

        // Controlli formali eseguiti dal Bean.
        credentials.validateSyntax();

        // Controllo applicativo: l'utente deve esistere nella persistenza.
        User user = userDAO.findByCredentials(
                        credentials.getEmail().trim(),
                        credentials.getPassword()
                )
                .orElseThrow(() -> new AuthenticationException(
                        "Email o password non corrette."
                ));

        UserBean authenticatedUser = UserMapper.toBean(user);
        userSession.login(authenticatedUser);
        flowContext.clearBookingFlow();
        return authenticatedUser;
    }

    public UserBean getCurrentUser()
            throws AuthenticationException, PersistenceException {

        UserBean sessionUser = requireAuthenticatedUser();

        User freshUser = userDAO.findById(sessionUser.getId())
                .orElseThrow(() -> new AuthenticationException(
                        "L'utente della sessione non esiste più."
                ));

        UserBean updatedUser = UserMapper.toBean(freshUser);
        userSession.login(updatedUser);
        return updatedUser;
    }

    public void logout() {
        userSession.logout();
        flowContext.clearBookingFlow();
    }

    private UserBean requireAuthenticatedUser()
            throws AuthenticationException {

        if (!userSession.isAuthenticated()) {
            throw new AuthenticationException(
                    "Effettuare il login per continuare."
            );
        }
        return userSession.requireUser();
    }
}