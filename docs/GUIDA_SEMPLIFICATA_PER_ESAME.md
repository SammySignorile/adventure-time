# Adventure Time — guida semplificata per capire e spiegare il progetto

Questa guida descrive il progetto con parole semplici e indica anche una frase pronta da usare all'esame.

---

# 1. Che cosa stai vedendo nella schermata di IntelliJ

## 1.1 Cartella `.idea`

La cartella `.idea` viene creata automaticamente da IntelliJ IDEA quando apri il progetto.

Non contiene la logica di Adventure Time. Contiene soltanto le impostazioni dell'IDE sul tuo computer.

### `.idea/.gitignore`

Dice a Git quali file interni di IntelliJ non devono essere caricati nel repository.

### `.idea/compiler.xml`

Contiene alcune impostazioni usate da IntelliJ per compilare il progetto, per esempio la versione di Java.

Nel nostro progetto la versione principale è comunque dichiarata nel `pom.xml`, quindi Maven rimane il riferimento più importante.

### `.idea/encodings.xml`

Dice a IntelliJ quale codifica usare per leggere i file di testo. Di solito è UTF-8.

Serve affinché caratteri come `à`, `è`, `€` e `•` siano letti correttamente.

### `.idea/jarRepositories.xml`

Contiene l'elenco dei repository da cui IntelliJ può cercare le librerie Maven.

Per esempio, JavaFX, JUnit e MySQL Connector vengono normalmente scaricati da Maven Central.

### `.idea/misc.xml`

Contiene impostazioni generali del progetto IntelliJ, come il JDK associato.

### `.idea/workspace.xml`

Contiene impostazioni personali della tua sessione di lavoro:

- file aperti;
- pannelli aperti;
- configurazioni di esecuzione locali;
- posizione del cursore;
- cronologia recente dell'IDE.

È un file personale e normalmente **non va condiviso su Git**.

### Come dirlo all'esame

> La cartella `.idea` non fa parte dell'architettura applicativa. È generata da IntelliJ e conserva solo configurazioni locali dell'ambiente di sviluppo. La configurazione riproducibile del progetto è invece nel `pom.xml` e nei file properties.

---

## 1.2 Cartella `database`

Contiene i file relativi a MySQL.

### `adventuretime.sql`

È lo script che:

1. elimina un eventuale vecchio database;
2. crea `adventuretimedb`;
3. crea le tabelle;
4. inserisce dati di prova.

Lo script è stato semplificato. I DAO JDBC usano direttamente query SQL tramite `PreparedStatement`, quindi non sono necessarie stored procedure per far funzionare l'app.

### `COME_USARE_LE_FOTO.md`

Spiega dove mettere le immagini e come il nome salvato nel database viene trasformato in un percorso reale sul computer.

### Come dirlo all'esame

> La cartella `database` contiene lo schema MySQL e i dati iniziali. Il database rappresenta una delle implementazioni della persistenza. I controller non eseguono SQL direttamente: usano le interfacce DAO.

---

## 1.3 Cartella `docs`

Contiene la documentazione, non il codice eseguito dall'applicazione.

### `ANALISI_PROGETTI_RIFERIMENTO.md`

Riassume le idee studiate negli altri progetti e specifica quali scelte sono state adattate ad Adventure Time.

### `BOZZA_RELAZIONE.md`

È una base per scrivere la relazione finale richiesta dal corso.

### `CHECKLIST_ESAME.md`

È una lista di controlli da fare prima della consegna e dell'esame.

### `GUIDA_ARCHITETTURA_E_CODICE.md`

È la guida tecnica più dettagliata.

### `GUIDA_SEMPLIFICATA_PER_ESAME.md`

È questo documento. Serve per capire il progetto e preparare una spiegazione orale.

### `SQL_CORRECTIONS.md`

Descriveva gli errori dello script iniziale e le correzioni effettuate. Puoi conservarlo come traccia del lavoro, ma non è necessario mostrarlo durante la dimostrazione principale.

### Cartella `docs/uml`

Contiene file PlantUML (`.puml`). Sono file testuali da cui si possono generare diagrammi UML:

- use case;
- VOPC;
- design-level class diagram;
- activity diagram;
- sequence diagram;
- state diagram.

### Come dirlo all'esame

> La cartella `docs` contiene gli artefatti di analisi e progettazione. Il codice e i diagrammi devono essere coerenti: per esempio il sequence diagram della prenotazione deve mostrare gli stessi controller e DAO realmente presenti nel codice.

