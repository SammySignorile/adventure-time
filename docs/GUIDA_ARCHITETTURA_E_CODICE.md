# Guida approfondita all'architettura e al codice

## 1. Vista generale del flusso

Adventure Time usa una variante didattica di **BCE + MVC Pull**.

### Flusso normale

1. La **View** raccoglie l'input.
2. Il **Graphic Controller** traduce l'input in un Bean.
3. Il **Graphic Controller** invoca il controller applicativo del caso d'uso.
4. L'**Application Controller** applica autorizzazioni e regole del flusso.
5. Il controller applicativo richiede i dati ai **DAO**: è il “pull”.
6. I DAO restituiscono entità del **Model**.
7. I **Mapper** trasformano le entità in Beans sicuri per la View.
8. Il Graphic Controller aggiorna la View o cambia scena.

La View non conosce SQL, file, entità persistenti o connessioni.

---

## 2. Avvio e configurazione

### `Main`

È l'entry point Java. Non decide da solo quale interfaccia o database usare. Esegue soltanto:

1. `AppBootstrap.initialize()`;
2. `InterfaceFactory.create(config.uiMode())`;
3. `start(args)` sull'interfaccia selezionata.

### `ConfigLoader`

Legge un file `.properties` dal classpath. Il file può essere cambiato senza ricompilare usando:

```text
-Dadventure.config=/application-full-db.properties
```

Valida anche una regola importante del capitolato:

- `DEMO` deve usare `IN_MEMORY`;
- `FULL` deve usare `DB` oppure `FILESYSTEM`.

Una configurazione incoerente genera `ConfigurationException` prima di creare UI o DAO.

### `AppBootstrap`

È l'oggetto di startup richiesto. In base a `persistence.mode` crea:

- `InMemoryDAOFactory`;
- `FileSystemDAOFactory`;
- `JdbcDAOFactory`.

Poi inizializza `AppContext`.

### `AppContext`

È il **composition root**: il punto in cui vengono collegate le dipendenze. Conserva:

- configurazione;
- factory DAO;
- sessione utente;
- buffer del flusso;
- router JavaFX.

I controller FXML devono avere costruttore vuoto. Perciò recuperano da `AppContext` il controller applicativo già costruito con i DAO corretti. È preferibile a creare DAO dentro i controller grafici.

---

## 3. Due interfacce, un controller applicativo

Per ogni caso d'uso esistono boundary diverse:

| Caso d'uso | JavaFX | CLI | Controller applicativo comune |
|---|---|---|---|
| Login | `LoginGraphicController` | `LoginCLIGraphicController` | `LoginApplicationController` |
| Gestire una prenotazione / Prenotare | `SearchHotelGraphicController`, `CheckoutGraphicController` | `SearchHotelCLIGraphicController`, `BookingCLIGraphicController` | `ManageBookingsApplicationController` |
| Gestire prenotazioni ricevute | `ManageHotelsGraphicController` | `ManageHotelCLIGraphicController` | `ManageHotelsApplicationController` |

Il profilo e la gestione del catalogo sono viste di supporto che riusano questi
controller: non esistono controller applicativi inventati soltanto per una
schermata.

“Due interfacce” non significa duplicare la business logic. Significa duplicare soltanto il codice di presentazione necessario a leggere input e mostrare output.

---

## 4. Beans, Model e Mapper

### Model

Le classi `User`, `HotelRoom` e `Booking` rappresentano entità persistenti. Contengono anche dati che la View non deve ricevere direttamente, per esempio la password in `User`.

### Beans

I Bean sono oggetti di trasferimento tra presentation e application layer:

- `CredentialsBean`: input del login;
- `SearchCriteriaBean`: input della ricerca;
- `HotelBean`: hotel mostrabile;
- `BookingRequestBean`: scelta del checkout;
- `BookingQuoteBean`: preventivo calcolato;
- `BookingBean`: prenotazione visualizzabile;
- `UserBean`: utente privo di password.

### Mapper

`UserMapper` e `HotelMapper` trasformano Model ↔ Bean. Questo riduce l'accoppiamento e impedisce alla View di modificare direttamente le entità caricate dal DB.

