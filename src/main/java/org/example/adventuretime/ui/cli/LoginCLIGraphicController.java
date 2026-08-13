package org.example.adventuretime.ui.cli;

import org.example.adventuretime.AppContext;
import org.example.adventuretime.bean.CredentialsBean;
import org.example.adventuretime.bean.UserBean;
import org.example.adventuretime.exception.AdventureTimeException;

public final class LoginCLIGraphicController {

    private final CliIO io;

    public LoginCLIGraphicController(CliIO io) {
        this.io = io;
    }

    public UserBean execute() {
        io.title("ADVENTURE TIME - LOGIN");
        while (true) {
            String email = io.readText("Email: ");
            String password = io.readText("Password: ");

            try {
                UserBean user = AppContext.getInstance()
                        .loginController()
                        .login(new CredentialsBean(email, password));
                io.info("Benvenuto " + user.getFullName()
                        + " (" + user.getRoleLabel() + ")");
                return user;
            } catch (AdventureTimeException e) {
                io.error(e.getMessage());
            }
        }
    }
}