---

## 1.4 `pom.xml`

È il file principale di Maven.

Descrive:

- nome del progetto;
- versione di Java;
- dipendenze;
- plugin di compilazione e test;
- classe principale da avviare.

Le dipendenze più importanti sono:

- `javafx-controls`: controlli grafici JavaFX;
- `javafx-fxml`: caricamento dei file FXML;
- `mysql-connector-j`: collegamento a MySQL;
- `junit-jupiter`: test automatici.

### Come dirlo all'esame

> Il `pom.xml` rende il progetto riproducibile. Chi clona il repository non deve aggiungere manualmente i JAR, perché Maven risolve le dipendenze dichiarate.

---

## 1.5 Cartella `src`

È la cartella più importante perché contiene codice, viste e test.

```text
src
├── main
│   ├── java
│   └── resources
└── test
    └── java
```

### `src/main/java`

Contiene le classi Java dell'applicazione.

### `src/main/resources`

Contiene risorse non Java caricate durante l'esecuzione:

- file `.properties`;
- FXML;
- CSS.

### `src/test/java`

Contiene i test JUnit.

---

# 2. Struttura dei package Java

Il package radice è:

```text
org.example.adventuretime
```

## 2.1 `Main`

È il punto di partenza del programma.

Il suo compito è volutamente piccolo:

1. inizializzare l'app;
2. leggere quale interfaccia usare;
3. avviare GUI oppure CLI.

### Frase semplice

> `Main` accende l'applicazione, ma non contiene regole di business.

---

## 2.2 `configuration`

Contiene:

- `AppConfig`;
- `ConfigLoader`;
- gli enum `UiMode`, `AppMode`, `PersistenceMode`.

### `ConfigLoader`

Legge le coppie chiave-valore del file properties.

Esempio:

```properties
ui.mode=GUI
persistence.mode=DB
```

Poi crea un oggetto `AppConfig` con valori già convertiti nel tipo corretto.

### `AppConfig`

È un contenitore immutabile della configurazione.

Ora contiene anche:

```java
Path hotelImagesPath
```

che indica la cartella delle foto.

### Perché leggere prima la configurazione

Perché tutti gli oggetti creati dopo devono essere coerenti:

- se scegli `DB`, devono essere creati i DAO JDBC;
- se scegli `FILESYSTEM`, devono essere creati i DAO filesystem;
- se scegli `IN_MEMORY`, devono essere creati i DAO in memoria;
- se scegli `GUI`, si avvia JavaFX;
- se scegli `CLI`, si avvia il terminale.

### Frase semplice

> La configurazione viene letta all'inizio, così il resto del codice non deve chiedersi continuamente quale modalità è attiva.

---

## 2.3 `AppBootstrap`

È l'oggetto che costruisce l'applicazione.

Legge `AppConfig` e sceglie una sola famiglia di DAO:

```text
IN_MEMORY  → InMemoryDAOFactory
FILESYSTEM → FileSystemDAOFactory
DB         → JdbcDAOFactory
```

Questa è l'applicazione dell'**Abstract Factory**.

### Frase semplice

> `AppBootstrap` è il montatore iniziale: legge la configurazione e collega l'app alla persistenza scelta.

---

## 2.4 `AppContext`

Conserva gli oggetti condivisi dell'app:

- configurazione;
- `DAOFactory`;
- `UserSession`;
- `FlowContext`;
- `SceneRouter`.

Inoltre costruisce i controller applicativi fornendo loro i DAO corretti.

È utile con JavaFX perché i controller FXML vengono creati automaticamente da `FXMLLoader` usando un costruttore vuoto.

### Frase semplice

> `AppContext` è il punto centrale da cui i controller grafici recuperano la logica applicativa già configurata.

---

## 2.5 `model`

Contiene le entità principali:

- `User`;
- `HotelRoom`;
- `Booking`.

Le entità rappresentano i dati reali del dominio e possono essere salvate nei vari sistemi di persistenza.

Esempio: `HotelRoom` contiene nome, città, prezzo, capienza e nome del file immagine.

### Frase semplice

> Il model rappresenta i concetti reali dell'applicazione, indipendentemente dall'interfaccia grafica.

---

## 2.6 `bean`

Contiene oggetti usati per scambiare dati tra interfaccia e logica.

