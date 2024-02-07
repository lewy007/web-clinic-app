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
import pl.szczecin.api.dto.MedicalAppointmentsDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.util.EntityFixtures;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DoctorScheduleRestController.class)
@AutoConfigureMockMvc(addFilters = false) //wylaczenie konfiguracji security na potrzeby testow
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DoctorScheduleRestControllerWebMvcTest {

    //klasa symuluje wywolania przegladarki
    private MockMvc mockMvc;
    // do zamiany jsonow na obiekty i odwrotnie
    private ObjectMapper objectMapper;

    @MockBean
    private MedicalAppointmentService medicalAppointmentService;
    @MockBean
    private MedicalAppointmentMapper medicalAppointmentMapper;
    @MockBean
    private MedicalAppointmentDateService medicalAppointmentDateService;
    @MockBean
    private MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;

    @Test
    @DisplayName("GET - That method should return correct medical appointment schedule for doctor")
    void shouldReturnCorrectMedicalAppointmentScheduleForDoctor() throws Exception {
        //given
        String doctorEmail = "anna.nowak@clinic.pl";
        MedicalAppointmentsDTO someMedicalAppointmentsDTO = EntityFixtures.someMedicalAppointmentsDTO();
        String responseBody = objectMapper.writeValueAsString(someMedicalAppointmentsDTO);

        // dane do mockowania
        List<MedicalAppointmentDate> medicalAppointmentDateList = List.of(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate2(),
                EntityFixtures.someMedicalAppointmentDate3()
        );
        List<Integer> someMedicalAppointmentDateIdsByDoctorEmail = List.of(1, 2, 3);

        List<MedicalAppointment> medicalAppointmentList = List.of(
                EntityFixtures.someMedicalAppointment1(),
                EntityFixtures.someMedicalAppointment2(),
                EntityFixtures.someMedicalAppointment3()
        );

        List<MedicalAppointmentDTO> medicalAppointmentDTOList = List.of(
                EntityFixtures.someMedicalAppointmentDTO1(),
                EntityFixtures.someMedicalAppointmentDTO2(),
                EntityFixtures.someMedicalAppointmentDTO3()
        );


        //when
        Mockito.when(medicalAppointmentDateService.getAllFutureDatesByDoctorEmail(doctorEmail))
                .thenReturn(medicalAppointmentDateList);
        Mockito.when(medicalAppointmentService.findAllMedicalAppointmentByMADateID(someMedicalAppointmentDateIdsByDoctorEmail))
                .thenReturn(medicalAppointmentList);
        Mockito.when(medicalAppointmentMapper.map(Mockito.any(MedicalAppointment.class)))
                .thenReturn(medicalAppointmentDTOList.get(0))
                .thenReturn(medicalAppointmentDTOList.get(1))
                .thenReturn(medicalAppointmentDTOList.get(2));

        //then
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(DoctorScheduleRestController.API_DOCTOR_SCHEDULE, doctorEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.medicalAppointmentsDTO", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medicalAppointmentsDTO[0].patientName",
                        Matchers.is(medicalAppointmentDTOList.get(0).getPatientName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medicalAppointmentsDTO[0].patientSurname",
                        Matchers.is(medicalAppointmentDTOList.get(0).getPatientSurname())))
                .andExpect(jsonPath("$.medicalAppointmentsDTO[0].doctorNote",
                        Matchers.is(medicalAppointmentDTOList.get(0).getDoctorNote())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.medicalAppointmentsDTO[0].dateTime",
                        Matchers.is(medicalAppointmentDTOList.get(0).getDateTime())))
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);
    }


    @Test
    @DisplayName("PATCH - That method should return correct medical appointment with added note")
    void shouldReturnCorrectMedicalAppointmentWithAddedNote() throws Exception {
        //given
        String doctorEmail = "anna.nowak@clinic.pl";

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("patientName", "Jan");
        parameters.add("patientSurname", "Kowalski");
        parameters.add("appointmentDate", "2024-10-23 09:30:00");
        parameters.add("doctorNote", "some added note to test");

        MedicalAppointmentRequestDTO medicalAppointmentRequestDTO = EntityFixtures.someMedicalAppointmentRequestDTO();
        MedicalAppointmentRequest medicalAppointmentRequest = EntityFixtures.someMedicalAppointmentRequest();
        MedicalAppointment medicalAppointment = EntityFixtures.someMedicalAppointment1();
        MedicalAppointmentDTO medicalAppointmentDTO = EntityFixtures.someMedicalAppointmentDTO1();

        String responseBody = objectMapper.writeValueAsString(medicalAppointmentDTO);

        //when
        Mockito.when(medicalAppointmentRequestMapper.map(medicalAppointmentRequestDTO)).thenReturn(medicalAppointmentRequest);
        Mockito.when(medicalAppointmentService.addNoteToMedicalAppointment(medicalAppointmentRequest)).thenReturn(medicalAppointment);
        Mockito.when(medicalAppointmentMapper.map(Mockito.any(MedicalAppointment.class))).thenReturn(medicalAppointmentDTO);

        //then
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch(
                                DoctorScheduleRestController.API_DOCTOR_SCHEDULE
                                        + DoctorScheduleRestController.NOTE, doctorEmail)
                        .params(parameters))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.patientName",
                        Matchers.is(medicalAppointmentDTO.getPatientName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.patientSurname",
                        Matchers.is(medicalAppointmentDTO.getPatientSurname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateTime",
                        Matchers.is(medicalAppointmentDTO.getDateTime())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.doctorNote",
                        Matchers.is(medicalAppointmentDTO.getDoctorNote())))
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);
    }

}