package org.example.adventuretime.mapper;

import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.model.HotelRoom;

public final class HotelMapper {

    private HotelMapper() {
    }

    public static HotelBean toBean(HotelRoom room) {
        HotelBean bean = new HotelBean();
        bean.setId(room.getId());
        bean.setManagerId(room.getManagerId());
        bean.setName(room.getName());
        bean.setCity(room.getCity());
        bean.setRoomType(room.getRoomType());
        bean.setServices(room.getServices());
        bean.setDistanceFromCenter(room.getDistanceFromCenter());
        bean.setPricePerNight(room.getPricePerNight());
        bean.setImageFileName(room.getImageFileName());
        bean.setCapacity(room.getCapacity());
        return bean;
    }

    public static HotelRoom toEntity(HotelBean bean) {
        HotelRoom room = new HotelRoom();
        room.setId(bean.getId());
        room.setManagerId(bean.getManagerId());
        room.setName(bean.getName());
        room.setCity(bean.getCity());
        room.setRoomType(bean.getRoomType());
        room.setServices(bean.getServices());
        room.setDistanceFromCenter(bean.getDistanceFromCenter());
        room.setPricePerNight(bean.getPricePerNight());
        room.setImageFileName(bean.getImageFileName());
        room.setCapacity(bean.getCapacity());
        return room;
    }
}
