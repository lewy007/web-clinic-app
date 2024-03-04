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
import pl.szczecin.api.controller.rest.PatientHistoryRestController;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.util.EntityFixtures;


@ExtendWith(MockitoExtension.class)
class PatientHistoryRestControllerMockitoTest {

    @Mock
    private PatientService patientService;
    @Mock
    private PatientMapper patientMapper;


    @InjectMocks
    private PatientHistoryRestController patientHistoryRestController;


    @BeforeEach
    public void setUp() {
        System.out.println("checking for nulls");
        Assertions.assertNotNull(patientService);
        Assertions.assertNotNull(patientMapper);
    }


    @Test
    @DisplayName("GET - That method should return correct medical appointment history for selected patient")
    void shouldReturnCorrectMedicalAppointmentHistoryForPatient() {
        // Given
        String patientEmail = "anna.nowak@clinic.pl";
        PatientHistory somePatientHistory = EntityFixtures.somePatientHistory();
        PatientHistoryDTO expectedPatientHistoryDTO = EntityFixtures.somePatientHistoryDTO();
        PatientHistoryDTO notExpectedPatientHistoryDTO = EntityFixtures.somePatientHistoryDTO2();


        Mockito.when(patientService.findPatientHistoryByEmail(patientEmail)).thenReturn(somePatientHistory);
        Mockito.when(patientMapper.map(Mockito.any(PatientHistory.class)))
                .thenReturn(expectedPatientHistoryDTO);

        // When
        PatientHistoryDTO result = patientHistoryRestController.patientMedicalAppointmentHistory(patientEmail);

        // Then
        Assertions.assertEquals(result, expectedPatientHistoryDTO);
        //lista z inna iloscia elementow nie jest taka sama, wiec test przechodzi
        Assertions.assertNotEquals(result, notExpectedPatientHistoryDTO);

        Mockito.verify(patientService, Mockito.times(1))
                .findPatientHistoryByEmail(Mockito.anyString());
        Mockito.verify(patientService, Mockito.never())
                .findPatientHistoryByEmail("other.email@clinic.pl");
        Mockito.verify(patientMapper, Mockito.times(1))
                .map(Mockito.any(PatientHistory.class));
    }
}