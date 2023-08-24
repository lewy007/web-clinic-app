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