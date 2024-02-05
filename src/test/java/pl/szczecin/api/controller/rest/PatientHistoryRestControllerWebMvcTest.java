package pl.szczecin.api.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.util.EntityFixtures;

@WebMvcTest(controllers = PatientHistoryRestController.class)
@AutoConfigureMockMvc(addFilters = false) //wylaczenie konfiguracji security na potrzeby testow
@AllArgsConstructor(onConstructor = @__(@Autowired))
class PatientHistoryRestControllerWebMvcTest {

    //klasa symuluje wywolania przegladarki
    private MockMvc mockMvc;
    // do zamiany jsonow na obiekty i odwrotnie
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;
    @MockBean
    private PatientMapper patientMapper;


    @Test
    @DisplayName("GET - That method should return correct medical appointments history for patient")
    void shouldReturnCorrectMedicalAppointmentsHistoryForPatient() throws Exception {
        //given
        String patientEmail = "alina.nowak@clinic.pl";
        PatientHistory somePatientHistory = EntityFixtures.somePatientHistory();
        PatientHistoryDTO somePatientHistoryDTO = EntityFixtures.somePatientHistoryDTO();

        String responseBody = objectMapper.writeValueAsString(somePatientHistoryDTO);

        //when
        Mockito.when(patientService.findPatientHistoryByEmail(patientEmail)).thenReturn(somePatientHistory);
        Mockito.when(patientMapper.map(Mockito.any(PatientHistory.class))).thenReturn(somePatientHistoryDTO);

        //then
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                                PatientHistoryRestController.API_PATIENT_HISTORY, patientEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.patientEmail",
                        Matchers.is(somePatientHistoryDTO.getPatientEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medicalAppointments[0].doctorSurname",
                        Matchers.is(somePatientHistoryDTO.getMedicalAppointments().get(0).getDoctorSurname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medicalAppointments[0].doctorName",
                        Matchers.is(somePatientHistoryDTO.getMedicalAppointments().get(0).getDoctorName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medicalAppointments[0].dateTime",
                        Matchers.is(somePatientHistoryDTO.getMedicalAppointments().get(0).getDateTime())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medicalAppointments[0].doctorNote",
                        Matchers.is(somePatientHistoryDTO.getMedicalAppointments().get(0).getDoctorNote())))
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);

    }
}
