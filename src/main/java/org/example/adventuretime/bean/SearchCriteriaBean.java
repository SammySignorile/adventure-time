package org.example.adventuretime.bean;

import org.example.adventuretime.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Bean di input della ricerca hotel.
 * I controlli tra più campi, come l'ordine delle date, restano nel controller.
 */
public class SearchCriteriaBean {

    private String city;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int people;
    private BigDecimal maximumPricePerNight;

    public SearchCriteriaBean() {
        // Costruttore vuoto utile per la costruzione graduale.
    }

    public SearchCriteriaBean(
            String city,
            LocalDate checkIn,
            LocalDate checkOut,
            int people,
            BigDecimal maximumPricePerNight
    ) {
        this.city = city;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.people = people;
        this.maximumPricePerNight = maximumPricePerNight;
    }

    public SearchCriteriaBean(SearchCriteriaBean other) {
        if (other == null) {
            throw new IllegalArgumentException(
                    "Il criterio da copiare non può essere nullo."
            );
        }
        this.city = other.city;
        this.checkIn = other.checkIn;
        this.checkOut = other.checkOut;
        this.people = other.people;
        this.maximumPricePerNight = other.maximumPricePerNight;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public BigDecimal getMaximumPricePerNight() {
        return maximumPricePerNight;
    }

    public void setMaximumPricePerNight(BigDecimal maximumPricePerNight) {
        this.maximumPricePerNight = maximumPricePerNight;
    }

    public void validateSyntax() throws ValidationException {
        if (city == null || city.isBlank()) {
            throw new ValidationException("La città è obbligatoria.");
        }

        if (checkIn == null) {
            throw new ValidationException("La data di arrivo è obbligatoria.");
        }

        if (checkOut == null) {
            throw new ValidationException("La data di partenza è obbligatoria.");
        }

        if (people <= 0) {
            throw new ValidationException(
                    "Il numero di persone deve essere maggiore di zero."
            );
        }

        if (maximumPricePerNight == null
                || maximumPricePerNight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(
                    "Il prezzo massimo deve essere maggiore di zero."
            );
        }
    }
}