# Correzioni e semplificazioni dello script SQL iniziale

1. Rimossa la riga con email vuota perché `email` deve identificare un account valido e univoco.
2. Aggiunto `gestore_id` in `hotelrooms` per sapere quale venditore possiede la struttura.
3. Uniformati i nomi delle colonne usati dai DAO Java.
4. Aggiunti in `bookings` i campi realmente usati dal codice: `persone`, `extras`, `punti_usati` e `stato`.
5. Rinominato il campo immagine in `nome_immagine`: contiene solo il nome del file, non un URL completo.
6. Allineata `hotel_images` a `JdbcHotelDAO`, usando la colonna `nome_immagine` per la galleria dell'hotel.
7. Rimosse le stored procedure: i DAO usano query parametrizzate con `PreparedStatement` e non ne hanno bisogno.
8. Impostato `utf8mb4` per conservare correttamente i caratteri accentati e il simbolo `•` in MySQL Workbench.
9. Sono rimaste le chiavi esterne essenziali, perché impediscono di creare prenotazioni collegate a utenti o hotel inesistenti.
10. I controlli più specifici, come date valide, prezzo positivo e capienza, vengono eseguiti dai Bean di input e dai controller applicativi.
