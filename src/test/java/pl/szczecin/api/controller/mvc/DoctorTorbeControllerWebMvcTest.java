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
import pl.szczecin.api.controller.DoctorTorbeController;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentDateMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.Doctor;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.util.EntityFixtures;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = DoctorTorbeController.class)
@AutoConfigureMockMvc(addFilters = false) //wylaczenie konfiguracji security na potrzeby testow
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DoctorTorbeControllerWebMvcTest {


    //klasa symuluje wywolania przegladarki
    private MockMvc mockMvc;

    @MockBean
    private MedicalAppointmentService medicalAppointmentService;
    @MockBean
    private MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    @MockBean
    private MedicalAppointmentDateService medicalAppointmentDateService;
    @MockBean
    private MedicalAppointmentDateMapper medicalAppointmentDateMapper;
    @MockBean
    private DoctorService doctorService;
    @MockBean
    private PatientService patientService;

    @Test
    @DisplayName("GET Method should return the correct view")
    void doctorTorbeControllerShouldReturnCorrectView() throws Exception {
        // given
        String patientEmail = "patient@example.com";
        String doctorSurname = "Torbe";
        String doctorEmail = "agata.torbe@clinic.pl";

        Doctor expectedDoctor = EntityFixtures.someDoctor1();

        List<MedicalAppointmentDate> medicalAppointmentDateList =
                List.of(
                        EntityFixtures.someMedicalAppointmentDate1(),
                        EntityFixtures.someMedicalAppointmentDate2(),
                        EntityFixtures.someMedicalAppointmentDate3()
                );


        Mockito.when(patientService.getLoggedInPatientEmail()).thenReturn(patientEmail);

        Mockito.when(doctorService.findDoctorBySurname(doctorSurname)).thenReturn(expectedDoctor);
        // bez tego mockowania, test rowniez przechodzi
        Mockito.when(medicalAppointmentDateService.getAvailableDatesByDoctorEmail(doctorEmail))
                .thenReturn(medicalAppointmentDateList);

        // when, then
        mockMvc.perform(get(DoctorTorbeController.DOCTOR_TORBE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("loggedInPatientEmail"))
                .andExpect(model().attributeExists("doctorTorbeEmail"))
                .andExpect(model().attributeExists("availableDates"))
                .andExpect(view().name("doctor_torbe_portal"));
    }

    @Test
    @DisplayName("POST Method should correctly make an appointment")
    void doctorTorbeControllerShouldCorrectMakeAppointment() throws Exception {
        // given
        String doctorSurname = "Torbe";
        Doctor expectedDoctor = EntityFixtures.someDoctor1();

        String medicalAppointmentDate = "2022-08-27 09:28:00";

        MedicalAppointmentRequest expectedRequest = EntityFixtures.someMedicalAppointmentRequest();
        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment();


        Mockito.when(doctorService.findDoctorBySurname(doctorSurname)).thenReturn(expectedDoctor);
        Mockito.when(medicalAppointmentRequestMapper.map(Mockito.any(MedicalAppointmentRequestDTO.class)))
                .thenReturn(expectedRequest);
        Mockito.when(medicalAppointmentService.makeAppointment(expectedRequest)).thenReturn(expectedMedicalAppointment);

        // when, then
        mockMvc.perform(post(DoctorTorbeController.DOCTOR_TORBE)
                        .param("medicalAppointmentDate", medicalAppointmentDate))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patientName"))
                .andExpect(model().attributeExists("patientSurname"))
                .andExpect(model().attributeExists("patientEmail"))
                .andExpect(model().attributeExists("medicalAppointmentDate"))
                .andExpect(model().attributeExists("doctorName"))
                .andExpect(model().attributeExists("doctorSurname"))
                .andExpect(view().name("medical_appointment_done"));

    }

}

