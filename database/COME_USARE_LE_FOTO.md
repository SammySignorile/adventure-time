# Come funzionano le foto degli hotel

## 1. Dove si trovano le immagini

Le immagini distribuite con il progetto si trovano in:

```text
src/main/resources/images
```

`HotelImageLoader` le carica dal classpath, quindi non serve creare una
cartella specifica sul Desktop.

## 2. Nel database salva solo il nome

Nelle colonne `hotelrooms.nome_immagine` e
`hotel_images.nome_immagine` viene salvato, per esempio:

```text
roma 1.jpg
```

Non contiene tutto il percorso `C:\Users\...`.

## 3. Come viene caricata una foto

Il programma cerca il nome ricevuto dal database sotto `/images/` nel
classpath. Nell'esecuzione da IntelliJ o Maven la cartella delle risorse viene
inclusa automaticamente.

```text
/images/ + roma 1.jpg
```

Se il file non è presente, l'interfaccia mostra il segnaposto previsto senza
interrompere l'applicazione.

## 4. Immagine principale e galleria

`hotelrooms.nome_immagine` contiene l'immagine principale. La tabella
`hotel_images` può contenere ulteriori immagini dello stesso hotel e viene
letta dal `JdbcHotelDAO`.
