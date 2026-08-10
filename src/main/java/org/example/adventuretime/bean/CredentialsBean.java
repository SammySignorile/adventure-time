package org.example.adventuretime.bean;

import org.example.adventuretime.exception.ValidationException;

/**
 * Bean di input usato dal caso d'uso di login.
 * Contiene soltanto controlli formali sui dati inseriti dall'utente.
 */
public class CredentialsBean {

    private String email;
    private String password;

    public CredentialsBean() {
        // Costruttore vuoto utile per JavaFX/CLI e per la costruzione graduale.
    }

    public CredentialsBean(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Verifica la forma delle credenziali, non la loro esistenza nel database.
     */
    public void validateSyntax() throws ValidationException {
        if (email == null || email.isBlank()) {
            throw new ValidationException("L'email è obbligatoria.");
        }

        if (!isValidEmail(email.trim())) {
            throw new ValidationException("Il formato dell'email non è valido.");
        }

        if (password == null || password.isBlank()) {
            throw new ValidationException("La password è obbligatoria.");
        }
    }

    private static boolean isValidEmail(String value) {
        int atIndex = value.indexOf('@');
        if (atIndex <= 0 || atIndex != value.lastIndexOf('@')) {
            return false;
        }

        int dotIndex = value.indexOf('.', atIndex + 2);
        if (dotIndex < 0 || dotIndex == value.length() - 1) {
            return false;
        }

        return value.chars().noneMatch(Character::isWhitespace);
    }
}
