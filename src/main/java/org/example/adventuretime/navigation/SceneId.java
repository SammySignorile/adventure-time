package org.example.adventuretime.navigation;

public enum SceneId {
    LOGIN("/org/example/adventuretime/view/login.fxml"),
    TRAVELER_HOME("/org/example/adventuretime/view/traveler-home.fxml"),
    VENDOR_HOME("/org/example/adventuretime/view/vendor-home.fxml"),
    SEARCH_HOTEL("/org/example/adventuretime/view/search-hotel.fxml"),
    HOTEL_LIST("/org/example/adventuretime/view/hotel-list.fxml"),
    HOTEL_DETAILS("/org/example/adventuretime/view/hotel-details.fxml"),
    CHECKOUT("/org/example/adventuretime/view/checkout.fxml"),
    PROFILE("/org/example/adventuretime/view/profile.fxml"),
    MANAGE_HOTELS("/org/example/adventuretime/view/manage-hotels.fxml");

    private final String resourcePath;

    SceneId(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getResourcePath() {
        return resourcePath;
    }
}
