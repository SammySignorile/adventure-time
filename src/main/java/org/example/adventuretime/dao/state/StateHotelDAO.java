package org.example.adventuretime.dao.state;

import org.example.adventuretime.dao.HotelDAO;
import org.example.adventuretime.exception.PersistenceException;
import org.example.adventuretime.model.HotelRoom;
import org.example.adventuretime.model.HotelSearchCriteria;

import java.util.List;
import java.util.Locale;
import java.util.Optional;


public class StateHotelDAO implements HotelDAO {

    private final DataStore store;

    public StateHotelDAO(DataStore store) {
        this.store = store;
    }

    @Override
    public synchronized Optional<HotelRoom> findById(long id) {
        return store.getState().getHotels().stream()
                .filter(room -> room.getId() == id)
                .findFirst()
                .map(HotelRoom::new);
    }

    @Override
    public synchronized List<HotelRoom> findByCriteria(
            HotelSearchCriteria criteria
    ) {
        String expectedCity = criteria.city().trim().toLowerCase(Locale.ROOT);
        return store.getState().getHotels().stream()
                .filter(room -> room.getCity().toLowerCase(Locale.ROOT)
                        .contains(expectedCity))
                .filter(room -> room.getPricePerNight()
                        .compareTo(criteria.maximumPricePerNight()) <= 0)
                .filter(room -> room.getCapacity() >= criteria.people())
                .map(HotelRoom::new)
                .toList();
    }

    @Override
    public synchronized List<HotelRoom> findByManagerId(long managerId) {
        return store.getState().getHotels().stream()
                .filter(room -> room.getManagerId() == managerId)
                .map(HotelRoom::new)
                .toList();
    }

    /**
     * Piccola galleria usata in modalità DEMO e FILESYSTEM.
     * I nomi corrispondono ai file presenti in src/main/resources/images.
     */
    @Override
    public synchronized List<String> findImageNamesByHotelId(long hotelId)
            throws PersistenceException {

        return switch ((int) hotelId) {
            case 1 -> List.of(
                    "roma 1.jpg",
                    "roma centro.jpg",
                    "roma 3.jpg",
                    "colazione 1.jpg",
                    "stanza roma 1.jpg"
            );
            case 2 -> List.of(
                    "termini.jpg",
                    "hotel roma.jpg",
                    "roma 2.jpg",
                    "stanza 2.jpg",
                    "colazione 2.jpg"
            );
            case 3 -> List.of(
                    "colosseo 3.jpg",
                    "roma 3.jpg",
                    "stanza roma 1.jpg",
                    "colazione 3.jpg"
            );
            default -> HotelDAO.super.findImageNamesByHotelId(hotelId);
        };
    }

    @Override
    public synchronized HotelRoom save(HotelRoom hotel)
            throws PersistenceException {
        DataState state = store.getState();
        if (hotel.getId() == 0) {
            HotelRoom created = new HotelRoom(hotel);
            created.setId(state.nextHotelId());
            state.getHotels().add(created);
            store.persist();
            return new HotelRoom(created);
        }

        HotelRoom existing = state.getHotels().stream()
                .filter(candidate -> candidate.getId() == hotel.getId())
                .filter(candidate -> candidate.getManagerId()
                        == hotel.getManagerId())
                .findFirst()
                .orElseThrow(() -> new PersistenceException(
                        "Struttura non trovata o non appartenente al venditore."));

        copyValues(hotel, existing);
        store.persist();
        return new HotelRoom(existing);
    }

    @Override
    public synchronized void delete(long id, long managerId)
            throws PersistenceException {
        boolean hasBookings = store.getState().getBookings().stream()
                .anyMatch(booking -> booking.getHotelId() == id);

        if (hasBookings) {
            throw new PersistenceException(
                    "Non è possibile eliminare una struttura con prenotazioni.");
        }

        boolean removed = store.getState().getHotels().removeIf(
                room -> room.getId() == id
                        && room.getManagerId() == managerId);

        if (!removed) {
            throw new PersistenceException(
                    "Struttura non trovata o non appartenente al venditore.");
        }
        store.persist();
    }

    private static void copyValues(
            HotelRoom source,
            HotelRoom destination
    ) {
        destination.setManagerId(source.getManagerId());
        destination.setName(source.getName());
        destination.setCity(source.getCity());
        destination.setRoomType(source.getRoomType());
        destination.setServices(source.getServices());
        destination.setDistanceFromCenter(source.getDistanceFromCenter());
        destination.setPricePerNight(source.getPricePerNight());
        destination.setImageFileName(source.getImageFileName());
        destination.setCapacity(source.getCapacity());
    }
}
