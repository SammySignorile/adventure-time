-- Adventure Time - schema MySQL inizializzato automaticamente dall'app.
-- Nei campi immagine viene salvato soltanto il nome del file nelle risorse.

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    punti INT NOT NULL DEFAULT 0,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('CLIENTE', 'GESTORE', 'ADMIN') NOT NULL
);

CREATE TABLE IF NOT EXISTS hotelrooms (
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
    UNIQUE KEY uk_hotel_name (nome),
    FOREIGN KEY (gestore_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS bookings (
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
    UNIQUE KEY uk_booking_identity (user_id, hotel_id, check_in),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (hotel_id) REFERENCES hotelrooms(id)
);

CREATE TABLE IF NOT EXISTS hotel_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hotel_id BIGINT NOT NULL,
    nome_immagine VARCHAR(255) NOT NULL,
    UNIQUE KEY uk_hotel_image (hotel_id, nome_immagine),
    FOREIGN KEY (hotel_id) REFERENCES hotelrooms(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS app_metadata (
    id INT PRIMARY KEY,
    schema_version INT NOT NULL
);

-- Valori riutilizzati dai dati dimostrativi.
SET @role_cliente = 'CLIENTE';
SET @role_gestore = 'GESTORE';
SET @stato_confermato = 'CONFIRMED';
SET @email_mike = 'mike@gmail.com';
SET @email_leoncino = 'leoncino@gmail.com';
SET @email_giovanni = 'giovanni@hotel.com';
SET @email_sara = 'sara@hotel.com';

INSERT IGNORE INTO users(nome, cognome, punti, email, password, role) VALUES
('Mario', 'Rossi', 1000, 'mario@test.com', '1234', @role_cliente),
('Luca', 'Bianchi', 100, 'luca@test.com', '1234', @role_cliente),
('Sammy', 'Signorile', 600, 'sammy@gmail.com', '1234', @role_cliente),
('Daniele', 'Di Meo', 4433, 'dany@gmail.com', '1234', @role_cliente),
('Michele', 'Damiano', 432, @email_mike, '1234', @role_gestore),
('Matteo', 'Leoncino', 34431, @email_leoncino, '1234', @role_gestore),
('Giovanni', 'Manager', 43334, @email_giovanni, '1234', @role_gestore),
('Sara', 'Manager', 44443, @email_sara, '1234', @role_gestore);

INSERT IGNORE INTO hotelrooms(
    gestore_id, nome, citta, tipo_camera, servizi,
    distanza_centro, prezzo_notte, nome_immagine, capienza
)
VALUES
((SELECT id FROM users WHERE email = @email_mike), 'Hotel Roma Center', 'Roma', 'Camera Singola', 'WiFi - Colazione inclusa - Piscina', '500m dal centro', 120.00, 'roma 1.jpg', 1),
((SELECT id FROM users WHERE email = @email_mike), 'Hotel Roma Termini', 'Roma', 'Camera Doppia', 'WiFi - Colazione inclusa - Aria condizionata', '300m dal centro', 110.00, 'roma 2.jpg', 2),
((SELECT id FROM users WHERE email = @email_mike), 'Hotel Colosseo View', 'Roma', 'Suite Deluxe', 'Vista Colosseo - WiFi - Spa', '150m dal centro', 280.00, 'roma 3.jpg', 4),
((SELECT id FROM users WHERE email = @email_leoncino), 'Hotel Trastevere Cozy', 'Roma', 'Camera Matrimoniale', 'WiFi - Colazione - Terrazza', '800m dal centro', 130.00, 'roma centro.jpg', 2),
((SELECT id FROM users WHERE email = @email_leoncino), 'Hotel Milano Luxury', 'Milano', 'Suite Deluxe', 'Spa - Piscina - Ristorante', '200m dal centro', 250.00, 'stanza 2.jpg', 4),
((SELECT id FROM users WHERE email = @email_leoncino), 'Hotel Napoli Mare', 'Napoli', 'Camera Matrimoniale', 'Vista mare - WiFi - Colazione', '1km dal centro', 90.00, 'colazione 2.jpg', 2),
((SELECT id FROM users WHERE email = @email_giovanni), 'Hotel Napoli Centro', 'Napoli', 'Camera Singola', 'WiFi - Colazione inclusa', '400m dal centro', 85.00, 'colazione 3.jpg', 1),
((SELECT id FROM users WHERE email = @email_giovanni), 'Hotel Vesuvio Luxury', 'Napoli', 'Suite', 'Vista Vesuvio - Piscina - Spa', '600m dal centro', 220.00, 'stanza 2.jpg', 4),
((SELECT id FROM users WHERE email = @email_sara), 'Hotel Spaccanapoli', 'Napoli', 'Camera Doppia', 'WiFi - Parcheggio - Colazione', '200m dal centro', 95.00, 'colazione 2.jpg', 2),
((SELECT id FROM users WHERE email = @email_sara), 'Hotel Firenze Relax', 'Firenze', 'Camera Doppia', 'WiFi - Giardino - Parcheggio', '300m dal centro', 140.00, 'colazione 3.jpg', 2);

INSERT IGNORE INTO hotel_images(hotel_id, nome_immagine) VALUES
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Roma Center'), 'roma 1.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Roma Center'), 'roma centro.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Roma Center'), 'colazione 1.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Roma Termini'), 'roma 2.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Roma Termini'), 'hotel roma.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Roma Termini'), 'termini.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Roma Termini'), 'stanza 2.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Colosseo View'), 'roma 3.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Colosseo View'), 'colosseo 3.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Colosseo View'), 'stanza roma 1.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Trastevere Cozy'), 'roma centro.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Trastevere Cozy'), 'stanza roma 1.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Trastevere Cozy'), 'colazione 1.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Milano Luxury'), 'stanza 2.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Milano Luxury'), 'hotel roma.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Milano Luxury'), 'colazione 3.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Napoli Mare'), 'colazione 2.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Napoli Mare'), 'stanza 2.jpg'),
((SELECT id FROM hotelrooms WHERE nome = 'Hotel Napoli Mare'), 'colazione 3.jpg');

INSERT IGNORE INTO bookings(
    user_id, hotel_id, check_in, check_out, persone,
    prezzo_totale, extras, punti_usati, stato
)
VALUES
((SELECT id FROM users WHERE email = 'sammy@gmail.com'),
 (SELECT id FROM hotelrooms WHERE nome = 'Hotel Roma Center'),
 '2027-06-10', '2027-06-15', 1, 600.00, '', 0, @stato_confermato),
((SELECT id FROM users WHERE email = 'dany@gmail.com'),
 (SELECT id FROM hotelrooms WHERE nome = 'Hotel Roma Termini'),
 '2027-07-15', '2027-07-18', 2, 370.00,
 'HEALTH_INSURANCE', 1000, @stato_confermato);

INSERT IGNORE INTO app_metadata(id, schema_version) VALUES (1, 1);
