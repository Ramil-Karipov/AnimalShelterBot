-- liquibase formatted sql

-- changeset a.gavrin:1
CREATE TABLE client (
id SERIAL PRIMARY KEY,
name VARCHAR NOT NULL,
phone CHAR(16) NOT NULL,
chat_id BIGINT,
pet_id INTEGER
);

CREATE TABLE pet (
id SERIAL PRIMARY KEY,
name VARCHAR NOT NULL,
birth_date DATE NOT NULL,
is_adopted BOOLEAN
);

CREATE TABLE adaptation (
id SERIAL PRIMARY KEY,
pet_id INTEGER,
client_id INTEGER,
volunteer_id INTEGER,
last_report_date DATE,
finish_date DATE,
is_finished BOOLEAN
);

CREATE TABLE report (
id SERIAL PRIMARY KEY,
report_date DATE NOT NULL,
pet_id INTEGER,
client_id INTEGER,
pet_photo BYTEA,
pet_info TEXT,
is_accepted BOOLEAN
);

-- changeset a.gavrin:2
ALTER TABLE adaptation ADD CONSTRAINT fk_pet_id FOREIGN KEY (pet_id) REFERENCES pet(id);
ALTER TABLE adaptation ADD CONSTRAINT fk_client_id FOREIGN KEY (client_id) REFERENCES client(id);

ALTER TABLE report ADD CONSTRAINT fk_pet_id FOREIGN KEY (pet_id) REFERENCES pet(id);
ALTER TABLE report ADD CONSTRAINT fk_client_id FOREIGN KEY (client_id) REFERENCES client(id);

-- changeset a.gavrin:3
CREATE TABLE volunteer (
id SERIAL PRIMARY KEY,
name VARCHAR NOT NULL,
tg_url VARCHAR
)

-- changeset a.gavrin:4
INSERT INTO volunteer VALUES (1, 'Рамиль', 'https://t.me/omil8');
INSERT INTO volunteer VALUES (2, 'Алексей', 'https://t.me/SilPliS');
INSERT INTO volunteer VALUES (3, 'Игорь', 'https://t.me/Poteriashka163');
INSERT INTO volunteer VALUES (4, 'Константин', 'https://t.me/sfall3003');

-- changeset a.gavrin:5
ALTER TABLE report RENAME COLUMN pet_photo TO pet_photo_path;

-- changeset a.gavrin:6
ALTER TABLE report ALTER COLUMN pet_photo_path TYPE VARCHAR;

-- changeset a.gavrin:7
ALTER TABLE volunteer ADD COLUMN chat_id BIGINT;

-- changeset a.gavrin:8
UPDATE volunteer SET chat_id = 5408065139 WHERE id = 1;
UPDATE volunteer SET chat_id = 355188608 WHERE id = 2;
UPDATE volunteer SET chat_id = 702814928 WHERE id = 3;
UPDATE volunteer SET chat_id = 129474100 WHERE id = 4;