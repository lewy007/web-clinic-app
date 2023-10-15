package pl.szczecin.util;

import lombok.experimental.UtilityClass;
import pl.szczecin.api.dto.DoctorDTO;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.domain.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@UtilityClass
public class EntityFixtures {


    public static Doctor someDoctor1() {
        return Doctor.builder()
                .name("Edyta")
                .surname("Kowalska")
                .email("edyta.kowalska@clinic.pl")
                .build();
    }

    public static Doctor someDoctor2() {
        return Doctor.builder()
                .name("Alina")
                .surname("Nowak")
                .email("alina.nowak@clinic.pl")
                .build();
    }

    public static Doctor someDoctor3() {
        return Doctor.builder()
                .name("Malwina")
                .surname("Malinowska")
                .email("malwina.malinowska@clinic.pl")
                .build();
    }

    public static DoctorDTO someDoctorDTO1() {
        return DoctorDTO.builder()
                .name("Edyta")
                .surname("Kowalska")
                .email("edyta.kowalska@clinic.pl")
                .build();
    }

    public static DoctorDTO someDoctorDTO2() {
        return DoctorDTO.builder()
                .name("Alina")
                .surname("Nowak")
                .email("alina.nowak@clinic.pl")
                .build();
    }

    public static DoctorDTO someDoctorDTO3() {
        return DoctorDTO.builder()
                .name("Malwina")
                .surname("Malinowska")
                .email("malwina.malinowska@clinic.pl")
                .build();
    }

    public static Patient somePatient1() {
        return Patient.builder()
                .name("Janina")
                .surname("Pacjentowska")
                .phone("+48 372 54 56")
                .email("janina.pacjentowska@clinic.pl")
                .build();
    }

    public static MedicalAppointment someMedicalAppointment() {
        return MedicalAppointment.builder()
                .medicalAppointmentDate(someMedicalAppointmentDate1())
                .doctorNote("some note to test")
                .build();
    }

    public static MedicalAppointment someMedicalAppointment1() {
        return MedicalAppointment.builder()
                .doctorNote("some note 1")
                .medicalAppointmentDate(someMedicalAppointmentDate1())
                .build();
    }

    public static MedicalAppointment someMedicalAppointment2() {
        return MedicalAppointment.builder()
                .doctorNote("some note 2")
                .medicalAppointmentDate(someMedicalAppointmentDate2())
                .build();
    }

    public static MedicalAppointment someMedicalAppointment3() {
        return MedicalAppointment.builder()
                .doctorNote("some note 3")
                .medicalAppointmentDate(someMedicalAppointmentDate3())
                .build();
    }

    public static MedicalAppointmentDTO someMedicalAppointmentDTO1() {
        return MedicalAppointmentDTO.builder()
                .doctorNote("some note 1")
                .medicalAppointmentDate(someMedicalAppointmentDate1())
                .build();
    }

    public static MedicalAppointmentDTO someMedicalAppointmentDTO2() {
        return MedicalAppointmentDTO.builder()
                .doctorNote("some note 2")
                .medicalAppointmentDate(someMedicalAppointmentDate2())
                .build();
    }

    public static MedicalAppointmentDTO someMedicalAppointmentDTO3() {
        return MedicalAppointmentDTO.builder()
                .doctorNote("some note 3")
                .medicalAppointmentDate(someMedicalAppointmentDate3())
                .build();
    }

    public static MedicalAppointmentDate someMedicalAppointmentDate1() {
        return MedicalAppointmentDate.builder()
                .medicalAppointmentDateId(1)
                .dateTime(OffsetDateTime.of(2023, 11, 15,
                        10, 0, 0, 0, ZoneOffset.UTC))
                .status(true)
                .doctor(someDoctor1())
                .build();
    }

    public static MedicalAppointmentDate someMedicalAppointmentDate2() {
        return MedicalAppointmentDate.builder()
                .medicalAppointmentDateId(2)
                .dateTime(OffsetDateTime.of(2023, 11, 16,
                        10, 0, 0, 0, ZoneOffset.UTC))
                .status(true)
                .doctor(someDoctor2())
                .build();
    }

    public static MedicalAppointmentDate someMedicalAppointmentDate3() {
        return MedicalAppointmentDate.builder()
                .medicalAppointmentDateId(3)
                .dateTime(OffsetDateTime.of(2023, 11, 17,
                        10, 0, 0, 0, ZoneOffset.UTC))
                .status(true)
                .doctor(someDoctor3())
                .build();
    }

    public static MedicalAppointmentDateDTO someMedicalAppointmentDateDTO1() {
        return MedicalAppointmentDateDTO.builder()
                .dateTime("2023-11-15 10:00:00")
                .status(true)
                .build();
    }

    public static MedicalAppointmentDateDTO someMedicalAppointmentDateDTO2() {
        return MedicalAppointmentDateDTO.builder()
                .dateTime("2023-11-16 10:00:00")
                .status(true)
                .build();
    }

    public static MedicalAppointmentDateDTO someMedicalAppointmentDateDTO3() {
        return MedicalAppointmentDateDTO.builder()
                .dateTime("2023-11-17 10:00:00")
                .status(true)
                .build();
    }

    public static MedicalAppointmentRequest someMedicalAppointmentRequest() {
        return MedicalAppointmentRequest.builder()
                .patientEmail("patient@example.com")
                .patientName("Piotr")
                .patientSurname("Piotrowski")
                .medicalAppointmentDate(
                        OffsetDateTime.of(2022, 8, 15,
                                0, 0, 0, 0, ZoneOffset.UTC))
                .doctorEmail("doctor@clinic.pl")
                .build();
    }

    public static PatientHistory.MedicalAppointment someMedicalAppointmentFromPatientHistory1() {
        return PatientHistory.MedicalAppointment.builder()
                .doctorName("nameTest1")
                .doctorSurname("surnameTest1")
                .dateTime(
                        OffsetDateTime.of(2022, 8, 15,
                                0, 0, 0, 0, ZoneOffset.UTC))
                .doctorNote("some test note1")
                .build();
    }

    public static PatientHistory.MedicalAppointment someMedicalAppointmentFromPatientHistory2() {
        return PatientHistory.MedicalAppointment.builder()
                .doctorName("nameTest2")
                .doctorSurname("surnameTest2")
                .dateTime(
                        OffsetDateTime.of(2023, 9, 16,
                                0, 0, 0, 0, ZoneOffset.UTC))
                .doctorNote("some test note1")
                .build();
    }

    public static PatientHistory somePatientHistory() {
        return PatientHistory.builder()
                .patientEmail("patient@example.com")
                .medicalAppointments(List.of(
                        someMedicalAppointmentFromPatientHistory1(),
                        someMedicalAppointmentFromPatientHistory2())
                )
                .build();
    }

    public static PatientHistoryDTO somePatientHistoryDTO() {
        return PatientHistoryDTO.builder()
                .patientEmail("patient@example.com")
                .medicalAppointments(List.of(
                        PatientHistoryDTO.MedicalAppointmentDTO.builder()
                                .doctorName("nameTest1")
                                .doctorSurname("surnameTest1")
                                .dateTime("2023-11-17 10:00:00")
                                .doctorNote("some test note1")
                                .build(),
                        PatientHistoryDTO.MedicalAppointmentDTO.builder()
                                .doctorName("nameTest2")
                                .doctorSurname("surnameTest2")
                                .dateTime("2023-11-18 10:00:00")
                                .doctorNote("some test note2")
                                .build())
                )
                .build();
    }

}
