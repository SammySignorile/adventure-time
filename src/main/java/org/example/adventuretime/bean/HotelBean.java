package org.example.adventuretime.bean;

import org.example.adventuretime.exception.ValidationException;

import java.math.BigDecimal;

/**
 * Bean usato per mostrare e inserire i dati di una struttura.
 * imageFileName contiene soltanto il nome del file, non il percorso completo.
 */
@SuppressWarnings("java:S107")
public class HotelBean {

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

    public HotelBean() {
        // Costruttore vuoto utile ai form JavaFX e CLI.
    }

    public HotelBean(
            long id,
            long managerId,
            String name,
            String city,
            String roomType,
            String services,
            String distanceFromCenter,
            BigDecimal pricePerNight,
            String imageFileName,
            int capacity
    ) {
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

    public HotelBean(HotelBean other) {
        if (other == null) {
            throw new IllegalArgumentException(
                    "L'hotel da copiare non può essere nullo."
            );
        }
        this.id = other.id;
        this.managerId = other.managerId;
        this.name = other.name;
        this.city = other.city;
        this.roomType = other.roomType;
        this.services = other.services;
        this.distanceFromCenter = other.distanceFromCenter;
        this.pricePerNight = other.pricePerNight;
        this.imageFileName = other.imageFileName;
        this.capacity = other.capacity;
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

    public void validateSyntax() throws ValidationException {
        if (id < 0) {
            throw new ValidationException("Identificativo hotel non valido.");
        }

        if (name == null || name.isBlank()) {
            throw new ValidationException(
                    "Il nome dell'hotel è obbligatorio."
            );
        }

        if (city == null || city.isBlank()) {
            throw new ValidationException("La città è obbligatoria.");
        }

        if (roomType == null || roomType.isBlank()) {
            throw new ValidationException(
                    "Il tipo di camera è obbligatorio."
            );
        }

        if (pricePerNight == null
                || pricePerNight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(
                    "Il prezzo per notte deve essere maggiore di zero."
            );
        }

        if (capacity <= 0) {
            throw new ValidationException(
                    "La capienza deve essere maggiore di zero."
            );
        }
    }

    @Override
    public String toString() {
        return safe(name) + " - " + safe(city) + " - " + safe(roomType)
                + " - €" + (pricePerNight == null
                ? "0.00" : pricePerNight) + "/notte";
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
