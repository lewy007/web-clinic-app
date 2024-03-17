package pl.szczecin.util;

import lombok.experimental.UtilityClass;
import pl.szczecin.api.dto.*;
import pl.szczecin.domain.*;
import pl.szczecin.infrastructure.database.entity.*;
import pl.szczecin.infrastructure.security.UserEntity;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public static DoctorsDTO someDoctorListDTO() {
        return DoctorsDTO.builder()
                .doctorsDTO(
                        List.of(
                                someDoctorDTO1(),
                                someDoctorDTO2(),
                                someDoctorDTO3())
                )
                .build();
    }

    public static Optional<DoctorEntity> someDoctorEntity1() {
        return Optional.of(DoctorEntity.builder()
                .doctorId(3)
                .name("Danuta")
                .surname("Wasilewska")
                .email("danuta.wasilewska@clinic.pl")
                .build());
    }

    public static DoctorEntity someDoctorEntity2() {
        return DoctorEntity.builder()
                .doctorId(4)
                .name("Wanda")
                .surname("Nowak")
                .email("wanda.nowak@clinic.pl")
                .build();
    }

    public static DoctorEntity someDoctorEntity3() {
        return DoctorEntity.builder()
                .doctorId(5)
                .name("Lucyna")
                .surname("Tarka")
                .email("lucyna.tarka@clinic.pl")
                .build();
    }

    public static Patient somePatient1() {
        return Patient.builder()
                .name("Janina")
                .surname("Pacjentowska")
                .phone("+48 372 54 56")
                .email("janina.pacjentowska@clinic.pl")
                .userEntity(someUserEntity1())
                .build();
    }

    public static Patient somePatient2() {
        return Patient.builder()
                .name("Ryszarda")
                .surname("Konopnicka")
                .phone("+48 348 21 66")
                .email("ryszarda.konopnicka@clinic.pl")
                .userEntity(someUserEntity2())
                .build();
    }

    public static Patient somePatient3() {
        return Patient.builder()
                .name("Angelika")
                .surname("Czarna")
                .phone("+48 267 16 69")
                .email("angelika.czarna@clinic.pl")
                .userEntity(someUserEntity2())
                .build();
    }

    public static PatientDTO somePatientDTO1() {
        return PatientDTO.builder()
                .name("Janina")
                .surname("Pacjentowska")
                .phone("+48 372 54 56")
                .email("janina.pacjentowska@clinic.pl")
                .build();
    }

    public static PatientDTO somePatientDTO2() {
        return PatientDTO.builder()
                .name("Ryszarda")
                .surname("Konopnicka")
                .phone("+48 348 21 66")
                .email("ryszarda.konopnicka@clinic.pl")
                .build();
    }

    public static PatientDTO somePatientDTO3() {
        return PatientDTO.builder()
                .name("Angelika")
                .surname("Czarna")
                .phone("+48 267 16 69")
                .email("angelika.czarna@clinic.pl")
                .build();
    }

    public static PatientsDTO somePatientsDTO() {
        return PatientsDTO.builder()
                .patientsDTO(
                        List.of(
                                somePatientDTO1(),
                                somePatientDTO2(),
                                somePatientDTO3())
                )
                .build();
    }

    public static PatientEntity somePatientEntity1() {

        return PatientEntity.builder()
                .name("Janina")
                .surname("Pacjentowska")
                .phone("+48 372 54 56")
                .email("janina.pacjentowska@clinic.pl")
                .address(AddressEntity.builder()
                        .country("Poland")
                        .city("Szczecin")
                        .postalCode("50-200")
                        .address("ul. Jana Matejki 13 ")
                        .build())
                .medicalAppointmentDetails(Set.of(MedicalAppointmentEntity.builder()
                        .medicalAppointmentId(1)
                        .medicalAppointmentDateEntity(MedicalAppointmentDateEntity.builder()
                                .medicalAppointmentDateId(1)
                                .dateTime(OffsetDateTime.of(2023, 11, 16,
                                        10, 0, 0, 0, ZoneOffset.UTC))
                                .build())
                        .build()))
                .userEntity(UserEntity.builder()
                        .userName("Janina")
                        .email("janina.pacjentowska@clinic.pl")
                        .password("test")
                        .active(true)
                        .build())
                .build();
    }

    public static PatientEntity somePatientEntity2() {

        return PatientEntity.builder()
                .name("Alina")
                .surname("Laskowska")
                .phone("+48 201 22 37")
                .email("alina.laskowska@clinic.pl")
                .address(AddressEntity.builder()
                        .country("Poland")
                        .city("Warszawa")
                        .postalCode("90-300")
                        .address("ul. Woronicza 11 ")
                        .build())
                .userEntity(UserEntity.builder()
                        .userName("Alina")
                        .email("alina.laskowska@clinic.pl")
                        .password("test")
                        .active(true)
                        .build())
                .build();
    }

    public static Optional<PatientEntity> somePatientEntity3() {

        return Optional.of(PatientEntity.builder()
                .name("Wanda")
                .surname("Zaorska")
                .phone("+48 345 14 21")
                .email("wanda.zaorska@clinic.pl")
                .address(AddressEntity.builder()
                        .country("Poland")
                        .city("Legnica")
                        .postalCode("10-120")
                        .address("ul. Legnicka 19 ")
                        .build())
                .userEntity(UserEntity.builder()
                        .userName("Wanda")
                        .email("wanda.zaorska@clinic.pl")
                        .password("test")
                        .active(true)
                        .build())
                .build());
    }

    public static UserEntity someUserEntity1() {
        return UserEntity.builder()
                .userId(1)
                .build();
    }

    public static UserEntity someUserEntity2() {
        return UserEntity.builder()
                .userId(2)
                .build();
    }

    public static UserEntity someUserEntity3() {
        return UserEntity.builder()
                .userId(3)
                .build();
    }

    public static MedicalAppointmentEntity someMedicalAppointmentEntity1() {
        return MedicalAppointmentEntity.builder()
                .doctorNote("some doctor note 1")
                .patient(somePatientEntity1())
                .build();
    }

    public static MedicalAppointmentEntity someMedicalAppointmentEntity2() {
        return MedicalAppointmentEntity.builder()
                .doctorNote("some doctor note 2")
                .patient(somePatientEntity2())
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
                .medicalAppointmentId(1)
                .doctorNote("some note 1")
                .patient(somePatient1())
                .medicalAppointmentDate(
                        someMedicalAppointmentDate1())
                .build();
    }

    public static MedicalAppointment someMedicalAppointment2() {
        return MedicalAppointment.builder()
                .medicalAppointmentId(2)
                .doctorNote("some note 2")
                .medicalAppointmentDate(
                        someMedicalAppointmentDate2())
                .build();
    }

    public static MedicalAppointment someMedicalAppointment3() {
        return MedicalAppointment.builder()
                .medicalAppointmentId(3)
                .doctorNote("some note 3")
                .medicalAppointmentDate(
                        someMedicalAppointmentDate3()
                )
                .build();
    }

    public static MedicalAppointmentDTO someMedicalAppointmentDTO1() {
        return MedicalAppointmentDTO.builder()
                .doctorNote("some note 1")
                .dateTime("2023-11-16 10:00:00")
                .patientName("Jan")
                .patientSurname("Kowalski")
                .build();
    }

    public static MedicalAppointmentDTO someMedicalAppointmentDTO2() {
        return MedicalAppointmentDTO.builder()
                .doctorNote("some note 2")
                .dateTime("2023-11-17 10:00:00")
                .patientName("Jan")
                .patientSurname("Kowalski")
                .build();
    }

    public static MedicalAppointmentDTO someMedicalAppointmentDTO3() {
        return MedicalAppointmentDTO.builder()
                .doctorNote("some note 3")
                .dateTime("2023-11-18 10:00:00")
                .patientName("Jan")
                .patientSurname("Kowalski")
                .build();
    }

    public static MedicalAppointmentsDTO someMedicalAppointmentsDTO() {
        return MedicalAppointmentsDTO.builder()
                .medicalAppointmentsDTO(
                        List.of(
                                someMedicalAppointmentDTO1(),
                                someMedicalAppointmentDTO2(),
                                someMedicalAppointmentDTO3()
                        ))
                .build();
    }

    public static MedicalAppointmentDateEntity someMedicalAppointmentDateEntity1() {
        return MedicalAppointmentDateEntity.builder()
                .medicalAppointmentDateId(1)
                .dateTime(OffsetDateTime.of(2025, 11, 15,
                        10, 0, 0, 0, ZoneOffset.UTC))
                .status(true)
                .medicalAppointmentEntity(someMedicalAppointmentEntity1())
                .doctor(someDoctorEntity2())
                .build();
    }

    public static MedicalAppointmentDateEntity someMedicalAppointmentDateEntity2() {
        return MedicalAppointmentDateEntity.builder()
                .medicalAppointmentDateId(2)
                .dateTime(OffsetDateTime.of(2025, 12, 16,
                        12, 0, 0, 0, ZoneOffset.UTC))
                .status(true)
                .medicalAppointmentEntity(someMedicalAppointmentEntity2())
                .doctor(someDoctorEntity3())
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
                .patientEmail("piotr.piotrowski@clinic.pl")
                .doctorNote("some added note to test")
                .patientName("Piotr")
                .patientSurname("Piotrowski")
                .medicalAppointmentDate(
                        OffsetDateTime.of(2022, 8, 15,
                                0, 0, 0, 0, ZoneOffset.UTC))
                .doctorEmail("edyta.kowalska@clinic.pl")
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
                .doctorNote("some test note2")
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

    public static PatientHistoryDTO somePatientHistoryDTO2() {
        return PatientHistoryDTO.builder()
                .patientEmail("patient.another@example.com")
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