Esempi:

- `CredentialsBean`: email e password;
- `SearchCriteriaBean`: città, date, persone e prezzo massimo;
- `HotelBean`: hotel mostrato all'utente;
- `BookingRequestBean`: dati necessari per chiedere una prenotazione;
- `BookingQuoteBean`: preventivo;
- `BookingBean`: prenotazione da visualizzare;
- `UserBean`: dati pubblici dell'utente senza password.

### Perché non passare sempre le entità

Per evitare che la GUI:

- veda dati che non le servono;
- modifichi direttamente gli oggetti persistenti;
- diventi dipendente dalla struttura interna del database.

### Frase semplice

> I Bean sono pacchetti di dati preparati per un determinato flusso dell'interfaccia.

---

## 2.7 `mapper`

Contiene classi che convertono:

```text
Entity → Bean
Bean   → Entity
```

Per esempio `HotelMapper` converte `HotelRoom` in `HotelBean`.

### Frase semplice

> Il Mapper separa il formato interno del model dal formato usato dalla presentazione.

---

## 2.8 `dao`

DAO significa **Data Access Object**.

Un DAO nasconde il modo concreto in cui i dati vengono letti o salvati.

Le interfacce principali sono:

- `UserDAO`;
- `HotelDAO`;
- `BookingDAO`.

Il controller applicativo conosce solo queste interfacce.

### `dao/db`

Contiene le implementazioni MySQL:

- `JdbcUserDAO`;
- `JdbcHotelDAO`;
- `JdbcBookingDAO`;
- `DBConnectionManager`;
- `JdbcDAOFactory`.

### `dao/filesystem`

Contiene i DAO che salvano su un file locale.

### `dao/memory`

Contiene i DAO della modalità demo. I dati scompaiono quando chiudi l'app.

### `dao/state`

Contiene la parte comune usata da memoria e filesystem:

- `DataState`: insieme dei dati;
- `DataStore`: modo di conservare lo stato;
- `StateUserDAO`, `StateHotelDAO`, `StateBookingDAO`: operazioni comuni;
- `DemoData`: dati iniziali fittizi.

### Frase semplice

> I DAO isolano la persistenza. La business logic chiama lo stesso metodo indipendentemente dal fatto che i dati siano in MySQL, in un file oppure in memoria.

---

## 2.9 `application_controller`

Contiene un controller per ciascun flusso applicativo:

- login;
- ricerca hotel;
- prenotazione;
- profilo;
- gestione hotel.

Questi controller non contengono pulsanti o componenti JavaFX.

Sono condivisi tra GUI e CLI.

### Frase semplice

> Il controller applicativo rappresenta il caso d'uso e può essere richiamato da entrambe le interfacce.

---

## 2.10 `ui/gui/controller`

Contiene i controller delle schermate JavaFX.

Il loro compito è:

1. leggere i campi FXML;
2. costruire un Bean;
3. chiamare il controller applicativo;
4. mostrare il risultato;
5. chiedere il cambio di schermata.

Non devono contenere SQL.

### Esempio

`CheckoutGraphicController` legge checkbox, dati della carta e hotel selezionato, poi invoca `ManageBookingsApplicationController`.

---

## 2.11 `ui/cli`

Contiene la seconda interfaccia, basata su terminale.

La CLI usa gli stessi controller applicativi della GUI.

Questo rispetta il requisito di fornire la stessa funzionalità attraverso due interfacce senza duplicare la logica.

---

## 2.12 `navigation`

### `SceneId`

È l'elenco delle schermate disponibili e dei relativi percorsi FXML.

### `SceneRouter`

Cambia il contenuto dello stesso `Stage` JavaFX.

Quindi non crea una nuova finestra per ogni schermata.

### Frase semplice

> Il router centralizza la navigazione, così i controller non devono conoscere tutti i percorsi FXML e non creano finestre autonomamente.

---

## 2.13 `session`

### `UserSession`

Conserva l'utente autenticato.

Permette di sapere:

- chi è l'utente;
- quanti punti possiede;
- se è viaggiatore;
- se è venditore.

### `FlowContext`

Conserva dati temporanei del flusso:

- ultima ricerca;
- risultati trovati;
- hotel selezionato.

Non è un database.

### Differenza fondamentale

```text
UserSession = chi sta usando l'app
FlowContext = cosa sta facendo in questo momento
```

---

