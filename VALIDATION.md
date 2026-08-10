# Validazione della versione semplificata

Data: 2026-08-04

## Controlli eseguiti con successo

1. Compilazione con `javac 21` del core applicativo:
   - configuration;
   - bean;
   - model;
   - exception;
   - dao;
   - mapper;
   - pattern;
   - facade;
   - session;
   - util;
   - application controller.
2. Parsing XML di tutti i 9 file FXML.
3. Controllo che ogni `fx:id` presente negli FXML abbia un campo nel controller corrispondente.
4. Controllo che ogni metodo indicato da `onAction` esista nel controller.
5. Caricamento reale di `application.properties` con `ConfigLoader`.
6. Espansione della proprietà `${user.home}` nella cartella immagini.
7. Controllo di coerenza tra colonna SQL `nome_immagine` e `JdbcHotelDAO`.
8. Controllo di assenza dei vecchi nomi `imageUrl` e `immagine_url` nel codice attivo.

## Controlli da fare sul computer dello studente

L'ambiente usato per preparare i file non dispone di Maven/OpenJFX e di un server MySQL attivo. Sul computer locale bisogna quindi eseguire:

```bash
mvn clean test
mvn clean javafx:run
```

Per la modalità DB:

1. eseguire `database/adventuretime.sql` su MySQL;
2. controllare utente e password in `application-full-db.properties`;
3. creare la cartella `Desktop/AdventureTimeImages`;
4. copiare le immagini con nomi uguali a quelli del database;
5. avviare con la configurazione full DB.

## Verifica visiva consigliata

- login come viaggiatore;
- ricerca Roma;
- controllo foto nelle card;
- selezione hotel;
- controllo foto nel checkout;
- aggiunta/rimozione extra e ricalcolo del prezzo;
- conferma prenotazione;
- verifica prenotazione nel profilo;
- login venditore;
- inserimento di una struttura indicando il nome file immagine;
- prova del pulsante Anteprima.
