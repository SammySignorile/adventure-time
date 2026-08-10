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

mvn clean javafx:run

```

Per la CLI, se il plugin JavaFX forza comunque il launcher grafico, eseguire:

```bash
mvn clean package
java -Dadventure.config=/application-cli.properties \
  -cp target/adventure-time-1.0.0.jar org.example.adventuretime.Main
```

## Account demo

- Viaggiatore: `mario@test.com` / `1234`
- Venditore: `mike@gmail.com` / `1234`

## Foto degli hotel

1. Creare la cartella `AdventureTimeImages` sul Desktop.
2. Copiare nella cartella file come `roma 1.jpg`.
3. Nel database salvare soltanto il nome nella colonna `nome_immagine`.
4. La proprietà `hotel.images.path` indica la directory da usare.

Il caricamento è gestito da `HotelImageLoader`. Se il file non esiste, la GUI mostra “Foto non disponibile”.

## MySQL

1. Eseguire `database/adventuretime.sql`.
2. Modificare utente e password in `application-full-db.properties`.
3. Avviare selezionando quella configurazione.

Lo script principale è stato mantenuto volutamente semplice: tre tabelle, chiavi primarie, chiavi esterne e dati di prova. I controlli applicativi più specifici restano nella logica Java.

## Test

```bash
mvn test
```

Sono presenti test per login, ricerca, prenotazione/Decorator e gestione hotel. Prima della consegna sostituire nei commenti di ciascuna classe di test il segnaposto con nome e matricola del responsabile.

## Struttura della documentazione

- `docs/ANALISI_PROGETTI_RIFERIMENTO.md`: cosa è stato riutilizzato come principio e cosa è stato semplificato.
- `docs/GUIDA_ARCHITETTURA_E_CODICE.md`: spiegazione tecnica approfondita.
- `docs/GUIDA_SEMPLIFICATA_PER_ESAME.md`: spiegazione semplice, cartella per cartella, con frasi da usare all'orale.
- `docs/FLUSSO_PRENOTAZIONE.md`: ordine esatto dei metodi eseguiti durante il checkout.
- `docs/CHECKLIST_ESAME.md`: confronto con i requisiti del professore e attività ancora da personalizzare.
- `docs/uml/*.puml`: diagrammi PlantUML modificabili.
