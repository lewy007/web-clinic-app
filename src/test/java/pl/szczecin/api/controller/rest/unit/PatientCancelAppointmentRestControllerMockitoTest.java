package pl.szczecin.api.controller.rest.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szczecin.api.controller.rest.PatientCancelRestController;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.util.EntityFixtures;


@ExtendWith(MockitoExtension.class)
class PatientCancelAppointmentRestControllerMockitoTest {

    @Mock
    private MedicalAppointmentService medicalAppointmentService;
    @Mock
    private MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    @Mock
    private PatientService patientService;
    @Mock
    private PatientMapper patientMapper;
    @Mock
    private MedicalAppointmentMapper medicalAppointmentMapper;


    @InjectMocks
    private PatientCancelRestController patientCancelRestController;


    @BeforeEach
    public void setUp() {
        System.out.println("checking for nulls");
        Assertions.assertNotNull(medicalAppointmentService);
        Assertions.assertNotNull(medicalAppointmentRequestMapper);
        Assertions.assertNotNull(patientService);
        Assertions.assertNotNull(patientMapper);
        Assertions.assertNotNull(medicalAppointmentMapper);
    }


    @Test
    @DisplayName("GET - That method should return correct medical appointment schedule for selected patient")
    void shouldReturnCorrectMedicalAppointmentScheduleForPatient() {
        // Given
        String patientEmail = "anna.nowak@clinic.pl";
        PatientHistory somePatientHistory = EntityFixtures.somePatientHistory();
        PatientHistoryDTO expectedPatientHistoryDTO = EntityFixtures.somePatientHistoryDTO();
        PatientHistoryDTO notExpectedPatientHistoryDTO = EntityFixtures.somePatientHistoryDTO2();


        Mockito.when(patientService.findPatientScheduleByEmail(patientEmail)).thenReturn(somePatientHistory);
        Mockito.when(patientMapper.map(Mockito.any(PatientHistory.class)))
                .thenReturn(expectedPatientHistoryDTO);

        // When
        PatientHistoryDTO result = patientCancelRestController.patientMedicalAppointmentSchedule(patientEmail);

        // Then
        Assertions.assertEquals(result, expectedPatientHistoryDTO);
        //lista z inna iloscia elementow nie jest taka sama, wiec test przechodzi
        Assertions.assertNotEquals(result, notExpectedPatientHistoryDTO);
    }


    @Test
    @DisplayName("DELETE - That method should delete correctly a medical appointment " +
            "and return data about cancelled medical appointment")
    void shouldDeleteCorrectlyMedicalAppointmentAndReturnDeletedMedicalAppointment() {
        // Given
        String doctorEmail = "doctor.doctor@clinic.pl";
        String patientEmail = "patient.patient@clinic.pl";
        String medicalAppointmentDate = "2023-11-17 10:00:00";

        MedicalAppointmentDTO expectedMedicalAppointmentDTO = EntityFixtures.someMedicalAppointmentDTO1();
        MedicalAppointmentDTO notExpextedMedicalAppointmentDTO = EntityFixtures.someMedicalAppointmentDTO2();

        MedicalAppointmentRequest request = EntityFixtures.someMedicalAppointmentRequest();
        MedicalAppointment someMedicalAppointment = EntityFixtures.someMedicalAppointment1();

        Mockito.when(medicalAppointmentRequestMapper.map(Mockito.any(MedicalAppointmentRequestDTO.class)))
                .thenReturn(request);
        Mockito.when(medicalAppointmentService.cancelAppointment(request)).thenReturn(someMedicalAppointment);
        Mockito.when(medicalAppointmentMapper.map(Mockito.any(MedicalAppointment.class)))
                .thenReturn(expectedMedicalAppointmentDTO);

        // When
        MedicalAppointmentDTO result = patientCancelRestController
                .cancelAppointment(patientEmail, doctorEmail, medicalAppointmentDate);

        // Then
        Assertions.assertEquals(result, expectedMedicalAppointmentDTO);
        //lista z inna iloscia elementow nie jest taka sama, wiec test przechodzi
        Assertions.assertNotEquals(result, notExpextedMedicalAppointmentDTO);
    }

}