CREATE TABLE doctor
(
    doctor_id 	    SERIAL 		    NOT NULL,
    name			VARCHAR(32)	    NOT NULL,
    surname			VARCHAR(32)		NOT NULL,
    email		    VARCHAR(32)	    NOT NULL,
    PRIMARY KEY (doctor_id),
    UNIQUE (email)
);