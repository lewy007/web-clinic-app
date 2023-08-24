DROP TABLE IF EXISTS medical_appointment CASCADE;
DROP TABLE IF EXISTS medical_appointment_date CASCADE;
DROP TABLE IF EXISTS patient CASCADE;
DROP TABLE IF EXISTS address CASCADE;
DROP TABLE IF EXISTS doctor CASCADE;

CREATE TABLE doctor
(
    doctor_id 	    SERIAL 		    NOT NULL,
    name			VARCHAR(32)	    NOT NULL,
    surname			VARCHAR(32)		NOT NULL,
    pesel		    VARCHAR(32)	    NOT NULL,
    PRIMARY KEY (doctor_id),
    UNIQUE (pesel)
);

CREATE TABLE address
(
    address_id      SERIAL          NOT NULL,
    country         VARCHAR(32)     NOT NULL,
    city            VARCHAR(32)     NOT NULL,
    postal_code     VARCHAR(32)     NOT NULL,
    address         VARCHAR(32)     NOT NULL,
    PRIMARY KEY (address_id)
);

CREATE TABLE patient
(
    patient_id      SERIAL          NOT NULL,
    name            VARCHAR(32)     NOT NULL,
    surname         VARCHAR(32)     NOT NULL,
    phone           VARCHAR(32)     NOT NULL,
    email           VARCHAR(32)     NOT NULL,
    address_id      INT             NOT NULL,
    PRIMARY KEY (patient_id),
    UNIQUE (email),
    CONSTRAINT fk_patient_address
        FOREIGN KEY (address_id)
            REFERENCES address (address_id)
);

CREATE TABLE medical_appointment_date
(
    medical_appointment_date_id      SERIAL                     NOT NULL,
    date_time                        TIMESTAMP WITH TIME ZONE   NOT NULL,
    status                           BOOLEAN                    NOT NULL,
    doctor_id                        INT                        NOT NULL,
    PRIMARY KEY (medical_appointment_date_id),
    CONSTRAINT fk_medical_appointment_date_doctor
        FOREIGN KEY (doctor_id)
            REFERENCES doctor (doctor_id)
);

CREATE TABLE medical_appointment
(
    medical_appointment_id          SERIAL      NOT NULL,
    doctor_note                     TEXT,
    patient_id                      INT         NOT NULL,
    medical_appointment_date_id     INT         NOT NULL,
    PRIMARY KEY (medical_appointment_id),
    CONSTRAINT fk_medical_appointment_patient
        FOREIGN KEY (patient_id)
            REFERENCES patient (patient_id),
    CONSTRAINT fk_medical_appointment_medical_appointment_date
        FOREIGN KEY (medical_appointment_date_id)
            REFERENCES medical_appointment_date (medical_appointment_date_id)
);