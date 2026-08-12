package org.example.adventuretime.bean;

import org.example.adventuretime.exception.ValidationException;

/**
 * Dati inseriti dal viaggiatore per il pagamento simulato.
 * Il numero completo e il CVV vengono usati solo per la validazione iniziale
 * e non vengono salvati nella prenotazione.
 */
public class PaymentDetailsBean {

    private String cardNumber;
    private String expiry;
    private String cvv;
    private String cardHolder;

    public PaymentDetailsBean() {
        // Costruttore vuoto da Java Bean.
    }

    public PaymentDetailsBean(
            String cardNumber,
            String expiry,
            String cvv,
            String cardHolder
    ) {
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvv = cvv;
        this.cardHolder = cardHolder;
    }

    public PaymentDetailsBean(PaymentDetailsBean other) {
        if (other == null) {
            throw new IllegalArgumentException(
                    "I dati di pagamento da copiare non possono essere nulli.");
        }
        this.cardNumber = other.cardNumber;
        this.expiry = other.expiry;
        this.cvv = other.cvv;
        this.cardHolder = other.cardHolder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void validate() throws ValidationException {
        String normalizedNumber = normalizedCardNumber();
        if (!normalizedNumber.matches("\\d{13,19}")) {
            throw new ValidationException(
                    "Il numero della carta deve contenere da 13 a 19 cifre.");
        }
        if (expiry == null
                || !expiry.trim().matches("(0[1-9]|1[0-2])/\\d{2}")) {
            throw new ValidationException(
                    "La scadenza della carta deve essere nel formato MM/AA.");
        }
        if (cvv == null || !cvv.trim().matches("\\d{3,4}")) {
            throw new ValidationException(
                    "Il CVV deve contenere 3 o 4 cifre.");
        }
        if (cardHolder == null || cardHolder.isBlank()) {
            throw new ValidationException(
                    "Il nome dell'intestatario della carta e obbligatorio.");
        }
    }

    public String normalizedCardNumber() {
        if (cardNumber == null) {
            return "";
        }
        return cardNumber.replace(" ", "").replace("-", "");
    }
}
