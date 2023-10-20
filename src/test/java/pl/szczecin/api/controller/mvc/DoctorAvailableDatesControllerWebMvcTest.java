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
import pl.szczecin.api.controller.DoctorAvailableDatesController;
import pl.szczecin.api.dto.mapper.MedicalAppointmentDateMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DoctorAvailableDatesController.class)
@AutoConfigureMockMvc(addFilters = false) //wylaczenie konfiguracji security na potrzeby testow
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DoctorAvailableDatesControllerWebMvcTest {

    //klasa symuluje wywolania przegladarki
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;
    @MockBean
    private MedicalAppointmentDateService medicalAppointmentDateService;
    @MockBean
    private MedicalAppointmentDateMapper medicalAppointmentDateMapper;


    @Test
    @DisplayName("GET Method should return the correct view")
    void doctorAvailableDatesControllerMethodGetWorksCorrectly() throws Exception {
        //given, when
        String doctorEmail = "doctor.test@clinic.pl";

        Mockito.when(doctorService.getLoggedInDoctorEmail()).thenReturn(doctorEmail);

        //then
        mockMvc.perform(get(DoctorAvailableDatesController.DOCTOR_AVAILABLE_DATES))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("loggedInDoctorEmail"))
                .andExpect(model().attributeExists("availableDatesByDoctorEmail"))
                .andExpect(view().name("doctor_available_dates"));
    }

}