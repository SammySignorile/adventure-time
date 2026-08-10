package org.example.adventuretime.mapper;

import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.model.HotelRoom;

public final class HotelMapper {

    private HotelMapper() {
    }

    public static HotelBean toBean(HotelRoom room) {
        return new HotelBean(
                room.getId(),
                room.getManagerId(),
                room.getName(),
                room.getCity(),
                room.getRoomType(),
                room.getServices(),
                room.getDistanceFromCenter(),
                room.getPricePerNight(),
                room.getImageFileName(),
                room.getCapacity()
        );
    }

    public static HotelRoom toEntity(HotelBean bean) {
        return new HotelRoom(
                bean.getId(),
                bean.getManagerId(),
                bean.getName(),
                bean.getCity(),
                bean.getRoomType(),
                bean.getServices(),
                bean.getDistanceFromCenter(),
                bean.getPricePerNight(),
                bean.getImageFileName(),
                bean.getCapacity()
        );
    }
}