## 2.14 `facade`

`BookingFacade` coordina tutte le operazioni necessarie a creare una prenotazione.

Senza Facade, `ManageBookingsApplicationController` dovrebbe conoscere:

- validazione;
- hotel DAO;
- booking DAO;
- user DAO;
- calcolo notti;
- punti;
- Decorator;
- disponibilità.

La Facade offre invece due operazioni semplici:

```java
quote(request)
createBooking(request)
```

### Frase semplice

> Ho usato una Facade perché la prenotazione coinvolge più sottosistemi. In questo modo il controller applicativo rimane piccolo e il flusso è più leggibile.

---

## 2.15 `pattern/booking`

Contiene il pattern **Decorator** per il prezzo.

Il prezzo parte da:

```text
BaseBookingPrice
```

Poi ogni servizio selezionato aggiunge il proprio costo:

```text
CancellationInsuranceDecorator
HealthInsuranceDecorator
FlexibleDateDecorator
```

### Frase semplice

> Il Decorator mi permette di combinare liberamente gli extra senza creare una classe diversa per ogni combinazione possibile.

---

## 2.16 `exception`

Contiene eccezioni specifiche del progetto:

- autenticazione fallita;
- autorizzazione negata;
- configurazione errata;
- persistenza fallita;
- dati non validi;
- hotel non più disponibile.

Le eccezioni vengono tradotte in messaggi comprensibili dalla GUI o dalla CLI.

---

# 3. MVC Pull spiegato in modo semplice

Nel progetto la View non riceve automaticamente notifiche dal database.

È il controller che **chiede** i dati quando servono.

Esempio della lista hotel:

```text
HotelListGraphicController
        ↓ chiede
ManageBookingsApplicationController
        ↓ legge il buffer
FlowContext
        ↓ restituisce
List<HotelBean>
```

Si chiama Pull perché la presentazione tira i dati quando deve mostrarli.

### Frase pronta

> Uso MVC Pull perché la View rimane passiva. Quando deve aggiornarsi, il controller richiede i dati alla logica applicativa e poi li inserisce nei componenti JavaFX.

---

# 4. Principi GRASP applicati

## Controller

Ogni caso d'uso ha un application controller dedicato.

## Low Coupling

I controller applicativi dipendono dalle interfacce DAO e non da MySQL direttamente.

## High Cohesion

Ogni classe ha una responsabilità principale:

- `ConfigLoader` legge configurazione;
- `SceneRouter` cambia scena;
- `JdbcHotelDAO` accede agli hotel nel DB;
- `BookingFacade` coordina la prenotazione.

## Information Expert

Il calcolo del prezzo degli extra è affidato agli oggetti del sottosistema pricing, che conoscono i costi.

## Indirection

I DAO fanno da intermediari tra business logic e database.

## Pure Fabrication

`BookingFacade`, i Mapper e i DAO non rappresentano oggetti del mondo reale, ma sono creati per mantenere il sistema più ordinato.

---

# 5. Come funzionano le immagini

## 5.1 Cosa viene salvato nel database

Solo il nome del file:

```text
roma 1.jpg
```

La colonna si chiama:

```sql
nome_immagine
```

## 5.2 Dove si trova la cartella

Nel properties:

```properties
hotel.images.path=${user.home}/Desktop/AdventureTimeImages
```

Sul tuo computer `${user.home}` diventa normalmente:

```text
C:/Users/sammy
```

Il percorso completo diventa:

```text
C:/Users/sammy/Desktop/AdventureTimeImages
```

## 5.3 Classe che costruisce il percorso

`HotelImageLoader` esegue concettualmente:

```text
cartella base + nome file del DB
```

Esempio:

```text
C:/Users/sammy/Desktop/AdventureTimeImages
+
roma 1.jpg
=
C:/Users/sammy/Desktop/AdventureTimeImages/roma 1.jpg
```

Poi converte il `Path` in un URI che JavaFX può usare per creare un `Image`.

## 5.4 Dove viene visualizzata

La foto è mostrata in:

- `hotel-card.fxml` tramite `HotelCardGraphicController`;
- `checkout.fxml` tramite `CheckoutGraphicController`;
- anteprima del venditore in `manage-hotels.fxml` tramite `ManageHotelsGraphicController`.

## 5.5 Perché non salvare il percorso completo nel DB

Un percorso come:

```text
C:/Users/sammy/Desktop/...
```

