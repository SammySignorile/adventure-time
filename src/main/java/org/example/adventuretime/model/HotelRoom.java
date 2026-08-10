package org.example.adventuretime.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entità persistente che rappresenta una camera/struttura prenotabile.
 */
@SuppressWarnings("java:S107")
public class HotelRoom implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long id;
    private long managerId;
    private String name;
    private String city;
    private String roomType;
    private String services;
    private String distanceFromCenter;
    private BigDecimal pricePerNight;
    private String imageFileName;
    private int capacity;

    public HotelRoom() {
        // Costruttore vuoto utile per il mapping.
    }

    public HotelRoom(long id, long managerId, String name, String city,
                     String roomType, String services, String distanceFromCenter,
                     BigDecimal pricePerNight, String imageFileName, int capacity) {
        this.id = id;
        this.managerId = managerId;
        this.name = name;
        this.city = city;
        this.roomType = roomType;
        this.services = services;
        this.distanceFromCenter = distanceFromCenter;
        this.pricePerNight = pricePerNight;
        this.imageFileName = imageFileName;
        this.capacity = capacity;
    }

    public HotelRoom(HotelRoom other) {
        this(other.id, other.managerId, other.name, other.city,
                other.roomType, other.services, other.distanceFromCenter,
                other.pricePerNight, other.imageFileName, other.capacity);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getManagerId() {
        return managerId;
    }

    public void setManagerId(long managerId) {
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getDistanceFromCenter() {
        return distanceFromCenter;
    }

    public void setDistanceFromCenter(String distanceFromCenter) {
        this.distanceFromCenter = distanceFromCenter;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof HotelRoom room)) {
            return false;
        }
        return id == room.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
