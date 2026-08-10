# Come funzionano le foto degli hotel

## 1. Crea la cartella sul Desktop

Il progetto usa come impostazione predefinita:

```text
C:\Users\TUO_NOME\Desktop\AdventureTimeImages
```

Sul computer mostrato nello screenshot sarà normalmente:

```text
C:\Users\sammy\Desktop\AdventureTimeImages
```

## 2. Copia le immagini nella cartella

Esempio:

```text
AdventureTimeImages
├── roma 1.jpg
├── roma 2.jpg
├── roma 3.jpg
├── roma 4.jpg
├── milano 1.jpg
├── napoli 1.jpg
├── napoli 2.jpg
├── napoli 3.jpg
├── napoli 4.jpg
└── firenze 1.jpg
```

## 3. Nel database salva solo il nome

Nella tabella `hotelrooms` la colonna è:

```sql
nome_immagine VARCHAR(255)
```

Un record contiene, per esempio:

```sql
'roma 1.jpg'
```

Non contiene tutto il percorso `C:\Users\...`.

## 4. Il programma costruisce il percorso

`HotelImageLoader` unisce:

```text
cartella configurata + nome presente nel database
```

quindi:

```text
C:\Users\sammy\Desktop\AdventureTimeImages
+
roma 1.jpg
=
C:\Users\sammy\Desktop\AdventureTimeImages\roma 1.jpg
```

## 5. Cambiare cartella

Nel file `application.properties` modifica:

```properties
hotel.images.path=${user.home}/Desktop/AdventureTimeImages
```

`${user.home}` viene sostituito automaticamente con la cartella dell'utente Windows.