---

## 5. Sessione e privilegi

### `UserSession`

Dopo il login conserva una copia di `UserBean`. Espone controlli semplici:

- `isAuthenticated()`;
- `isTraveler()`;
- `isVendor()`.

Il ruolo MySQL resta `CLIENTE` o `GESTORE` per compatibilità; nell'interfaccia viene presentato come Viaggiatore o Venditore.

La sessione non sostituisce il database. Conserva soltanto l'identità corrente e viene aggiornata quando cambiano i punti.

### Autorizzazioni

I controlli non sono affidati solo ai pulsanti della GUI. Sono ripetuti nei controller applicativi:

- ricerca e prenotazione richiedono un viaggiatore;
- gestione hotel richiede un venditore.

Quindi un client alternativo non può aggirare i privilegi semplicemente chiamando un metodo.

---

## 6. Buffer temporaneo del flusso

### `FlowContext`

Serve esclusivamente durante un flusso multi-schermata:

1. la ricerca salva criteri e risultati;
2. la lista legge i risultati senza interrogare nuovamente il DB;
3. la card salva l'hotel selezionato;
4. il checkout legge criteri e hotel;
5. dopo la conferma o il logout il buffer viene svuotato.

Le copie difensive (`new HotelBean(...)`) evitano che una schermata modifichi accidentalmente l'oggetto conservato.

Il buffer non è persistenza: se la prenotazione non viene confermata, nessuna scrittura avviene.

---

## 7. DAO e Abstract Factory

### Interfacce DAO

- `UserDAO` gestisce utenti, login e punti;
- `HotelDAO` gestisce catalogo, ricerca e proprietà del venditore;
- `BookingDAO` gestisce prenotazioni e disponibilità.

Gli application controller dipendono da queste interfacce, non dalle classi concrete.

### Factory

`DAOFactory` espone i tre DAO. Le implementazioni producono famiglie coerenti:

- `InMemoryDAOFactory`: tutti i DAO lavorano sullo stesso `InMemoryDataStore`;
- `FileSystemDAOFactory`: tutti lavorano sullo stesso `FileSystemDataStore`;
- `JdbcDAOFactory`: tutti condividono lo stesso `DBConnectionManager`.

In modalita DB viene mantenuta una sola connessione attiva. I DAO chiudono
`PreparedStatement` e `ResultSet` con try-with-resources; `AppContext` chiude la
connessione quando termina l'applicazione.

Non è corretto mescolare, per esempio, `JdbcUserDAO` con `InMemoryBookingDAO` nello stesso avvio: la factory impedisce questa incoerenza.

### In-memory

`DemoData` crea utenti, hotel e prenotazioni fittizie. `InMemoryDataStore.persist()` non esegue nulla: spegnendo l'app i dati si perdono, come richiesto per la demo-version.

### Filesystem

`FileSystemDataStore` serializza un unico `DataState`. Scrive prima un file temporaneo e poi lo sostituisce, riducendo il rischio di lasciare un file parziale. Le entità implementano `Serializable`.

### JDBC

Ogni operazione apre una connessione tramite `DBConnectionManager` e la chiude con try-with-resources. È più robusto, per un progetto didattico, di conservare una `Connection` globale che può scadere.

Le query sono parametrizzate con `PreparedStatement`.

---

## 8. Ricerca hotel

`ManageBookingsApplicationController.search()` esegue:

1. verifica ruolo;
2. validazione del Bean;
3. conversione in `HotelSearchCriteria`;
4. pull dei candidati da `HotelDAO`;
5. verifica delle sovrapposizioni tramite `BookingDAO`;
6. mapping in `HotelBean`;
7. salvataggio nel buffer.

La condizione di sovrapposizione usata è:

```text
prenotazione.checkIn < ricerca.checkOut
AND
prenotazione.checkOut > ricerca.checkIn
```

Due soggiorni che si toccano esattamente al check-out/check-in non confliggono.

---

## 9. Facade e Decorator della prenotazione

### Perché una Facade

Creare una prenotazione richiede più collaboratori. `BookingFacade` offre due operazioni semplici:

