ALTER TABLE doctor
ADD COLUMN user_id INT,
ADD FOREIGN KEY (user_id) REFERENCES web_clinic_user (user_id);

ALTER TABLE patient
ADD COLUMN user_id INT,
ADD FOREIGN KEY (user_id) REFERENCES web_clinic_user (user_id);

insert into web_clinic_user (user_id, user_name, email, password, active) values (1, 'agata.torbe', 'agata.torbe@clinic.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into web_clinic_user (user_id, user_name, email, password, active) values (2, 'renata.kruk', 'renata.kruk@clinic.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into web_clinic_user (user_id, user_name, email, password, active) values (3, 'danuta.wasilewska', 'danuta.wasilewska@clinic.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);

insert into web_clinic_user (user_id, user_name, email, password, active) values (4, 'agnieszka.nowak', 'agnieszka.nowak@clinic.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into web_clinic_user (user_id, user_name, email, password, active) values (5, 'michał.kowalski', 'michal.kowalski@clinic.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into web_clinic_user (user_id, user_name, email, password, active) values (6, 'magdalena.szymańska', 'magdalena.szymanska@clinic.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into web_clinic_user (user_id, user_name, email, password, active) values (7, 'kamil.wójcik', 'kamil.wojcik@clinic.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);
insert into web_clinic_user (user_id, user_name, email, password, active) values (8, 'monika.zając', 'monika.zajac@clinic.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);

insert into web_clinic_user (user_id, user_name, email, password, active) values (9, 'test.user', 'test.user@clinic.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true);

UPDATE doctor SET user_id = 1 WHERE email = 'agata.torbe@clinic.pl';
UPDATE doctor SET user_id = 2 WHERE email = 'renata.kruk@clinic.pl';
UPDATE doctor SET user_id = 3 WHERE email = 'danuta.wasilewska@clinic.pl';

UPDATE patient SET user_id = 4 WHERE email = 'agnieszka.nowak@clinic.pl';
UPDATE patient SET user_id = 5 WHERE email = 'michal.kowalski@clinic.pl';
UPDATE patient SET user_id = 6 WHERE email = 'magdalena.szymanska@clinic.pl';
UPDATE patient SET user_id = 7 WHERE email = 'kamil.wojcik@clinic.pl';
UPDATE patient SET user_id = 8 WHERE email = 'monika.zajac@clinic.pl';

insert into web_clinic_role (role_id, role) values (1, 'DOCTOR'), (2, 'PATIENT'), (3, 'REST_API');

insert into web_clinic_user_role (user_id, role_id) values (1, 1), (2, 1), (3, 1);
insert into web_clinic_user_role (user_id, role_id) values (4, 2), (5, 2), (6, 2), (7, 2), (8, 2);
insert into web_clinic_user_role (user_id, role_id) values (9, 3);

ALTER TABLE doctor
ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE patient
ALTER COLUMN user_id SET NOT NULL;