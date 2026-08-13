package org.example.adventuretime.session;

import org.example.adventuretime.bean.UserBean;
import org.example.adventuretime.model.Role;

import java.util.Optional;

public final class UserSession {

    private UserBean currentUser;

    public synchronized void login(UserBean user) {
        currentUser = new UserBean(user);
    }

    public synchronized void logout() {
        currentUser = null;
    }

    public synchronized boolean isAuthenticated() {
        return currentUser != null;
    }

    public synchronized Optional<UserBean> getCurrentUser() {
        return currentUser == null
                ? Optional.empty()
                : Optional.of(new UserBean(currentUser));
    }

    public synchronized UserBean requireUser() {
        if (currentUser == null) {
            throw new IllegalStateException("Nessun utente autenticato.");
        }
        return new UserBean(currentUser);
    }

    public synchronized boolean isTraveler() {
        return currentUser != null && currentUser.getRole() == Role.CLIENTE;
    }

    public synchronized boolean isVendor() {
        return currentUser != null && currentUser.getRole() == Role.GESTORE;
    }

    public synchronized void updatePoints(int newPoints) {
        if (currentUser != null) {
            currentUser.setPoints(newPoints);
        }
    }
}