funzionerebbe soltanto sul tuo PC.

Salvando solo il nome, puoi spostare l'app su un altro computer e cambiare una sola proprietà.

### Frase pronta

> Nel database salvo soltanto il nome logico dell'immagine. La directory fisica è una configurazione esterna, così il database non dipende dal computer su cui viene eseguita l'app.

---

# 6. SQL semplificato: significato delle parole rimaste

## `PRIMARY KEY`

Identifica in modo univoco una riga.

```sql
id BIGINT AUTO_INCREMENT PRIMARY KEY
```

## `AUTO_INCREMENT`

MySQL assegna automaticamente il prossimo identificativo.

## `NOT NULL`

Il campo è obbligatorio.

## `UNIQUE`

Non possono esistere due utenti con la stessa email.

## `DEFAULT`

Assegna un valore se non ne viene specificato uno.

```sql
punti INT NOT NULL DEFAULT 0
```

## `ENUM`

Limita i valori possibili.

```sql
role ENUM('CLIENTE', 'GESTORE', 'ADMIN')
```

## `DECIMAL(10,2)`

Numero con due cifre decimali, adatto ai prezzi.

## `FOREIGN KEY`

Collega una tabella a un'altra.

```sql
FOREIGN KEY (hotel_id) REFERENCES hotelrooms(id)
```

Significa che una prenotazione può indicare soltanto un hotel realmente esistente.

## Cosa erano `CHARACTER SET` e `COLLATE`

La versione precedente conteneva:

```sql
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci
```

- `CHARACTER SET` decide come vengono codificati i caratteri;
- `COLLATE` decide come confrontare e ordinare le stringhe.

Non erano sbagliati, ma per questo progetto didattico non sono indispensabili. MySQL usa comunque le proprie impostazioni predefinite, quindi sono stati rimossi per rendere lo script più facile da spiegare.

## Cosa erano i `CONSTRAINT nome...`

Una scrittura come:

```sql
CONSTRAINT fk_booking_user
    FOREIGN KEY (user_id) REFERENCES users(id)
```

assegna semplicemente un nome al vincolo.

La versione semplificata scrive direttamente:

```sql
FOREIGN KEY (user_id) REFERENCES users(id)
```

Il comportamento essenziale rimane lo stesso.

---

# 7. Flusso completo della prenotazione

Prima della conferma ci sono due momenti diversi:

1. calcolo del preventivo;
2. salvataggio definitivo.

## 7.1 Quando si apre il checkout

`CheckoutGraphicController.initialize()`:

1. legge `FlowContext`;
2. recupera hotel selezionato e criteri di ricerca;
3. mostra nome, date, persone e immagine;
4. chiama `refreshQuote()`.

`refreshQuote()`:

1. chiama `buildRequest()`;
2. crea `BookingRequestBean`;
3. richiama `ManageBookingsApplicationController.getQuote()`;
4. il controller applicativo delega a `BookingFacade.quote()`;
5. la Facade calcola prezzo base, extra, punti e totale;
6. il controller grafico aggiorna le Label.

Questa fase **non salva nulla**.

## 7.2 Quando l'utente cambia una checkbox

Ogni checkbox chiama:

```java
onOptionsChanged()
```

che richiama ancora `refreshQuote()`.

Perciò il totale viene ricalcolato immediatamente.

## 7.3 Quando l'utente preme “Conferma prenotazione”

### Passaggio 1 — Graphic Controller

Viene eseguito:

```java
CheckoutGraphicController.onConfirm()
```

Il metodo:

1. controlla i campi carta con `validCardFields()`;
2. crea `BookingRequestBean` con `buildRequest()`;
3. recupera il controller applicativo da `AppContext`;
4. chiama `confirm(request)`.

### Passaggio 2 — AppContext

Viene eseguito:

```java
AppContext.manageBookingsController()
```

Questo metodo costruisce:

1. `BookingFacade` con i tre DAO;
2. `ManageBookingsApplicationController` con Facade e `FlowContext`.

### Passaggio 3 — Application Controller

Viene eseguito:

```java
ManageBookingsApplicationController.confirm(request)
```

Il metodo:

1. chiama `bookingFacade.createBooking(request)`;
2. se tutto va bene, svuota il buffer con `flowContext.clearBookingFlow()`;
3. restituisce `BookingBean` alla GUI.

### Passaggio 4 — Facade

