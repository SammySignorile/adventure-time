# Analisi dei progetti di riferimento

## 1. OrtoGest

Il repository separa chiaramente:

- `appcontroller`: controller applicativi divisi per caso d'uso;
- `graphiccontroller` e `graphiccontrollercli`: due boundary diverse;
- `beans`: oggetti di scambio verso la presentazione;
- `dao`: interfacce e implementazioni JDBC, filesystem e in-memory;
- `model`: entità di dominio;
- `utils/pattern`: infrastruttura e soluzioni trasversali.

Il punto più utile per Adventure Time non è copiare nomi o codice, ma mantenere la regola:

> la GUI e la CLI cambiano, il controller applicativo del caso d'uso rimane uno solo.

Adventure Time applica questa regola con, ad esempio:

- `LoginGraphicController` e `LoginCLIGraphicController`;
- entrambi chiamano `LoginApplicationController`;
- nessuno dei due accede direttamente a un DAO.

## 2. ISPW_FINAL_EXAM

Il progetto adotta una struttura più generale e riusabile:

- lettura delle proprietà prima dell'avvio;
- scelta runtime tra JavaFX e CLI;
- `DAOFactoryAbstract` con factory DB, demo e filesystem;
- session manager;
- package distinti per Bean, mapper, model, use case e navigazione.

Adventure Time conserva questi elementi, ma evita una gerarchia troppo ampia per un progetto universitario di dimensione contenuta:

- `ConfigLoader` legge un file properties;
- `AppBootstrap` sceglie una sola famiglia DAO;
- `InterfaceFactory` sceglie GUI o CLI;
- `AppContext` fa da composition root;
- `UserSession` conserva l'utente autenticato;
- `SceneRouter` centralizza la navigazione JavaFX.

## 3. Progetti allegati

### FoodMood e Cardify

Gli aspetti più solidi sono:

- Abstract Factory per rendere la logica indipendente dalla persistenza;
- Singleton/composition root per componenti condivisi;
- Beans e mapper per non esporre direttamente il model alla View;
- selezione della UI a runtime;
- separazione fra controller grafico e controller applicativo.

### Biblioteca

È utile il concetto di Facade come ingresso semplice per un sottosistema. Adventure Time usa `BookingFacade` per evitare che `ManageBookingsApplicationController` debba coordinare direttamente:

1. hotel DAO;
2. booking DAO;
3. user DAO;
4. disponibilità;
5. punti;
6. catena Decorator;
7. salvataggio finale.

### Meal Planner

La lezione principale è usare pattern solo quando riducono realmente il problema. Per questo Adventure Time non introduce pattern artificiali per ogni classe. I pattern scelti hanno un motivo visibile nel flusso d'esame:

- Factory per configurazione variabile;
- Facade per il sottosistema prenotazione;
- Decorator per combinare servizi extra;
- Session Context per privilegi;
- MVC/BCE per separare presentation, application e persistence.

## 4. Differenze intenzionali rispetto al progetto iniziale mostrato negli screenshot

Sono state corrette queste criticità:

- package singolo `org.example.adventuretime`, senza alternare `org.example`, `albergatore`, `cliente` e package a lettere maiuscole;
- nomi coerenti in inglese per le classi e in italiano per i testi UI;
- eliminazione di controller duplicati con responsabilità sovrapposte;
- `SceneSwitcher` sostituito da `SceneRouter` unico;
- DAO specializzati ma creati da una factory comune;
- nessun controller JavaFX crea direttamente DAO o connessioni;
- immagini non collegate a percorsi assoluti dentro `Downloads`;
- FXML validi, con namespace e import corretti;
- dati tra schermate passati tramite `FlowContext`, non tramite campi statici sparsi;
- scrittura persistente soltanto alla conferma della prenotazione.

## 5. Scelta del perimetro

Per restare credibile all'esame, il progetto non implementa attività turistiche, pagamenti reali, email, mappe o API esterne. Il flusso completo e dimostrabile è:

`login → ricerca hotel → risultati → preventivo → extra/punti → conferma → profilo`

Il venditore dispone del flusso complementare:

`login → gestione strutture → visualizzazione prenotazioni ricevute`.
