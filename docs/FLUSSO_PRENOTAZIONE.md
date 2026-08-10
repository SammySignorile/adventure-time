# Flusso eseguito quando viene prenotata una camera

## Preventivo

```text
CheckoutGraphicController.initialize()
→ refreshQuote()
→ buildRequest()
→ AppContext.manageBookingsController()
→ ManageBookingsApplicationController.getQuote()
→ BookingFacade.quote()
→ BookingRequestBean.validateSyntax()
→ UserSession.isTraveler()
→ HotelDAO.findById()
→ BookingPriceDecoratorFactory.decorate()
→ BookingQuoteBean
→ aggiornamento Label JavaFX
```

Non viene scritto nulla nella persistenza.

## Conferma

```text
CheckoutGraphicController.onConfirm()
→ validCardFields()
→ buildRequest()
→ AppContext.manageBookingsController()
→ ManageBookingsApplicationController.book()
→ BookingFacade.createBooking()
    → quote()
    → UserSession.requireUser()
    → BookingDAO.isHotelAvailable()
    → new Booking(...)
    → BookingDAO.save()
    → UserDAO.updatePoints()
    → UserSession.updatePoints()
→ FlowContext.clearBookingFlow()
→ AlertHelper.info()
→ SceneRouter.show(PROFILE)
```

In modalità DB, le due chiamate di persistenza diventano:

```text
BookingDAO.save()      → JdbcBookingDAO.save()      → INSERT INTO bookings
UserDAO.updatePoints() → JdbcUserDAO.updatePoints() → UPDATE users
```
