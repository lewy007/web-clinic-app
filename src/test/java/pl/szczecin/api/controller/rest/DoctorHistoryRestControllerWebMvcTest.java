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
import pl.szczecin.api.dto.MedicalAppointmentsDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.util.EntityFixtures;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.szczecin.util.EntityFixtures.*;

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
        MedicalAppointmentsDTO someMedicalAppointmentsDTO = EntityFixtures.someMedicalAppointmentsDTO();
        String responseBody = objectMapper.writeValueAsString(someMedicalAppointmentsDTO);

        // dane do mockowania
        List<MedicalAppointmentDate> medicalAppointmentDateList = List.of(
                someMedicalAppointmentDate1(),
                someMedicalAppointmentDate2(),
                someMedicalAppointmentDate3()
        );
        List<Integer> someMedicalAppointmentDateIdsByDoctorEmail = List.of(1, 2, 3);

        List<MedicalAppointment> medicalAppointmentList = List.of(
                someMedicalAppointment1(),
                someMedicalAppointment2(),
                someMedicalAppointment3()
        );

        List<MedicalAppointmentDTO> medicalAppointmentDTOList = List.of(
                someMedicalAppointmentDTO1(),
                someMedicalAppointmentDTO2(),
                someMedicalAppointmentDTO3()
        );


        //when
        Mockito.when(medicalAppointmentDateService.getAllHistoryDatesByDoctorEmail(doctorEmail))
                .thenReturn(medicalAppointmentDateList);
        Mockito.when(medicalAppointmentService.findAllMedicalAppointmentByMADateID(someMedicalAppointmentDateIdsByDoctorEmail))
                .thenReturn(medicalAppointmentList);
        Mockito.when(medicalAppointmentMapper.map(Mockito.any(MedicalAppointment.class)))
                .thenReturn(medicalAppointmentDTOList.get(0))
                .thenReturn(medicalAppointmentDTOList.get(1))
                .thenReturn(medicalAppointmentDTOList.get(2));

        //then
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(DoctorHistoryRestController.API_DOCTOR_HISTORY, doctorEmail)
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

}
