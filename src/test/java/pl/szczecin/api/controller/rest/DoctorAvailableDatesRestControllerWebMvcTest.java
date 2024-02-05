package pl.szczecin.api.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
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
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentDateMapper;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.util.EntityFixtures;

import java.util.List;

@WebMvcTest(controllers = DoctorAvailableDatesRestController.class)
@AutoConfigureMockMvc(addFilters = false) //wylaczenie konfiguracji security na potrzeby testow
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DoctorAvailableDatesRestControllerWebMvcTest {

    //klasa symuluje wywolania przegladarki
    private MockMvc mockMvc;
    // do zamiany jsonow na obiekty i odwrotnie
    private ObjectMapper objectMapper;

    @MockBean
    private MedicalAppointmentDateService medicalAppointmentDateService;
    @MockBean
    private MedicalAppointmentDateMapper medicalAppointmentDateMapper;

    @Test
    @DisplayName("That method should return correct available appointment dates for doctor")
    void shouldReturnCorrectAvailableAppointmentDatesForDoctor() throws Exception {
        //given
        String doctorEmail = "anna.nowak@clinic.pl";
        List<MedicalAppointmentDate> someMedicalAppointmentDateList = List.of(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate2(),
                EntityFixtures.someMedicalAppointmentDate3()
        );

        MedicalAppointmentDateDTO someMedicalAppointmentDateDTO = EntityFixtures.someMedicalAppointmentDateDTO1();
        List<String> expectedDatesList = List.of(
                "2023-11-15 10:00:00",
                "2023-11-16 10:00:00",
                "2023-11-17 10:00:00"
        );

        //when
        Mockito.when(medicalAppointmentDateService.getAvailableDatesByDoctorEmail(doctorEmail))
                .thenReturn(someMedicalAppointmentDateList);
        Mockito.when(medicalAppointmentDateMapper.map(someMedicalAppointmentDateList.get(0)))
                .thenReturn(someMedicalAppointmentDateDTO);

        //then
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(
                                DoctorAvailableDatesRestController.API_DOCTOR_AVAILABLE_DATES, doctorEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

    }
}

//    private List<String> getAvailableDatesByDoctorEmail(String doctorEmail) {
//        return medicalAppointmentDateService.getAvailableDatesByDoctorEmail(doctorEmail).stream()
//                .map(medicalAppointmentDateMapper::map)
//                .map(MedicalAppointmentDateDTO::getDateTime)
//                .toList();
//    }