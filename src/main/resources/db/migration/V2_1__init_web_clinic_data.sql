-- Wprowadzenie przykładowych danych dla tabeli doctor
INSERT INTO doctor (name, surname, email)
VALUES
    ('Agata', 'Torbe', 'agata.torbe@clinic.pl'),
    ('Renata', 'Kruk', 'renata.kruk@clinic.pl'),
    ('Danuta', 'Wasilewska', 'danuta.wasilewska@clinic.pl');

-- Wprowadzenie przykładowych danych dla tabeli address
INSERT INTO address (country, city, postal_code, address)
VALUES
    ('Polska', 'Warszawa', '00-001', 'ul. Piękna 1'),
    ('Polska', 'Kraków', '30-001', 'ul. Krakowska 2'),
    ('Polska', 'Gdańsk', '80-001', 'ul. Gdańska 3'),
    ('Polska', 'Wrocław', '50-001', 'ul. Wrocławska 4'),
    ('Polska', 'Poznań', '60-001', 'ul. Poznańska 5');

-- Wprowadzenie przykładowych danych dla tabeli patient
INSERT INTO patient (name, surname, phone, email, address_id)
VALUES
    ('Agnieszka', 'Nowak', '123456789', 'agnieszka.nowak@clinic.pl', 1),
    ('Michał', 'Kowalski', '987654321', 'michal.kowalski@clinic.pl', 2),
    ('Magdalena', 'Szymańska', '555666777', 'magdalena.szymanska@clinic.pl', 3),
    ('Kamil', 'Wójcik', '111222333', 'kamil.wojcik@clinic.pl', 4),
    ('Monika', 'Zając', '999888777', 'monika.zajac@clinic.pl', 5);

-- Wprowadzenie przykładowych danych dla tabeli medical_appointment_date
INSERT INTO medical_appointment_date (date_time, status, doctor_id)
VALUES
    ('2023-08-20 09:00', false, 1),
    ('2023-08-20 09:30', true, 1),
    ('2023-08-20 10:00', true, 1),
    ('2023-08-20 10:30', true, 1),
    ('2023-08-20 11:00', true, 1),
    ('2023-08-20 11:30', true, 1),
    ('2023-08-20 12:00', false, 2),
    ('2023-08-20 12:30', true, 2),
    ('2023-08-20 13:00', true, 2),
    ('2023-08-20 13:30', true, 2),
    ('2023-08-20 14:00', true, 2),
    ('2023-08-20 14:30', true, 2),
    ('2023-08-21 09:00', false, 1),
    ('2023-08-21 09:30', true, 1),
    ('2023-08-21 10:00', true, 1),
    ('2023-08-21 10:30', true, 1),
    ('2023-08-21 11:00', true, 1),
    ('2023-08-21 11:30', true, 1),
    ('2023-08-21 12:00', false, 2),
    ('2023-08-21 12:30', true, 2),
    ('2023-08-21 13:00', true, 2),
    ('2023-08-21 13:30', true, 2),
    ('2023-08-21 14:00', true, 2),
    ('2023-08-21 14:30', true, 2),
    ('2023-08-22 09:00', false, 2),
    ('2023-08-22 09:30', true, 2),
    ('2023-08-22 10:00', true, 2),
    ('2023-08-22 10:30', true, 2),
    ('2023-08-22 11:00', true, 2),
    ('2023-08-22 11:30', true, 2),
    ('2023-08-22 12:00', false, 1),
    ('2023-08-22 12:30', true, 1),
    ('2023-08-22 13:00', true, 1),
    ('2023-08-22 13:30', true, 1),
    ('2023-08-22 14:00', true, 1),
    ('2023-08-22 14:30', true, 1),
    ('2023-08-23 09:00', false, 2),
    ('2023-08-23 09:30', true, 2),
    ('2023-08-23 10:00', true, 2),
    ('2023-08-23 10:30', true, 2),
    ('2023-08-23 11:00', true, 2),
    ('2023-08-23 11:30', true, 2),
    ('2023-08-23 12:00', false, 1),
    ('2023-08-23 12:30', true, 1),
    ('2023-08-23 13:00', true, 1),
    ('2023-08-23 13:30', true, 1),
    ('2023-08-23 14:00', true, 1),
    ('2023-08-23 14:30', true, 1),
    ('2024-10-23 09:00', true, 1),
    ('2024-10-23 09:00', true, 2),
    ('2024-10-23 09:00', true, 3),
    ('2024-10-23 09:30', true, 1),
    ('2024-10-23 09:30', true, 2),
    ('2024-10-23 09:30', true, 3),
    ('2024-10-23 10:00', true, 1),
    ('2024-10-23 10:00', true, 2),
    ('2024-10-23 10:00', true, 3),
    ('2024-10-23 10:30', true, 1),
    ('2024-10-23 10:30', true, 2),
    ('2024-10-23 10:30', true, 3),
    ('2024-10-23 11:00', true, 1),
    ('2024-10-23 11:00', true, 2),
    ('2024-10-23 11:00', true, 3),
    ('2024-10-23 11:30', true, 1),
    ('2024-10-23 11:30', true, 2),
    ('2024-10-23 11:30', true, 3),
    ('2024-10-23 12:00', true, 1),
    ('2024-10-23 12:00', true, 2),
    ('2024-10-23 12:00', true, 3);

-- Wprowadzenie przykładowych danych dla tabeli medical_appointment
INSERT INTO medical_appointment (doctor_note, patient_id, medical_appointment_date_id)
VALUES
    ('Badanie ogólne.', 1, 1),
    ('Konsultacja przed operacją.', 2, 2),
    ('Skierowanie na dodatkowe badania.', 3, 3),
    ('Konsultacja dermatologiczna.', 4, 4),
    ('Badanie okulistyczne.', 5, 5),
    ('Badanie kardiologiczne.', 1, 6),
    ('Konsultacja neurologiczna.', 2, 7),
    ('Skierowanie na rehabilitację.', 3, 8),
    ('Badanie ginekologiczne.', 4, 9),
    ('Skierowanie na badania hormonalne.', 5, 10);