Viene eseguito:

```java
BookingFacade.createBooking(request)
```

La Facade esegue:

1. `quote(request)` per validare e calcolare nuovamente il totale;
2. `requireTraveler()` per verificare il ruolo;
3. `bookingDAO.isHotelAvailable(...)` per ricontrollare la disponibilità;
4. crea l'entità `Booking` in memoria;
5. chiama `bookingDAO.save(booking)`;
6. calcola i nuovi punti;
7. chiama `userDAO.updatePoints(...)`;
8. aggiorna `UserSession`;
9. crea e restituisce un `BookingBean`.

Il secondo controllo della disponibilità è importante perché, tra ricerca e conferma, un altro utente potrebbe aver prenotato la stessa camera.

### Passaggio 5 — DAO JDBC

In modalità DB viene eseguito:

```java
JdbcBookingDAO.save(booking)
```

Il metodo:

1. apre una connessione;
2. prepara l'`INSERT`;
3. inserisce i valori nei `?` del `PreparedStatement`;
4. esegue l'INSERT;
5. legge l'id generato dal database;
6. restituisce una copia della prenotazione con l'id.

Poi viene eseguito:

```java
JdbcUserDAO.updatePoints(userId, updatedPoints)
```

che esegue:

```sql
UPDATE users SET punti = ? WHERE id = ?
```

### Passaggio 6 — Ritorno alla GUI

Il risultato torna lungo la catena:

```text
JdbcBookingDAO
→ BookingFacade
→ ManageBookingsApplicationController
→ CheckoutGraphicController
```

Infine il controller grafico:

1. mostra codice e totale;
2. chiede a `SceneRouter` di aprire il profilo.

---

# 8. Sequence diagram testuale

```text
Viaggiatore
    │
    │ preme Conferma
    ▼
CheckoutGraphicController.onConfirm()
    │ controlla i dati della carta
    │ buildRequest()
    ▼
AppContext.manageBookingsController()
    │ crea BookingFacade e ManageBookingsApplicationController
    ▼
ManageBookingsApplicationController.confirm(request)
    ▼
BookingFacade.createBooking(request)
    │
    ├── quote(request)
    │     ├── BeanValidator.validateBookingRequest()
    │     ├── requireTraveler()
    │     ├── HotelDAO.findById()
    │     ├── calcolo numero notti
    │     ├── BaseBookingPrice
    │     ├── BookingPriceDecoratorFactory.decorate()
    │     ├── calcolo sconto punti
    │     └── restituisce BookingQuoteBean
    │
    ├── requireTraveler()
    ├── BookingDAO.isHotelAvailable()
    ├── new Booking(...)
    ├── BookingDAO.save()
    │      └── in modalità DB: JdbcBookingDAO.save()
    ├── UserDAO.updatePoints()
    │      └── in modalità DB: JdbcUserDAO.updatePoints()
    ├── UserSession.updatePoints()
    └── restituisce BookingBean
    ▼
ManageBookingsApplicationController
    │ FlowContext.clearBookingFlow()
    ▼
CheckoutGraphicController
    │ mostra conferma
    │ SceneRouter.show(PROFILE)
    ▼
Profilo utente
```

---

# 9. Versione orale molto breve del flusso

> Quando il viaggiatore conferma, il controller JavaFX raccoglie i dati in un `BookingRequestBean` e chiama il controller applicativo. Il controller applicativo delega alla `BookingFacade`, che valida la richiesta, carica l'hotel tramite DAO, calcola il prezzo con il Decorator, ricontrolla la disponibilità e salva la prenotazione. Successivamente aggiorna i punti sia nella persistenza sia nella sessione. Solo dopo il successo il buffer temporaneo viene svuotato e la GUI passa al profilo.

---

# 10. Cose da ricordare assolutamente all'esame

1. GUI e CLI condividono i controller applicativi.
2. I controller grafici non eseguono query SQL.
3. I DAO nascondono DB, file e memoria.
4. La DAO Factory sceglie una famiglia coerente di DAO.
5. `UserSession` conserva l'utente; `FlowContext` conserva il flusso temporaneo.
6. Il preventivo non salva nulla.
7. La disponibilità viene ricontrollata al momento della conferma.
8. Il Decorator combina gli extra.
9. La Facade coordina il sottosistema prenotazione.
10. Nel DB si salva solo il nome dell'immagine; il percorso è configurabile.
