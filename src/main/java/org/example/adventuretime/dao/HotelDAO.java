package org.example.adventuretime.dao;

import org.example.adventuretime.exception.PersistenceException;
import org.example.adventuretime.model.HotelRoom;
import org.example.adventuretime.model.HotelSearchCriteria;

import java.util.List;
import java.util.Optional;

public interface HotelDAO {

    Optional<HotelRoom> findById(long id) throws PersistenceException;

    List<HotelRoom> findByCriteria(HotelSearchCriteria criteria)
            throws PersistenceException;

    List<HotelRoom> findByManagerId(long managerId)
            throws PersistenceException;

    HotelRoom save(HotelRoom hotel) throws PersistenceException;

    void delete(long id, long managerId) throws PersistenceException;

    /**
     * Restituisce i nomi delle immagini associate all'hotel.
     *
     * Le implementazioni senza una tabella dedicata (memoria e filesystem)
     * usano come ripiego la sola immagine principale dell'hotel.
     */
    default List<String> findImageNamesByHotelId(long hotelId)
            throws PersistenceException {

        return findById(hotelId)
                .map(HotelRoom::getImageFileName)
                .filter(name -> !name.isBlank())
                .map(List::of)
                .orElseGet(List::of);
    }
}
