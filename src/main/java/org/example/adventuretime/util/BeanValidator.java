package org.example.adventuretime.util;

import org.example.adventuretime.bean.BookingRequestBean;
import org.example.adventuretime.bean.CredentialsBean;
import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.bean.SearchCriteriaBean;
import org.example.adventuretime.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class BeanValidator {

    private BeanValidator() {
    }

    public static void validateCredentials(CredentialsBean bean)
            throws ValidationException {
        if (bean == null
                || isBlank(bean.getEmail())
                || isBlank(bean.getPassword())) {
            throw new ValidationException(
                    "Email e password sono obbligatorie.");
        }
        if (!bean.getEmail().contains("@")) {
            throw new ValidationException("Formato email non valido.");
        }
    }

    public static void validateSearch(SearchCriteriaBean bean)
            throws ValidationException {
        if (bean == null || isBlank(bean.getCity())) {
            throw new ValidationException("Inserire una città.");
        }
        validateDates(bean.getCheckIn(), bean.getCheckOut());
        if (bean.getPeople() <= 0) {
            throw new ValidationException(
                    "Il numero di persone deve essere positivo.");
        }
        if (bean.getMaximumPricePerNight() == null
                || bean.getMaximumPricePerNight()
                .compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(
                    "Il prezzo massimo deve essere positivo.");
        }
    }

    public static void validateBookingRequest(BookingRequestBean bean)
            throws ValidationException {
        if (bean == null || bean.getHotelId() <= 0) {
            throw new ValidationException("Struttura non selezionata.");
        }
        validateDates(bean.getCheckIn(), bean.getCheckOut());
        if (bean.getPeople() <= 0) {
            throw new ValidationException(
                    "Il numero di persone deve essere positivo.");
        }
    }

    public static void validateHotel(HotelBean bean)
            throws ValidationException {
        if (bean == null
                || isBlank(bean.getName())
                || isBlank(bean.getCity())
                || isBlank(bean.getRoomType())) {
            throw new ValidationException(
                    "Nome, città e tipo di camera sono obbligatori.");
        }
        if (bean.getPricePerNight() == null
                || bean.getPricePerNight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(
                    "Il prezzo per notte deve essere positivo.");
        }
        if (bean.getCapacity() <= 0) {
            throw new ValidationException(
                    "La capienza deve essere positiva.");
        }
    }

    private static void validateDates(
            LocalDate checkIn,
            LocalDate checkOut
    ) throws ValidationException {
        if (checkIn == null || checkOut == null) {
            throw new ValidationException(
                    "Le date di arrivo e partenza sono obbligatorie.");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new ValidationException(
                    "La partenza deve essere successiva all'arrivo.");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new ValidationException(
                    "La data di arrivo non può essere nel passato.");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
