# Adventure Time

Progetto didattico per **Ingegneria del Software e Progettazione Web**: applicazione Java per cercare hotel, creare prenotazioni e gestire strutture ricettive.

## Funzionalità

### Viaggiatore (`CLIENTE`)
- login;
- ricerca per città, date, persone e prezzo massimo;
- visualizzazione delle sole camere compatibili e disponibili;
- preventivo con punti fedeltà;
- aggiunta di assicurazione annullamento, assicurazione medica e cambio data flessibile;
- conferma della prenotazione;
- profilo e storico prenotazioni.

### Venditore (`GESTORE`)
- login;
- visualizzazione delle proprie strutture;
- inserimento ed eliminazione di una struttura;
- visualizzazione delle prenotazioni ricevute;
- annullamento di una prenotazione ricevuta;
- profilo.

## Architettura essenziale

- **MVC Pull / BCE**: la View invoca il graphic controller; il graphic controller costruisce Beans e chiama un application controller; l'application controller recupera i dati tramite DAO.
- **Un application controller per caso d'uso**, riutilizzato da JavaFX e CLI.
- **DAO Abstract Factory**: una famiglia coerente di DAO per DB, filesystem o memoria.
- **Facade**: `BookingFacade` coordina preventivo, disponibilità, punti e salvataggio.
- **Decorator**: i servizi extra modificano il prezzo senza moltiplicare sottoclassi di prenotazione.
- **Session Context**: `UserSession` conserva identità e privilegi dopo il login.
- **Flow Buffer**: `FlowContext` conserva temporaneamente criteri, risultati e hotel selezionato durante `ricerca → lista → checkout`.
- **Centralized navigation**: `SceneRouter` cambia la root dello stesso `Stage`; i controller non aprono finestre autonome.

## Avvio

Requisiti: JDK 21+, Maven 3.9+, JavaFX risolto da Maven.

```bash
mvn clean javafx:run
```

La configurazione predefinita è **GUI + DEMO + IN_MEMORY**.

Configurazioni già incluse:

```bash
# GUI, DEMO, dati soltanto in memoria (predefinita)
mvn javafx:run

# CLI, DEMO, dati soltanto in memoria
mvn -Dadventure.config=/application-cli.properties javafx:run

# GUI, FULL, persistenza su file
mvn -Dadventure.config=/application-full-filesystem.properties javafx:run

# GUI, FULL, persistenza MySQL
mvn -Dadventure.config=/application-full-db.properties javafx:run
```

`ConfigLoader` valida la coppia modalità/persistenza: DEMO accetta soltanto
`IN_MEMORY`, mentre FULL richiede `FILESYSTEM` oppure `DB`.

## Account demo

- Viaggiatore: `mario@test.com` / `1234`
- Venditore: `mike@gmail.com` / `1234`

## Foto degli hotel

Le immagini distribuite con l'applicazione si trovano in
`src/main/resources/images`. Nel database e nello stato persistente viene
salvato soltanto il nome del file. `HotelImageLoader` carica la risorsa dal
classpath e, se non esiste, la GUI mostra “Foto non disponibile”.

## MySQL

1. Importare in MySQL Workbench lo script `adventuretime.sql` consegnato
   separatamente dal progetto.
2. Modificare utente e password in `application-full-db.properties`.
3. Avviare selezionando quella configurazione.

Lo script non è versionato nel repository perché contiene la preparazione
dell'ambiente MySQL. La versione consegnata separatamente contiene quattro
tabelle, chiavi primarie, chiavi esterne e dati di prova.

## Test

```bash
mvn verify
```

Sono presenti 23 test automatici per configurazione, login, ricerca,
prenotazione, gestione delle prenotazioni ricevute e Decorator. Prima della
consegna va completata la matricola nei
commenti di ciascuna classe di test e va verificato che l'assegnazione rispecchi
quella effettiva del gruppo.

Il comando `verify` esegue i test e genera il report di coverage JaCoCo in
`target/site/jacoco/index.html`.

## Struttura della documentazione

- `docs/ANALISI_PROGETTI_RIFERIMENTO.md`: cosa è stato riutilizzato come principio e cosa è stato semplificato.
- `docs/GUIDA_ARCHITETTURA_E_CODICE.md`: spiegazione tecnica approfondita.
- `docs/GUIDA_SEMPLIFICATA_PER_ESAME.md`: spiegazione semplice, cartella per cartella, con frasi da usare all'orale.
- `docs/FLUSSO_PRENOTAZIONE.md`: ordine esatto dei metodi eseguiti durante il checkout.
- `docs/CHECKLIST_ESAME.md`: confronto con i requisiti del professore e attività ancora da personalizzare.
- `docs/uml/*.puml`: diagrammi PlantUML modificabili.
