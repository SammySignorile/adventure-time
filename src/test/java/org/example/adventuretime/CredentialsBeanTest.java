package org.example.adventuretime;

import org.example.adventuretime.bean.CredentialsBean;
import org.example.adventuretime.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Responsabile test: Sammy Signorile (matricola da inserire). */
class CredentialsBeanTest {

    @Test
    void acceptsWellFormedEmail() {
        var credentials = new CredentialsBean("mario@test.com", "1234");

        assertDoesNotThrow(credentials::validateSyntax);
    }

    @Test
    void rejectsEmailWithoutDomainDot() {
        var credentials = new CredentialsBean("mario@test", "1234");

        assertThrows(ValidationException.class, credentials::validateSyntax);
    }

    @Test
    void rejectsEmailWithMoreThanOneAtSign() {
        var credentials = new CredentialsBean(
                "mario@test@example.com",
                "1234"
        );

        assertThrows(ValidationException.class, credentials::validateSyntax);
    }

    @Test
    void rejectsBlankEmail() {
        var credentials = new CredentialsBean("  ", "1234");

        assertThrows(ValidationException.class, credentials::validateSyntax);
    }

    @Test
    void rejectsNullEmail() {
        var credentials = new CredentialsBean(null, "1234");

        assertThrows(ValidationException.class, credentials::validateSyntax);
    }

    @Test
    void rejectsEmailContainingWhitespace() {
        var credentials = new CredentialsBean("mario @test.com", "1234");

        assertThrows(ValidationException.class, credentials::validateSyntax);
    }

    @Test
    void rejectsEmailWithoutDomainName() {
        var credentials = new CredentialsBean("mario@.com", "1234");

        assertThrows(ValidationException.class, credentials::validateSyntax);
    }

    @Test
    void rejectsEmailWithTrailingDot() {
        var credentials = new CredentialsBean("mario@test.", "1234");

        assertThrows(ValidationException.class, credentials::validateSyntax);
    }

    @Test
    void rejectsBlankPassword() {
        var credentials = new CredentialsBean("mario@test.com", "  ");

        assertThrows(ValidationException.class, credentials::validateSyntax);
    }

    @Test
    void rejectsNullPassword() {
        var credentials = new CredentialsBean("mario@test.com", null);

        assertThrows(ValidationException.class, credentials::validateSyntax);
    }
}
