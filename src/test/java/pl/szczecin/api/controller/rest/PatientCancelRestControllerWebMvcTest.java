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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

@WebMvcTest(controllers = PatientCancelRestController.class)
@AutoConfigureMockMvc(addFilters = false) //wylaczenie konfiguracji security na potrzeby testow
@AllArgsConstructor(onConstructor = @__(@Autowired))
class PatientCancelRestControllerWebMvcTest {

    //klasa symuluje wywolania przegladarki
    private MockMvc mockMvc;
    // do zamiany jsonow na obiekty i odwrotnie
    private ObjectMapper objectMapper;


    @MockBean
    private MedicalAppointmentService medicalAppointmentService;
    @MockBean
    private MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    @MockBean
    private PatientService patientService;
    @MockBean
    private PatientMapper patientMapper;
    @MockBean
    private MedicalAppointmentMapper medicalAppointmentMapper;


    @Test
    @DisplayName("GET - That method should return correct medical appointments schedule for patient")
    void shouldReturnCorrectMedicalAppointmentsScheduleForPatient() throws Exception {
        //given
        PatientHistory somePatientHistory = EntityFixtures.somePatientHistory();
        PatientHistoryDTO somePatientHistoryDTO = EntityFixtures.somePatientHistoryDTO();
        String patientEmail = "anna.nowak@clinic.pl";

        String responseBody = objectMapper.writeValueAsString(somePatientHistoryDTO);

        //when
        Mockito.when(patientService.findPatientScheduleByEmail(patientEmail)).thenReturn(somePatientHistory);
        Mockito.when(patientMapper.map(Mockito.any(PatientHistory.class))).thenReturn(somePatientHistoryDTO);

        //then
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                                PatientCancelRestController.API_PATIENT_APPOINTMENTS, patientEmail)
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


    @Test
    @DisplayName("DELETE - That method should return correct data about cancelled medical appointment")
    void shouldReturnCorrectDataAboutCancelledMedicalAppointment() throws Exception {
        //given
        String patientEmail = "anna.nowak@clinic.pl";

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("doctorEmail", "ewa.kowalska@clinic.pl");
        parameters.add("appointmentDate", "2024-10-23 09:30:00");

        MedicalAppointmentRequest request = EntityFixtures.someMedicalAppointmentRequest();

        MedicalAppointment someMedicalAppointment = EntityFixtures.someMedicalAppointment();
        MedicalAppointmentDTO someMedicalAppointmentDTO = EntityFixtures.someMedicalAppointmentDTO1();

        String responseBody = objectMapper.writeValueAsString(someMedicalAppointmentDTO);

        //when
        Mockito.when(medicalAppointmentRequestMapper.map(Mockito.any(MedicalAppointmentRequestDTO.class))).thenReturn(request);
        Mockito.when(medicalAppointmentService.cancelAppointment(request)).thenReturn(someMedicalAppointment);
        Mockito.when(medicalAppointmentMapper.map(someMedicalAppointment)).thenReturn(someMedicalAppointmentDTO);

        //then
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(
                                PatientCancelRestController.API_PATIENT_APPOINTMENTS
                                        + PatientCancelRestController.CANCEL, patientEmail)
                        .params(parameters))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.patientName",
                        Matchers.is(someMedicalAppointmentDTO.getPatientName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.patientSurname",
                        Matchers.is(someMedicalAppointmentDTO.getPatientSurname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.doctorNote",
                        Matchers.is(someMedicalAppointmentDTO.getDoctorNote())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateTime",
                        Matchers.is(someMedicalAppointmentDTO.getDateTime())))
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);

    }

}