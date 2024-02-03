package pl.szczecin.api.controller.mvc.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import pl.szczecin.api.controller.PatientScheduleController;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.util.EntityFixtures;

@ExtendWith(MockitoExtension.class)
class PatientScheduleControllerMockitoTest {

    @Mock
    private PatientService patientService;
    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientScheduleController patientScheduleController;

    @Test
    @DisplayName("That method should return correct view")
    public void patientSchedulePageShouldReturnCorrectViewName() {
        // given
        ExtendedModelMap model = new ExtendedModelMap();

        // when
        String resultView = patientScheduleController.patientSchedulePage(model);

        // then
        Assertions.assertThat("patient_schedule").isEqualTo(resultView);

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

    }

    @Test
    @DisplayName("That method should return correct attributes of model")
    void patientSchedulePageShouldAddAttributesToModel() {
        // given
        ExtendedModelMap model = new ExtendedModelMap();

        // when
        patientScheduleController.patientSchedulePage(model);

        // then
        Assertions.assertThat(model).containsKeys("patientHistoryDTO", "loggedInPatientEmail");
    }

    @Test
    @DisplayName("That method should return correct patient schedule of logged in patient")
    public void findPatientScheduleByEmailShouldReturnCorrectPatientSchedule() {
        // given
        String patientEmail = "patient.test@clinic.pl";
        PatientHistory expected = EntityFixtures.somePatientHistory();

        Mockito.when(patientService.findPatientScheduleByEmail(patientEmail)).thenReturn(expected);

        // when
        PatientHistory result = patientService.findPatientScheduleByEmail(patientEmail);

        // then
        Assertions.assertThat(result).isEqualTo(expected);

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

    }

}