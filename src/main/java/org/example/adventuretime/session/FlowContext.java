package org.example.adventuretime.session;

import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.bean.SearchCriteriaBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Short-lived buffer for a multi-screen flow.
 *
 * Search results are read from persistence only once, transformed into Beans,
 * and kept here while the user moves from search -> list -> checkout.
 * Nothing is written to persistence until the booking is confirmed.
 */
public final class FlowContext {

    private SearchCriteriaBean lastCriteria;
    private List<HotelBean> searchResults = List.of();
    private HotelBean selectedHotel;

    public synchronized void setSearch(
            SearchCriteriaBean criteria,
            List<HotelBean> results
    ) {
        lastCriteria = new SearchCriteriaBean(criteria);
        searchResults = results.stream().map(HotelBean::new).toList();
        selectedHotel = null;
    }

    public synchronized Optional<SearchCriteriaBean> getLastCriteria() {
        return lastCriteria == null
                ? Optional.empty()
                : Optional.of(new SearchCriteriaBean(lastCriteria));
    }

    public synchronized List<HotelBean> getSearchResults() {
        List<HotelBean> copy = new ArrayList<>();
        for (HotelBean bean : searchResults) {
            copy.add(new HotelBean(bean));
        }
        return copy;
    }

    public synchronized void selectHotel(HotelBean hotel) {
        selectedHotel = new HotelBean(hotel);
    }

    public synchronized Optional<HotelBean> getSelectedHotel() {
        return selectedHotel == null
                ? Optional.empty()
                : Optional.of(new HotelBean(selectedHotel));
    }

    public synchronized void clearBookingFlow() {
        lastCriteria = null;
        searchResults = List.of();
        selectedHotel = null;
    }
}
