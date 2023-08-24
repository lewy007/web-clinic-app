-- Wprowadzenie przykładowych danych dla tabeli doctor
INSERT INTO doctor (name, surname, pesel)
VALUES
    ('Jan', 'Kowalski', '12345678901'),
    ('Anna', 'Nowak', '23456789012'),
    ('Marta', 'Wójcik', '34567890123'),
    ('Piotr', 'Zając', '45678901234'),
    ('Katarzyna', 'Lewandowska', '56789012345'),
    ('Tomasz', 'Kwiatkowski', '67890123456'),
    ('Magdalena', 'Szymańska', '78901234567'),
    ('Michał', 'Dąbrowski', '89012345678'),
    ('Alicja', 'Pawlak', '90123456789'),
    ('Robert', 'Wiśniewski', '01234567890');

-- Wprowadzenie przykładowych danych dla tabeli address
INSERT INTO address (country, city, postal_code, address)
VALUES
    ('Polska', 'Warszawa', '00-001', 'ul. Piękna 1'),
    ('Polska', 'Kraków', '30-001', 'ul. Krakowska 2'),
    ('Polska', 'Gdańsk', '80-001', 'ul. Gdańska 3'),
    ('Polska', 'Wrocław', '50-001', 'ul. Wrocławska 4'),
    ('Polska', 'Poznań', '60-001', 'ul. Poznańska 5'),
    ('Polska', 'Łódź', '90-001', 'ul. Łódzka 6'),
    ('Polska', 'Katowice', '40-001', 'ul. Katowicka 7'),
    ('Polska', 'Szczecin', '70-001', 'ul. Szczecińska 8'),
    ('Polska', 'Gdynia', '81-001', 'ul. Gdyńska 9'),
    ('Polska', 'Bydgoszcz', '85-001', 'ul. Bydgoska 10');

-- Wprowadzenie przykładowych danych dla tabeli patient
INSERT INTO patient (name, surname, phone, email, address_id)
VALUES
    ('Agnieszka', 'Nowak', '123456789', 'agnieszka@example.com', 1),
    ('Michał', 'Kowalski', '987654321', 'michal@example.com', 2),
    ('Magdalena', 'Szymańska', '555666777', 'magdalena@example.com', 3),
    ('Kamil', 'Wójcik', '111222333', 'kamil@example.com', 4),
    ('Monika', 'Zając', '999888777', 'monika@example.com', 5),
    ('Paweł', 'Lewandowski', '333222111', 'pawel@example.com', 6),
    ('Natalia', 'Kwiatkowska', '444555666', 'natalia@example.com', 7),
    ('Krzysztof', 'Dąbrowski', '777888999', 'krzysztof@example.com', 8),
    ('Weronika', 'Pawlak', '666555444', 'weronika@example.com', 9),
    ('Marcin', 'Wiśniewski', '222333444', 'marcin@example.com', 10);

-- Wprowadzenie przykładowych danych dla tabeli medical_appointment_date
INSERT INTO medical_appointment_date (date_time, status, doctor_id)
VALUES
    ('2023-08-20 10:00', true, 1),
    ('2023-08-21 14:30', true, 2),
    ('2023-08-22 09:45', false, 3),
    ('2023-08-23 11:15', true, 4),
    ('2023-08-24 16:00', false, 5),
    ('2023-08-25 13:30', true, 6),
    ('2023-08-26 08:00', true, 7),
    ('2023-08-27 17:45', false, 8),
    ('2023-08-28 12:15', true, 9),
    ('2023-08-29 10:30', false, 10);

-- Wprowadzenie przykładowych danych dla tabeli medical_appointment
INSERT INTO medical_appointment (doctor_note, patient_id, medical_appointment_date_id)
VALUES
    ('Badanie ogólne.', 1, 1),
    ('Konsultacja przed operacją.', 2, 2),
    ('Skierowanie na dodatkowe badania.', 3, 3),
    ('Konsultacja dermatologiczna.', 4, 4),
    ('Badanie okulistyczne.', 5, 5),
    ('Badanie kardiologiczne.', 6, 6),
    ('Konsultacja neurologiczna.', 7, 7),
    ('Skierowanie na rehabilitację.', 8, 8),
    ('Badanie ginekologiczne.', 9, 9),
    ('Skierowanie na badania hormonalne.', 10, 10);
