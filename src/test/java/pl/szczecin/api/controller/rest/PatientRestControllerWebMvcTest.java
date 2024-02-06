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
import pl.szczecin.api.dto.PatientDTO;
import pl.szczecin.api.dto.PatientsDTO;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.Patient;
import pl.szczecin.util.EntityFixtures;

import java.util.List;

@WebMvcTest(controllers = PatientRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class PatientRestControllerWebMvcTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;
    @MockBean
    private PatientMapper patientMapper;

    @Test
    @DisplayName("That method should return correct DTO List Patients")
    void shouldReturnCorrectListDTOPatients() throws Exception {
        //given
        PatientsDTO somePatientListDTO = EntityFixtures.somePatientsDTO();
        String responseBody = objectMapper.writeValueAsString(somePatientListDTO);

        // Do mockowania
        List<Patient> patientList = List.of(
                EntityFixtures.somePatient1(),
                EntityFixtures.somePatient2(),
                EntityFixtures.somePatient3()
        );
        List<PatientDTO> patientDTOList = List.of(
                EntityFixtures.somePatientDTO1(),
                EntityFixtures.somePatientDTO2(),
                EntityFixtures.somePatientDTO3()
        );


        Mockito.when(patientService.findAvailablePatients()).thenReturn(patientList);
        Mockito.when(patientMapper.map(Mockito.any(Patient.class)))
                .thenReturn(patientDTOList.get(0))
                .thenReturn(patientDTOList.get(1))
                .thenReturn(patientDTOList.get(2));

        //when,then
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(PatientRestController.PATIENT_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.patientsDTO[0].name",
                        Matchers.is(patientDTOList.get(0).getName())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.patientsDTO[0].surname",
                        Matchers.is(patientDTOList.get(0).getSurname())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.patientsDTO[0].phone",
                        Matchers.is(patientDTOList.get(0).getPhone())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.patientsDTO[0].email",
                        Matchers.is(patientDTOList.get(0).getEmail())))
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);

    }


}