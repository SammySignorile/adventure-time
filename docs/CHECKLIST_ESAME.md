# Checklist rispetto alle consegne ISPW

## Già coperto dal codice

- [x] progetto Java/Maven;
- [x] JavaFX con FXML;
- [x] seconda interfaccia CLI;
- [x] stessa funzionalità principale nelle due interfacce;
- [x] un controller applicativo per flusso condiviso;
- [x] modalità DEMO solo in-memory;
- [x] modalità FULL con DB o filesystem;
- [x] almeno un DAO in DB, filesystem e memoria; nel progetto lo sono tutti e tre;
- [x] Abstract Factory per le famiglie DAO;
- [x] factory per GUI/CLI;
- [x] Facade per la prenotazione;
- [x] Decorator per i servizi extra;
- [x] Session Context per ruoli;
- [x] eccezioni personalizzate gestite nei boundary;
- [x] nove test per configurazione e flussi principali;
- [x] circa 5.500 righe Java, senza codice aggiunto al solo scopo di aumentare il conteggio;
- [x] script MySQL coerente;
- [x] diagrammi PlantUML modificabili.

## Da personalizzare prima della consegna

- [ ] inserire nomi e matricole nei commenti dei test;
- [ ] decidere quanti membri compongono il gruppo;
- [ ] preparare 3 user story e 3 requisiti funzionali per membro;
- [ ] preparare almeno 2 storyboard per membro;
- [ ] produrre 1 VOPC e 1 design-level diagram per membro;
- [ ] assegnare un pattern diverso a ciascun membro nella relazione;
- [ ] produrre activity, sequence e state diagram richiesti per membro;
- [x] configurare repository Git e branch strategy;
- [ ] collegare SonarCloud e risolvere ogni issue;
- [ ] registrare video di 1–2 minuti;
- [ ] creare la relazione PDF finale seguendo l'indice ufficiale;
- [ ] verificare che codice, diagrammi e relazione descrivano la stessa versione.

I punti ancora aperti richiedono dati personali del gruppo, account esterni o
materiale multimediale: non possono essere completati correttamente inventando
nomi, matricole, risultati SonarCloud, storyboard o video.

## Pattern suggeriti da assegnare

Per un gruppo di tre persone:

1. **Abstract Factory** – configurazione e persistenza;
2. **Decorator** – assicurazioni e cambio data;
3. **Facade** – creazione completa della prenotazione.

Singleton può essere descritto per il contesto applicativo, ma è meglio non presentarlo come unico pattern principale se il professore richiede un pattern differente per membro.

## Funzionalità da non aggiungere senza tempo sufficiente

- pagamento reale;
- mappe/API esterne;
- chat;
- email automatiche;
- gestione voli;
- pannello amministratore completo;
- attività turistiche.

Sono estensioni possibili, ma aumentano diagrammi, test, errori e incoerenze. Il flusso hotel è già completo e difendibile.
