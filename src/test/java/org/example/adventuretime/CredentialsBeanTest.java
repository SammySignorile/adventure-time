package org.example.adventuretime;

import org.example.adventuretime.bean.CredentialsBean;
import org.example.adventuretime.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CredentialsBeanTest {

    @Test
    void acceptsWellFormedEmail() {
        var credentials = new CredentialsBean("mario@test.com", "1234");

        assertDoesNotThrow(credentials::validateSyntax);
    }

    @Test
    void rejectsMalformedEmails() {
        String[] invalidEmails = {
                "mario@test",
                "mario@test@example.com",
                "  ",
                null,
                "mario @test.com",
                "mario@.com",
                "mario@test."
        };

        for (String email : invalidEmails) {
            CredentialsBean credentials = new CredentialsBean(email, "1234");
            assertThrows(
                    ValidationException.class,
                    credentials::validateSyntax,
                    () -> "Email non rifiutata: " + email
            );
        }
    }

    @Test
    void rejectsMissingPasswords() {
        String[] invalidPasswords = {"  ", null};

        for (String password : invalidPasswords) {
            CredentialsBean credentials = new CredentialsBean(
                    "mario@test.com", password);
            assertThrows(
                    ValidationException.class,
                    credentials::validateSyntax
            );
        }
    }
}
