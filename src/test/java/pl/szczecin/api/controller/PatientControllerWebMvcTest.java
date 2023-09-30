package pl.szczecin.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.szczecin.api.dto.DoctorDTO;
import pl.szczecin.api.dto.mapper.DoctorMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.Doctor;
import pl.szczecin.util.EntityFixtures;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = PatientController.class)
@AutoConfigureMockMvc(addFilters = false) //wylaczenie konfiguracji security na potrzeby testow
@AllArgsConstructor(onConstructor = @__(@Autowired))
class PatientControllerWebMvcTest {

    //klasa symuluje wywolania przegladarki
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;
    @MockBean
    private DoctorService doctorService;
    @MockBean
    private DoctorMapper doctorMapper;


    @Test
    void patientControllerMethodGetWorksCorrectly() throws Exception {

        // Given
        String emailPatient = "test@example.com";

        List<Doctor> doctors = List.of(
                EntityFixtures.someDoctor1(),
                EntityFixtures.someDoctor2(),
                EntityFixtures.someDoctor3());

        Mockito.when(patientService.getLoggedInPatientEmail()).thenReturn(emailPatient);
        Mockito.when(doctorService.findAvailableDoctors()).thenReturn(doctors);

        // Mock the map operation
        Mockito.when(doctorMapper.map(Mockito.any(Doctor.class))).thenAnswer(
                invocation -> {
                    Doctor inputDoctor = invocation.getArgument(0);
                    return DoctorDTO.builder()
                            .name(inputDoctor.getName())
                            .surname(inputDoctor.getSurname())
                            .email(inputDoctor.getEmail())
                            .build();
                });

        // when, then
        mockMvc.perform(get(PatientController.PATIENT))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("availableDoctorsDTOs"))
                .andExpect(model().attributeExists("loggedInPatientEmail"))
                .andExpect(view().name("patient_portal"));
    }

}