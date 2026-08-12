-- Migrazione dallo schema 1 allo schema 2: approvazione prima del pagamento.

ALTER TABLE bookings
    MODIFY stato ENUM(
        'PENDING_APPROVAL', 'CONFIRMED', 'REJECTED', 'CANCELLED'
    ) NOT NULL DEFAULT 'PENDING_APPROVAL';

ALTER TABLE bookings
    ADD COLUMN payment_token VARCHAR(100) NOT NULL DEFAULT '',
    ADD COLUMN card_holder VARCHAR(120) NOT NULL DEFAULT '',
    ADD COLUMN card_last_four CHAR(4) NOT NULL DEFAULT '',
    ADD COLUMN payment_completed BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE bookings
SET payment_token = CONCAT('legacy-', id),
    card_holder = 'Pagamento precedente',
    card_last_four = '0000',
    payment_completed = TRUE
WHERE stato = 'CONFIRMED';

UPDATE app_metadata SET schema_version = 2 WHERE id = 1;
