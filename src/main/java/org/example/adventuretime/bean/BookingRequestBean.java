package org.example.adventuretime.bean;

import org.example.adventuretime.exception.ValidationException;
import org.example.adventuretime.model.ExtraService;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

/**
 * Bean di input del checkout.
 * Non controlla disponibilità, capienza o punti: quelle sono regole applicative.
 */
public class BookingRequestBean {

    private long hotelId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int people;
    private EnumSet<ExtraService> extras =
            EnumSet.noneOf(ExtraService.class);
    private boolean usePoints;

    public BookingRequestBean() {
        // Costruttore vuoto utile per la costruzione graduale.
    }

    public BookingRequestBean(
            long hotelId,
            LocalDate checkIn,
            LocalDate checkOut,
            int people,
            Set<ExtraService> extras,
            boolean usePoints
    ) {
        this.hotelId = hotelId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.people = people;
        setExtras(extras);
        this.usePoints = usePoints;
    }

    public BookingRequestBean(BookingRequestBean other) {
        if (other == null) {
            throw new IllegalArgumentException(
                    "La richiesta da copiare non può essere nulla."
            );
        }
        this.hotelId = other.hotelId;
        this.checkIn = other.checkIn;
        this.checkOut = other.checkOut;
        this.people = other.people;
        this.extras = copyExtras(other.extras);
        this.usePoints = other.usePoints;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
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

    public Set<ExtraService> getExtras() {
        return copyExtras(extras);
    }

    public void setExtras(Set<ExtraService> extras) {
        this.extras = copyExtras(extras);
    }

    public boolean isUsePoints() {
        return usePoints;
    }

    public void setUsePoints(boolean usePoints) {
        this.usePoints = usePoints;
    }

    public void validateSyntax() throws ValidationException {
        if (hotelId <= 0) {
            throw new ValidationException(
                    "Non è stato selezionato un hotel valido."
            );
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
    }

    private static EnumSet<ExtraService> copyExtras(
            Set<ExtraService> source
    ) {
        if (source == null || source.isEmpty()) {
            return EnumSet.noneOf(ExtraService.class);
        }
        return EnumSet.copyOf(source);
    }
}
