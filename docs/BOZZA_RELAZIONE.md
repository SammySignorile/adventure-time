# Adventure Time — bozza della relazione tecnica

> Questa è una base da personalizzare con componenti del gruppo, matricole, storyboard esportati, screenshot, link GitHub/SonarCloud e video.

## 1. Software Requirement Specification

### 1.1 Scopo

Il documento definisce requisiti, attori, flussi e scelte progettuali di Adventure Time, applicazione desktop Java che permette ai viaggiatori di cercare e prenotare camere e ai venditori di gestire le proprie strutture.

### 1.2 Panoramica

L'applicazione presenta due ruoli:

- **Viaggiatore**: ricerca hotel, valuta il prezzo, seleziona servizi aggiuntivi, usa punti fedeltà e conferma una prenotazione.
- **Venditore**: inserisce/rimuove camere, consulta e annulla le prenotazioni ricevute.

L'app è disponibile con due interfacce, JavaFX e command line, e può essere avviata in modalità demo in-memory o full con MySQL/filesystem.

### 1.3 Requisiti hardware e software

- JDK 21 o superiore;
- Maven 3.9 o superiore;
- JavaFX 21;
- MySQL 8+ per il backend DB;
- filesystem scrivibile per il backend file;
- 4 GB RAM consigliati;
- IntelliJ IDEA e Scene Builder consigliati;
- Git e SonarCloud per versionamento e controllo qualità.

### 1.4 Sistemi correlati

#### Booking.com

**Pro:** catalogo ampio, filtri avanzati, recensioni e processo di prenotazione consolidato.  
**Contro:** piattaforma molto complessa, non adatta a mostrare in modo didattico una struttura software piccola e controllabile.

#### Airbnb

**Pro:** esperienza visiva efficace e gestione di host e viaggiatori.  
**Contro:** dominio più ampio, con messaggistica, recensioni, pagamenti, verifica identità e regole di cancellazione complesse.

Adventure Time concentra il perimetro su ricerca, disponibilità, prenotazione con extra e gestione delle strutture.

## 2. User stories

1. Come viaggiatore, voglio cercare una camera indicando città, date, numero di persone e budget, per visualizzare soltanto soluzioni compatibili.
2. Come viaggiatore, voglio aggiungere servizi assicurativi e usare punti, per personalizzare il viaggio e ottenere uno sconto.
3. Come viaggiatore, voglio visualizzare lo storico delle prenotazioni, per controllare i viaggi acquistati.
4. Come venditore, voglio aggiungere una struttura al catalogo, per renderla disponibile ai viaggiatori.
5. Come venditore, voglio rimuovere una mia struttura, per mantenere aggiornata l'offerta.
6. Come venditore, voglio visualizzare le prenotazioni ricevute, per conoscere occupazione e incassi previsti.
7. Come venditore, voglio annullare una prenotazione ricevuta, per gestire un soggiorno che non puo essere erogato.

## 3. Requisiti funzionali

1. Il sistema deve autenticare l'utente e memorizzare nella sessione identità e ruolo.
2. Il sistema deve mostrare soltanto camere con città, prezzo e capienza compatibili.
3. Il sistema deve escludere le camere con prenotazioni sovrapposte all'intervallo richiesto.
4. Il sistema deve calcolare il prezzo come prezzo per notte moltiplicato per il numero di notti, più i servizi extra, meno lo sconto punti.
5. Il sistema deve ricontrollare la disponibilità immediatamente prima della conferma.
6. Il sistema deve permettere a un venditore di modificare soltanto strutture associate al proprio identificativo.
7. Il sistema deve permettere a un venditore di annullare soltanto prenotazioni ricevute dalle proprie strutture.

## 4. Use case: prenotare un hotel

### Flusso principale

1. Il viaggiatore effettua il login.
2. Il sistema crea la sessione e mostra la home viaggiatore.
3. Il viaggiatore apre la ricerca.
4. Il sistema richiede città, check-in, check-out, persone e prezzo massimo.
5. Il viaggiatore inserisce i dati e conferma.
6. Il sistema valida i dati.
7. Il sistema recupera dal DAO gli hotel compatibili.
8. Il sistema esclude gli hotel occupati nelle date richieste.
9. Il sistema mostra i risultati.
10. Il viaggiatore seleziona un hotel.
11. Il sistema conserva hotel e criteri nel buffer temporaneo.
12. Il viaggiatore seleziona eventuali assicurazioni e l'uso dei punti.
13. Il sistema costruisce la catena Decorator e mostra il preventivo.
14. Il viaggiatore inserisce i dati carta simulati e conferma.
15. Il sistema ricontrolla la disponibilità.
16. Il sistema salva la prenotazione e aggiorna i punti.
17. Il sistema svuota il buffer e mostra il codice prenotazione.

### Estensioni

- **6a. Input non valido:** il sistema mostra il motivo e torna al form.
- **8a. Nessun risultato:** il sistema mostra un messaggio e permette una nuova ricerca.
- **14a. Dati carta incompleti:** il sistema non conferma e richiede i campi mancanti.
- **15a. Camera non più disponibile:** il sistema genera `HotelUnavailableException`, non salva e invita a ripetere la ricerca.
- **16a. Errore di persistenza:** il sistema mostra un messaggio gestito senza terminare l'applicazione.

## 5. Design pattern

### Abstract Factory

`DAOFactory` e le factory concrete creano una famiglia di DAO coerente con il backend selezionato.

### Decorator

I servizi aggiuntivi avvolgono `BaseBookingPrice`, aggiungendo prezzo e descrizione senza creare combinazioni rigide di sottoclassi.

### Facade

`BookingFacade` nasconde il coordinamento fra hotel, prenotazioni, punti e pricing.

### Simple Factory

`InterfaceFactory` seleziona JavaFX o CLI a runtime.

## 6. Eccezioni

Le eccezioni personalizzate non vengono semplicemente catturate e rilanciate:

- la persistenza converte gli errori tecnici in `PersistenceException`;
- i controller applicativi generano errori di validazione/autorizzazione specifici;
- JavaFX mostra Alert;
- CLI mostra messaggi e continua il loop;
- `HotelUnavailableException` riporta l'utente al flusso di ricerca.

## 7. Testing

- login valido, password errata e logout;
- ricerca con filtri e propagazione nel `FlowContext`;
- calcolo Decorator e salvataggio prenotazione;
- creazione di una struttura da parte del venditore.
- annullamento di una prenotazione ricevuta da parte del venditore.

## 8. Persistenza

- **Demo:** `InMemoryDataStore`, dati persi alla chiusura.
- **Filesystem:** serializzazione di `DataState` con scrittura temporanea.
- **DB:** MySQL tramite DAO JDBC, una connessione condivisa e `PreparedStatement` chiusi dopo ogni operazione.

## 9. Discrepanze/limiti dichiarati

- la carta è soltanto simulata e non viene salvata;
- non sono implementati voli, attività, email o pagamenti reali;
- il ruolo ADMIN esiste nello schema per compatibilità, ma non ha un flusso UI;
- nel backend JDBC prenotazione e punti non sono ancora racchiusi in una singola transazione;
- ogni hotel ha un'immagine principale e può avere una piccola galleria; nel DB viene salvato soltanto il nome della risorsa inclusa nel classpath.
