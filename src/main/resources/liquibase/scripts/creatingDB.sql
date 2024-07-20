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
pet_photo_path VARCHAR,
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
tg_url VARCHAR,
chat_id BIGINT
);