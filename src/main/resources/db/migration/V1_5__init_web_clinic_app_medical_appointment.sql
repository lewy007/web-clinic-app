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