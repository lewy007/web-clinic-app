package pl.szczecin.api.controller.mvc.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import pl.szczecin.api.controller.PatientHistoryController;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.Patient;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.util.EntityFixtures;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PatientHistoryControllerMockitoTest {

    @Mock
    private PatientService patientService;
    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientHistoryController patientHistoryController;


    @BeforeEach
    public void setUp() {
        System.out.println("checking for nulls");
        assertNotNull(patientService);
        assertNotNull(patientMapper);
    }


    @Test
    @DisplayName("That method should return correct view")
    public void patientHistoryPageShouldReturnCorrectViewName() {
        // given
        ExtendedModelMap model = new ExtendedModelMap();

        // when
        String resultView = patientHistoryController.patientHistoryPage(model);

        // then
        Assertions.assertThat("patient_history").isEqualTo(resultView);

    }

    @Test
    @DisplayName("That method should return correct email of logged in patient")
    public void getLoggedInPatientEmailShouldReturnCorrectEmail() {
        // given
        String patientEmail = "patient.test@clinic.pl";
        Mockito.when(patientService.getLoggedInPatientEmail()).thenReturn(patientEmail);

        // when
        String result = patientService.getLoggedInPatientEmail();

        // then
        Assertions.assertThat(result).isEqualTo(patientEmail);

        Mockito.verify(patientService, Mockito.times(1))
                .getLoggedInPatientEmail();

        Mockito.verifyNoInteractions(patientMapper);

    }

    @Test
    @DisplayName("That method should return correct attributes of model")
    void patientHistoryPageShouldAddAttributesToModel() {
        // given
        ExtendedModelMap model = new ExtendedModelMap();

        // when
        patientHistoryController.patientHistoryPage(model);

        // then
        Assertions.assertThat(model).containsKeys("patientHistoryDTO", "loggedInPatientEmail");
    }

    @Test
    @DisplayName("That method should return correct patient history of logged in patient")
    public void findPatientHistoryByEmailShouldReturnCorrectPatientHistory() {
        // given
        String patientEmail = "patient.test@clinic.pl";
        PatientHistory expected = EntityFixtures.somePatientHistory();

        Mockito.when(patientService.findPatientHistoryByEmail(patientEmail)).thenReturn(expected);

        // when
        PatientHistory result = patientService.findPatientHistoryByEmail(patientEmail);

        // then
        Assertions.assertThat(result).isEqualTo(expected);

        Mockito.verify(patientService, Mockito.times(1))
                .findPatientHistoryByEmail(Mockito.anyString());
        Mockito.verify(patientService, Mockito.never())
                .findPatientHistoryByEmail("other.email@clinic.pl");

        Mockito.verifyNoInteractions(patientMapper);

    }

    @Test
    @DisplayName("That method should correctly mapped patientHistory to patientHistoryDTO")
    void patientMapperShouldReturnCorrectMappedPatientHistory() {
        // given
        PatientHistory somePatientHistory = EntityFixtures.somePatientHistory();
        PatientHistoryDTO expectedPatientHistoryDTO = EntityFixtures.somePatientHistoryDTO();

        Mockito.when(patientMapper.map(Mockito.any(PatientHistory.class)))
                .thenReturn(expectedPatientHistoryDTO);

        // when
        PatientHistoryDTO resultPatientHistoryDTO = patientMapper.map(somePatientHistory);

        // then
        Assertions.assertThat(resultPatientHistoryDTO).isEqualTo(expectedPatientHistoryDTO);

        Mockito.verify(patientMapper, Mockito.times(1))
                .map(Mockito.any(PatientHistory.class));
        Mockito.verify(patientMapper, Mockito.never())
                .map(somePatientHistory.withPatientEmail("other.email@clinic.pl"));

        Mockito.verifyNoInteractions(patientService);

    }

}