package org.example.adventuretime.bean;

import org.example.adventuretime.model.Role;

/**
 * Bean di output/sessione. Non contiene la password e non rappresenta input
 * libero dell'utente, quindi non necessita di validateSyntax().
 */
public class UserBean {

    private long id;
    private String firstName;
    private String lastName;
    private int points;
    private String email;
    private Role role;

    public UserBean() {
        // Costruttore vuoto utile per il mapping.
    }

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
        if (other == null) {
            throw new IllegalArgumentException(
                    "L'utente da copiare non può essere nullo."
            );
        }
        this.id = other.id;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.points = other.points;
        this.email = other.email;
        this.role = other.role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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