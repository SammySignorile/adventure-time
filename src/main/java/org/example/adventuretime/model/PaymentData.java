package org.example.adventuretime.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Riferimento sicuro alla carta usata nel checkout simulato.
 * Non contiene numero completo, scadenza o CVV.
 */
public class PaymentData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String token;
    private String cardHolder;
    private String lastFourDigits;

    public PaymentData() {
        // Necessario per mapper e persistenza.
    }

    public PaymentData(String token, String cardHolder, String lastFourDigits) {
        this.token = token;
        this.cardHolder = cardHolder;
        this.lastFourDigits = lastFourDigits;
    }

    public PaymentData(PaymentData other) {
        this(other.token, other.cardHolder, other.lastFourDigits);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getLastFourDigits() {
        return lastFourDigits;
    }

    public void setLastFourDigits(String lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }

    public String getMaskedLabel() {
        if (lastFourDigits == null || lastFourDigits.isBlank()) {
            return "Pagamento non disponibile";
        }
        return "Carta **** " + lastFourDigits;
    }
}
