package pl.szczecin.api.controller.mvc;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.szczecin.api.controller.PatientScheduleController;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.util.EntityFixtures;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PatientScheduleController.class)
@AutoConfigureMockMvc(addFilters = false) //wylaczenie konfiguracji security na potrzeby testow
@AllArgsConstructor(onConstructor = @__(@Autowired))
class PatientScheduleControllerWebMvcTest {

    //klasa symuluje wywolania przegladarki
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;
    @MockBean
    private PatientMapper patientMapper;


    @Test
    @DisplayName("GET Method should return the correct view")
    void patientScheduleControllerMethodGetWorksCorrectly() throws Exception {
        //given, when
        String patientEmail = "patient.test@clinic.pl";
        PatientHistory expected = EntityFixtures.somePatientHistory();
        PatientHistoryDTO expectedPatientHistoryDTO = EntityFixtures.somePatientHistoryDTO();


        Mockito.when(patientService.getLoggedInPatientEmail()).thenReturn(patientEmail);
        Mockito.when(patientService.findPatientScheduleByEmail(patientEmail)).thenReturn(expected);
        Mockito.when(patientMapper.map(Mockito.any(PatientHistory.class)))
                .thenReturn(expectedPatientHistoryDTO);


        //then
        mockMvc.perform(get(PatientScheduleController.PATIENT_SCHEDULE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patientHistoryDTO"))
                .andExpect(model().attributeExists("loggedInPatientEmail"))
                .andExpect(view().name("patient_schedule"));
    }

}