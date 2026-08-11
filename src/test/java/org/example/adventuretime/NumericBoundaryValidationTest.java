package org.example.adventuretime;

import org.example.adventuretime.bean.BookingRequestBean;
import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.bean.SearchCriteriaBean;
import org.example.adventuretime.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Verifica valori piccoli, grandi e negativi degli input numerici. */
class NumericBoundaryValidationTest {

    private static final LocalDate CHECK_IN = LocalDate.of(2027, 10, 10);
    private static final LocalDate CHECK_OUT = LocalDate.of(2027, 10, 12);

    @Test
    void validatesSmallLargeAndNegativeSearchPeopleValues() {
        assertValidSearchPeople(1);
        assertValidSearchPeople(1_000);

        SearchCriteriaBean negative = validSearchCriteria();
        negative.setPeople(-1);
        assertThrows(ValidationException.class, negative::validateSyntax);
    }

    @Test
    void validatesSmallLargeAndNegativeSearchPrices() {
        assertValidSearchPrice("0.01");
        assertValidSearchPrice("1000000.00");

        SearchCriteriaBean negative = validSearchCriteria();
        negative.setMaximumPricePerNight(new BigDecimal("-1.00"));
        assertThrows(ValidationException.class, negative::validateSyntax);
    }

    @Test
    void validatesSmallLargeAndNegativeHotelIdentifiers() {
        assertValidHotelId(0);
        assertValidHotelId(Long.MAX_VALUE);

        HotelBean negative = validHotel();
        negative.setId(-1);
        assertThrows(ValidationException.class, negative::validateSyntax);
    }

    @Test
    void validatesSmallLargeAndNegativeHotelPrices() {
        assertValidHotelPrice("0.01");
        assertValidHotelPrice("1000000.00");

        HotelBean negative = validHotel();
        negative.setPricePerNight(new BigDecimal("-1.00"));
        assertThrows(ValidationException.class, negative::validateSyntax);
    }

    @Test
    void validatesSmallLargeAndNegativeHotelCapacities() {
        assertValidHotelCapacity(1);
        assertValidHotelCapacity(1_000);

        HotelBean negative = validHotel();
        negative.setCapacity(-1);
        assertThrows(ValidationException.class, negative::validateSyntax);
    }

    @Test
    void validatesSmallLargeAndNegativeBookingHotelIdentifiers() {
        assertValidBookingHotelId(1);
        assertValidBookingHotelId(Long.MAX_VALUE);

        BookingRequestBean negative = validBookingRequest();
        negative.setHotelId(-1);
        assertThrows(ValidationException.class, negative::validateSyntax);
    }

    @Test
    void validatesSmallLargeAndNegativeBookingPeopleValues() {
        assertValidBookingPeople(1);
        assertValidBookingPeople(1_000);

        BookingRequestBean negative = validBookingRequest();
        negative.setPeople(-1);
        assertThrows(ValidationException.class, negative::validateSyntax);
    }

    private static void assertValidSearchPeople(int people) {
        SearchCriteriaBean criteria = validSearchCriteria();
        criteria.setPeople(people);
        assertDoesNotThrow(criteria::validateSyntax);
    }

    private static void assertValidSearchPrice(String price) {
        SearchCriteriaBean criteria = validSearchCriteria();
        criteria.setMaximumPricePerNight(new BigDecimal(price));
        assertDoesNotThrow(criteria::validateSyntax);
    }

    private static void assertValidHotelId(long id) {
        HotelBean hotel = validHotel();
        hotel.setId(id);
        assertDoesNotThrow(hotel::validateSyntax);
    }

    private static void assertValidHotelPrice(String price) {
        HotelBean hotel = validHotel();
        hotel.setPricePerNight(new BigDecimal(price));
        assertDoesNotThrow(hotel::validateSyntax);
    }

    private static void assertValidHotelCapacity(int capacity) {
        HotelBean hotel = validHotel();
        hotel.setCapacity(capacity);
        assertDoesNotThrow(hotel::validateSyntax);
    }

    private static void assertValidBookingHotelId(long hotelId) {
        BookingRequestBean request = validBookingRequest();
        request.setHotelId(hotelId);
        assertDoesNotThrow(request::validateSyntax);
    }

    private static void assertValidBookingPeople(int people) {
        BookingRequestBean request = validBookingRequest();
        request.setPeople(people);
        assertDoesNotThrow(request::validateSyntax);
    }

    private static SearchCriteriaBean validSearchCriteria() {
        return new SearchCriteriaBean(
                "Roma",
                CHECK_IN,
                CHECK_OUT,
                2,
                new BigDecimal("150.00")
        );
    }

    private static HotelBean validHotel() {
        HotelBean hotel = new HotelBean();
        hotel.setName("Hotel di prova");
        hotel.setCity("Roma");
        hotel.setRoomType("Camera doppia");
        hotel.setPricePerNight(new BigDecimal("100.00"));
        hotel.setCapacity(2);
        return hotel;
    }

    private static BookingRequestBean validBookingRequest() {
        return new BookingRequestBean(
                1,
                CHECK_IN,
                CHECK_OUT,
                2,
                Set.of(),
                false
        );
    }
}
