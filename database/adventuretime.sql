-- ============================================================
-- ADVENTURE TIME - DATABASE SEMPLIFICATO
-- ============================================================
-- Questo script è volutamente semplice e coerente con i DAO Java.
-- Nel campo nome_immagine viene salvato SOLO il nome del file,
-- per esempio: roma 1.jpg
-- Le immagini si trovano in src/main/resources/images.

DROP DATABASE IF EXISTS adventuretimedb;
CREATE DATABASE adventuretimedb
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE adventuretimedb;

-- ------------------------------------------------------------
-- UTENTI
-- CLIENTE = viaggiatore
-- GESTORE = venditore/proprietario di hotel
-- ------------------------------------------------------------
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    punti INT NOT NULL DEFAULT 0,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('CLIENTE', 'GESTORE', 'ADMIN') NOT NULL
);

-- ------------------------------------------------------------
-- HOTEL/CAMERE
-- gestore_id collega la struttura al suo venditore.
-- nome_immagine contiene solo il nome della risorsa nel classpath.
-- ------------------------------------------------------------
CREATE TABLE hotelrooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    gestore_id BIGINT NOT NULL,
    nome VARCHAR(150) NOT NULL,
    citta VARCHAR(100) NOT NULL,
    tipo_camera VARCHAR(100) NOT NULL,
    servizi TEXT,
    distanza_centro VARCHAR(80),
    prezzo_notte DECIMAL(10,2) NOT NULL,
    nome_immagine VARCHAR(255),
    capienza INT NOT NULL,

    FOREIGN KEY (gestore_id) REFERENCES users(id)
);

-- ------------------------------------------------------------
-- PRENOTAZIONI
-- user_id indica il viaggiatore.
-- hotel_id indica la camera prenotata.
-- ------------------------------------------------------------
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    hotel_id BIGINT NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    persone INT NOT NULL,
    prezzo_totale DECIMAL(10,2) NOT NULL,
    extras VARCHAR(300) NOT NULL DEFAULT '',
    punti_usati INT NOT NULL DEFAULT 0,
    stato ENUM('CONFIRMED', 'CANCELLED') NOT NULL DEFAULT 'CONFIRMED',

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (hotel_id) REFERENCES hotelrooms(id)
);

-- ------------------------------------------------------------
-- DATI DI PROVA
-- ------------------------------------------------------------
INSERT INTO users(nome, cognome, punti, email, password, role) VALUES
('Mario', 'Rossi', 1000, 'mario@test.com', '1234', 'CLIENTE'),
('Luca', 'Bianchi', 100, 'luca@test.com', '1234', 'CLIENTE'),
('Sammy', 'Signorile', 600, 'sammy@gmail.com', '1234', 'CLIENTE'),
('Daniele', 'Di Meo', 4433, 'dany@gmail.com', '1234', 'CLIENTE'),
('Michele', 'Damiano', 432, 'mike@gmail.com', '1234', 'GESTORE'),
('Matteo', 'Leoncino', 34431, 'leoncino@gmail.com', '1234', 'GESTORE'),
('Giovanni', 'Manager', 43334, 'giovanni@hotel.com', '1234', 'GESTORE'),
('Sara', 'Manager', 44443, 'sara@hotel.com', '1234', 'GESTORE');

INSERT INTO hotelrooms(
    gestore_id,
    nome,
    citta,
    tipo_camera,
    servizi,
    distanza_centro,
    prezzo_notte,
    nome_immagine,
    capienza
) VALUES
(5, 'Hotel Roma Center', 'Roma', 'Camera Singola',
 'WiFi • Colazione inclusa • Piscina', '500m dal centro',
 120.00, 'roma 1.jpg', 1),

(5, 'Hotel Roma Termini', 'Roma', 'Camera Doppia',
 'WiFi • Colazione inclusa • Aria condizionata', '300m dal centro',
 110.00, 'roma 2.jpg', 2),

(5, 'Hotel Colosseo View', 'Roma', 'Suite Deluxe',
 'Vista Colosseo • WiFi • Spa', '150m dal centro',
 280.00, 'roma 3.jpg', 4),

(6, 'Hotel Trastevere Cozy', 'Roma', 'Camera Matrimoniale',
 'WiFi • Colazione • Terrazza', '800m dal centro',
 130.00, 'roma centro.jpg', 2),

(6, 'Hotel Milano Luxury', 'Milano', 'Suite Deluxe',
 'Spa • Piscina • Ristorante', '200m dal centro',
 250.00, 'stanza 2.jpg', 4),

(6, 'Hotel Napoli Mare', 'Napoli', 'Camera Matrimoniale',
 'Vista mare • WiFi • Colazione', '1km dal centro',
 90.00, 'colazione 2.jpg', 2),

(7, 'Hotel Napoli Centro', 'Napoli', 'Camera Singola',
 'WiFi • Colazione inclusa', '400m dal centro',
 85.00, 'colazione 3.jpg', 1),

(7, 'Hotel Vesuvio Luxury', 'Napoli', 'Suite',
 'Vista Vesuvio • Piscina • Spa', '600m dal centro',
 220.00, 'stanza 2.jpg', 4),

(8, 'Hotel Spaccanapoli', 'Napoli', 'Camera Doppia',
 'WiFi • Parcheggio • Colazione', '200m dal centro',
 95.00, 'colazione 2.jpg', 2),

(8, 'Hotel Firenze Relax', 'Firenze', 'Camera Doppia',
 'WiFi • Giardino • Parcheggio', '300m dal centro',
 140.00, 'colazione 3.jpg', 2);

-- ------------------------------------------------------------
-- IMMAGINI DEGLI HOTEL
-- Il DAO JDBC usa questa tabella per mostrare più immagini.
-- Anche qui viene salvato soltanto il nome del file nel classpath.
-- ------------------------------------------------------------
CREATE TABLE hotel_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hotel_id BIGINT NOT NULL,
    nome_immagine VARCHAR(255) NOT NULL,

    FOREIGN KEY (hotel_id) REFERENCES hotelrooms(id)
        ON DELETE CASCADE
);

INSERT INTO hotel_images(hotel_id, nome_immagine) VALUES
(1, 'roma 1.jpg'),
(1, 'roma centro.jpg'),
(1, 'colazione 1.jpg'),
(2, 'roma 2.jpg'),
(2, 'hotel roma.jpg'),
(2, 'termini.jpg'),
(2, 'stanza 2.jpg'),
(3, 'roma 3.jpg'),
(3, 'colosseo 3.jpg'),
(3, 'stanza roma 1.jpg');

INSERT INTO bookings(
    user_id,
    hotel_id,
    check_in,
    check_out,
    persone,
    prezzo_totale,
    extras,
    punti_usati,
    stato
) VALUES
(3, 1, '2027-06-10', '2027-06-15', 1,
 600.00, '', 0, 'CONFIRMED'),

(4, 2, '2027-07-15', '2027-07-18', 2,
 370.00, 'HEALTH_INSURANCE', 1000, 'CONFIRMED');
