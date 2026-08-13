package org.example.adventuretime.bean;

import org.example.adventuretime.model.Role;

/**
 * Bean di output/sessione. Non contiene la password e non rappresenta input
 * libero dell'utente, quindi non necessita di validateSyntax().
 */
public class UserBean {

    private final long id;
    private final String firstName;
    private final String lastName;
    private int points;
    private final String email;
    private final Role role;

    public UserBean(
            long id,
            String firstName,
            String lastName,
            int points,
            String email,
            Role role
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.points = points;
        this.email = email;
        this.role = role;
    }

    public UserBean(UserBean other) {
        this(requireOther(other).id, other.firstName, other.lastName,
                other.points, other.email, other.role);
    }

    private static UserBean requireOther(UserBean other) {
        if (other == null) {
            throw new IllegalArgumentException(
                    "L'utente da copiare non può essere nullo."
            );
        }
        return other;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public String getFullName() {
        return (safe(firstName) + " " + safe(lastName)).trim();
    }

    public String getRoleLabel() {
        if (role == null) {
            return "Ruolo non definito";
        }

        return switch (role) {
            case CLIENTE -> "Viaggiatore";
            case GESTORE -> "Venditore";
            case ADMIN -> "Amministratore";
        };
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
