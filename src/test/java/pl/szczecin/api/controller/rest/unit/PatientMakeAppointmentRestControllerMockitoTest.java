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
import pl.szczecin.api.controller.rest.PatientMakeAppointmentRestController;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.util.EntityFixtures;


@ExtendWith(MockitoExtension.class)
class PatientMakeAppointmentRestControllerMockitoTest {

    @Mock
    private MedicalAppointmentService medicalAppointmentService;
    @Mock
    private MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    @Mock
    private MedicalAppointmentMapper medicalAppointmentMapper;


    @InjectMocks
    private PatientMakeAppointmentRestController patientMakeAppointmentRestController;


    @BeforeEach
    public void setUp() {
        System.out.println("checking for nulls");
        Assertions.assertNotNull(medicalAppointmentService);
        Assertions.assertNotNull(medicalAppointmentRequestMapper);
        Assertions.assertNotNull(medicalAppointmentMapper);
    }


    @Test
    @DisplayName("POST - That method should add correctly a medical appointment and return medical appointment.")
    void shouldAddCorrectlyMedicalAppointmentAndReturnMedicalAppointment() {
        // Given
        MedicalAppointmentRequest request = EntityFixtures.someMedicalAppointmentRequest();
        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment1();
        MedicalAppointment notExpectedMedicalAppointment = EntityFixtures.someMedicalAppointment2();


        Mockito.when(medicalAppointmentService.makeAppointment(request)).thenReturn(expectedMedicalAppointment);

        // When
        MedicalAppointment result = medicalAppointmentService.makeAppointment(request);

        // Then
        Assertions.assertEquals(result, expectedMedicalAppointment);
        //lista z inna iloscia elementow nie jest taka sama, wiec test przechodzi
        Assertions.assertNotEquals(result, notExpectedMedicalAppointment);
    }

    @Test
    @DisplayName("POST - That method should add correctly a medical appointment and return data (medicalAppointmentDTO).")
    void shouldAddCorrectlyMedicalAppointmentAndReturnMedicalAppointmentDTO() {
        // Given
        String doctorEmail = "doctor.doctor@clinic.pl";
        String patientEmail = "patient.patient@clinic.pl";
        String medicalAppointmentDate = "2023-11-17 10:00:00";

        MedicalAppointmentDTO expectedMedicalAppointmentDTO = EntityFixtures.someMedicalAppointmentDTO1();
        MedicalAppointmentDTO notExpectedMedicalAppointmentDTO = EntityFixtures.someMedicalAppointmentDTO2();

        MedicalAppointmentRequest request = EntityFixtures.someMedicalAppointmentRequest();
        MedicalAppointment someMedicalAppointment = EntityFixtures.someMedicalAppointment1();

        Mockito.when(medicalAppointmentRequestMapper.map(Mockito.any(MedicalAppointmentRequestDTO.class)))
                .thenReturn(request);
        Mockito.when(medicalAppointmentService.makeAppointment(request)).thenReturn(someMedicalAppointment);
        Mockito.when(medicalAppointmentMapper.map(Mockito.any(MedicalAppointment.class)))
                .thenReturn(expectedMedicalAppointmentDTO);

        // When
        MedicalAppointmentDTO result = patientMakeAppointmentRestController
                .makeAppointment(doctorEmail, patientEmail, medicalAppointmentDate);

        // Then
        Assertions.assertEquals(result, expectedMedicalAppointmentDTO);
        //lista z inna iloscia elementow nie jest taka sama, wiec test przechodzi
        Assertions.assertNotEquals(result, notExpectedMedicalAppointmentDTO);
    }
}