- `quote(request)`;
- `createBooking(request)`.

Internamente coordina validazione, hotel, capienza, notti, extra, punti, disponibilità e DAO.

### Decorator

`BaseBookingPrice` contiene il prezzo del soggiorno. Ogni Decorator aggiunge un servizio:

```text
BaseBookingPrice
  → CancellationInsuranceDecorator
  → HealthInsuranceDecorator
  → FlexibleDateDecorator
```

La composizione dipende dalle opzioni selezionate. Non servono classi come:

- `BookingWithHealthInsurance`;
- `BookingWithCancellationAndHealthInsurance`;
- `BookingWithAllInsurances`.

La factory `BookingPriceDecoratorFactory` costruisce la catena.

### Punti

- 1 punto vale 0,01 €;
- lo sconto massimo è il 20% del prezzo lordo;
- dopo l'acquisto si guadagna il 5% del totale in nuovi punti.

Il preventivo non scrive dati. La conferma ricontrolla la disponibilità, salva e aggiorna i punti.

### Limite consapevole

Nel backend JDBC il salvataggio della prenotazione e l'aggiornamento punti sono due operazioni distinte. Per un progetto più evoluto andrebbero racchiuse in una transazione unica mediante un repository/service transazionale. Qui la soluzione resta semplice e facilmente spiegabile; questo limite va dichiarato nella relazione se il professore chiede atomicità.

---

## 10. Navigazione JavaFX

`SceneRouter` possiede l'unico `Stage` principale. Per cambiare schermata:

1. trova l'FXML da `SceneId`;
2. lo carica con `FXMLLoader`;
3. sostituisce la root della `Scene` esistente;
4. applica il CSS una sola volta.

I controller non conoscono percorsi assoluti del computer. Le risorse sono nel classpath. Questo corregge i riferimenti del tipo `../../Downloads/...`, che funzionerebbero solo sul computer dello sviluppatore.

---

## 11. Eccezioni

- `ConfigurationException`: properties mancanti o incoerenti;
- `ValidationException`: input non valido;
- `AuthenticationException`: credenziali errate/sessione non valida;
- `AuthorizationException`: ruolo privo del privilegio;
- `HotelUnavailableException`: concorrenza sulla disponibilità;
- `PersistenceException`: errore DB/file.

I DAO trasformano le eccezioni tecniche in `PersistenceException`. I controller grafici trasformano le eccezioni applicative in Alert o messaggi CLI. L'app non si chiude per un errore previsto.

---

## 12. GRASP applicati

- **Controller**: ogni use case ha un application controller.
- **Information Expert**: il DAO conosce la persistenza; il Decorator conosce il proprio sovrapprezzo; la Facade conosce il coordinamento della prenotazione.
- **Creator**: le factory creano famiglie DAO e interfacce.
- **Low Coupling**: controller applicativi dipendono da interfacce DAO.
- **High Cohesion**: ogni controller gestisce un solo flusso.
- **Indirection**: mapper, facade e factory separano livelli che non devono conoscersi direttamente.
- **Protected Variations**: UI e persistence possono cambiare senza modificare il caso d'uso.
- **Law of Demeter**: GUI e CLI parlano con gli application controller; non
  navigano direttamente dentro `UserSession` o `FlowContext`.

I pattern GoF sono usati soltanto dove risolvono un problema concreto:
`DAOFactory` crea una famiglia coerente di DAO, `AppContext` offre un unico
contesto applicativo e i Decorator aggiungono gli extra al prezzo mantenendo
la stessa interfaccia `BookingPriceComponent`.

---

## 13. Come presentarlo all'esame

Una dimostrazione efficace dura pochi minuti:

1. mostrare `application.properties`;
2. avviare in DEMO/GUI;
3. login viaggiatore;
4. ricerca e preventivo con un extra;
5. conferma e profilo;
6. logout e login venditore;
7. mostrare e annullare la prenotazione ricevuta;
8. cambiare configurazione in CLI o filesystem;
9. mostrare `DAOFactory`, un controller applicativo e il Decorator;
10. eseguire i test e mostrare SonarCloud.
