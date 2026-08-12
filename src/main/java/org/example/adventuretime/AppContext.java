package org.example.adventuretime;

import org.example.adventuretime.application_controller.LoginApplicationController;
import org.example.adventuretime.application_controller.ManageBookingsApplicationController;
import org.example.adventuretime.application_controller.ManageHotelsApplicationController;
import org.example.adventuretime.configuration.AppConfig;
import org.example.adventuretime.dao.BookingDAO;
import org.example.adventuretime.dao.DAOFactory;
import org.example.adventuretime.dao.HotelDAO;
import org.example.adventuretime.dao.UserDAO;
import org.example.adventuretime.facade.BookingFacade;
import org.example.adventuretime.exception.PersistenceException;
import org.example.adventuretime.navigation.SceneRouter;
import org.example.adventuretime.session.FlowContext;
import org.example.adventuretime.session.UserSession;

/**
 * Composition root dell'applicazione.
 * Crea una sola istanza dei tre controller applicativi condivisi da GUI e CLI.
 */
public final class AppContext {

    private static AppContext instance;

    private final AppConfig config;
    private final UserSession userSession;
    private final FlowContext flowContext;
    private final DAOFactory daoFactory;

    private final LoginApplicationController loginController;
    private final ManageBookingsApplicationController manageBookingsController;
    private final ManageHotelsApplicationController manageHotelsController;

    private SceneRouter sceneRouter;

    private AppContext(AppConfig config, DAOFactory daoFactory) {
        this.config = config;
        this.daoFactory = daoFactory;
        this.userSession = new UserSession();
        this.flowContext = new FlowContext();

        UserDAO userDAO = daoFactory.getUserDAO();
        HotelDAO hotelDAO = daoFactory.getHotelDAO();
        BookingDAO bookingDAO = daoFactory.getBookingDAO();

        BookingFacade bookingFacade = new BookingFacade(
                hotelDAO,
                bookingDAO,
                userDAO,
                userSession
        );

        this.loginController = new LoginApplicationController(
                userDAO,
                userSession,
                flowContext
        );

        this.manageBookingsController =
                new ManageBookingsApplicationController(
                        hotelDAO,
                        bookingDAO,
                        userSession,
                        flowContext,
                        bookingFacade
                );

        this.manageHotelsController =
                new ManageHotelsApplicationController(
                        hotelDAO,
                        bookingDAO,
                        userSession,
                        bookingFacade
                );
    }

    public static synchronized void initialize(
            AppConfig config,
            DAOFactory daoFactory
    ) {
        if (instance != null) {
            throw new IllegalStateException(
                    "AppContext è già stato inizializzato.");
        }
        instance = new AppContext(config, daoFactory);
    }

    public static synchronized AppContext getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                    "AppContext non ancora inizializzato.");
        }
        return instance;
    }

    public static synchronized void resetForTests() {
        instance = null;
    }

    public static synchronized void shutdown() throws PersistenceException {
        if (instance != null) {
            instance.daoFactory.close();
            instance = null;
        }
    }

    public AppConfig getConfig() {
        return config;
    }

    public LoginApplicationController loginController() {
        return loginController;
    }

    public ManageBookingsApplicationController manageBookingsController() {
        return manageBookingsController;
    }

    public ManageHotelsApplicationController manageHotelsController() {
        return manageHotelsController;
    }

    public synchronized void setSceneRouter(SceneRouter sceneRouter) {
        this.sceneRouter = sceneRouter;
    }

    public synchronized SceneRouter getSceneRouter() {
        if (sceneRouter == null) {
            throw new IllegalStateException(
                    "SceneRouter disponibile soltanto in modalità GUI.");
        }
        return sceneRouter;
    }
}
