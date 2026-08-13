package org.example.adventuretime.mapper;

import org.example.adventuretime.bean.BookingBean;
import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.model.Booking;

/** Converte l'entità Booking nel Bean mostrato dalle interfacce. */
public final class BookingMapper {

    private BookingMapper() {
    }

    public static BookingBean toBean(Booking booking, HotelBean hotel) {
        BookingBean bean = new BookingBean();
        bean.setId(booking.getId());
        bean.setUserId(booking.getUserId());
        bean.setHotel(hotel);
        bean.setCheckIn(booking.getCheckIn());
        bean.setCheckOut(booking.getCheckOut());
        bean.setPeople(booking.getPeople());
        bean.setTotalPrice(booking.getTotalPrice());
        bean.setExtras(booking.getExtras());
        bean.setPointsUsed(booking.getPointsUsed());
        bean.setStatus(booking.getStatus());
        return bean;
    }
}
