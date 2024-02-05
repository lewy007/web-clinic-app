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
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.util.EntityFixtures;

import java.util.List;

@WebMvcTest(controllers = DoctorHistoryRestController.class)
@AutoConfigureMockMvc(addFilters = false) //wylaczenie konfiguracji security na potrzeby testow
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DoctorHistoryRestControllerWebMvcTest {

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

    @Test
    @DisplayName("That method should return correct medical appointment history for doctor")
    void shouldReturnCorrectMedicalAppointmentHistoryForDoctor() throws Exception {
        //given
        String doctorEmail = "anna.nowak@clinic.pl";
        List<MedicalAppointmentDate> medicalAppointmentDateList = List.of(EntityFixtures.someMedicalAppointmentDate1());
        List<MedicalAppointment> medicalAppointmentList = List.of(EntityFixtures.someMedicalAppointment());
        List<Integer> someMedicalAppointmentDateIdsByDoctorEmail = List.of(1, 3, 7, 9, 11);

        MedicalAppointment someMedicalAppointment = EntityFixtures.someMedicalAppointment();
        MedicalAppointmentDTO someMedicalAppointmentDTO = EntityFixtures.someMedicalAppointmentDTO1();

        String responseBody = objectMapper.writeValueAsString(someMedicalAppointmentDTO);

        //when
        Mockito.when(medicalAppointmentDateService.getAllHistoryDatesByDoctorEmail(doctorEmail))
                .thenReturn(medicalAppointmentDateList);
        Mockito.when(medicalAppointmentService.findAllMedicalAppointmentByMADateID(someMedicalAppointmentDateIdsByDoctorEmail))
                .thenReturn(medicalAppointmentList);
        Mockito.when(medicalAppointmentMapper.map(Mockito.any(MedicalAppointment.class)))
                .thenReturn(someMedicalAppointmentDTO);

        //then
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(DoctorHistoryRestController.API_DOCTOR_HISTORY, doctorEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.patientName",
//                        Matchers.is(someMedicalAppointmentDTO.getPatientName())))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.patientSurname",
//                        Matchers.is(someMedicalAppointmentDTO.getPatientSurname())))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.doctorNote",
//                        Matchers.is(someMedicalAppointmentDTO.getDoctorNote())))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.dateTime",
//                        Matchers.is(someMedicalAppointmentDTO.getDateTime())))
                .andReturn();

//        Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);
    }

}

//.doctorNote("some note 1")
//        .dateTime("2023-11-16 10:00:00")
//        .patientName("Jan")
//        .patientSurname(