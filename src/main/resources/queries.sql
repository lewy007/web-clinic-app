SELECT *
FROM medical_appointment
         INNER JOIN medical_appointment_date
                    ON medical_appointment.medical_appointment_date_id =
                       medical_appointment_date.medical_appointment_date_id
         INNER JOIN patient
                    ON medical_appointment.patient_id = patient.patient_id
         INNER JOIN doctor
                    ON medical_appointment_date.doctor_id = doctor.doctor_id;

--Wynik tego zapytania zawiera dostępność lekarza (doctor_id = 2) na wybraną datę ('2023-09-01 09:00:00').
--Jeśli wynik zawiera wiersz o wartości status równej true, oznacza to, że lekarz jest dostępny w tym czasie.
-- Jednak należy również sprawdzić, czy nie istnieją zapisy w tabeli medical_appointment,
--  które używają tej samej daty (medical_appointment_date_id) i lekarza (doctor_id).
--  Jeśli wynik nie zawiera żadnych wierszy lub zawiera wiersze, ale wszystkie mają status równy true
--  i nie ma pasujących wpisów w tabeli medical_appointment, oznacza to, że lekarz jest dostępny,
--  i pacjent może umówić wizytę.
--Zapytanie to wykorzystuje lewe łączenie (LEFT JOIN), aby uwzględnić dostępność lekarza z tabeli medical_appointment_date
-- oraz sprawdzenie, czy istnieją pasujące wpisy w tabeli medical_appointment.
-- Jeśli takie wpisy istnieją, to znaczy, że lekarz jest już zajęty na tę datę, więc zostaje wykluczony z wyniku.
SELECT
    mad.doctor_id,
    mad.date_time,
    mad.status
FROM
    medical_appointment_date mad
LEFT JOIN
    medical_appointment ma ON mad.medical_appointment_date_id = ma.medical_appointment_date_id
WHERE
    mad.date_time = '2023-08-20 09:00:00' -- Wybrana data i godzina
    AND mad.doctor_id = 2 -- Identyfikator wybranego lekarza
    AND (ma.medical_appointment_id IS NULL OR ma.medical_appointment_date_id IS NULL)
    ORDER BY mad.date_time;
