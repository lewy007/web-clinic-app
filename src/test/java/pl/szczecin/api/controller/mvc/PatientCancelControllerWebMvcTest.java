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
import pl.szczecin.api.controller.PatientCancelController;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.*;
import pl.szczecin.util.EntityFixtures;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PatientCancelController.class)
@AutoConfigureMockMvc(addFilters = false) //wylaczenie konfiguracji security na potrzeby testow
@AllArgsConstructor(onConstructor = @__(@Autowired))
class PatientCancelControllerWebMvcTest {

    //klasa symuluje wywolania przegladarki
    private MockMvc mockMvc;


    @MockBean
    private MedicalAppointmentService medicalAppointmentService;
    @MockBean
    private MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    @MockBean
    private PatientService patientService;
    @MockBean
    private PatientMapper patientMapper;
    @MockBean
    private DoctorService doctorService;


    @Test
    @DisplayName("GET Method should return the correct view")
    void patientCancelControllerMethodGetWorksCorrectly() throws Exception {
        //given, when
        String patientEmail = "test@example.com";
        PatientHistory expectedPatientHistory = EntityFixtures.somePatientHistory();
        PatientHistoryDTO expectedPatientHistoryDTO = EntityFixtures.somePatientHistoryDTO();

        Mockito.when(patientService.getLoggedInPatientEmail()).thenReturn(patientEmail);
        Mockito.when(patientService.findCurrentPatientAppointmentsByEmail(patientEmail))
                .thenReturn(expectedPatientHistory);
        Mockito.when(patientMapper.map(Mockito.any(PatientHistory.class)))
                .thenReturn(expectedPatientHistoryDTO);

        //then
        mockMvc.perform(get(PatientCancelController.PATIENT_CANCEL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patientHistoryDTO"))
                .andExpect(model().attributeExists("loggedInPatientEmail"))
                .andExpect(view().name("patient_cancel"));
    }

    @Test
    @DisplayName("POST Method should correctly cancel an appointment")
    void patientCancelControllerShouldCancelAppointment() throws Exception {
        // given
        MedicalAppointmentRequest expectedRequest = EntityFixtures.someMedicalAppointmentRequest();
        String patientEmail = "patient.test@clinic.pl";
        Patient expectedPatient = EntityFixtures.somePatient1();
        String doctorSurname = "Testowa";
        Doctor expectedDoctor = EntityFixtures.someDoctor1();
        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment();
        String appointmentDate = "2022-08-27 09:28:00";

        Mockito.when(medicalAppointmentRequestMapper.map(Mockito.any(MedicalAppointmentRequestDTO.class)))
                .thenReturn(expectedRequest);
        Mockito.when(patientService.findPatientByEmail(patientEmail))
                .thenReturn(expectedPatient);
        Mockito.when(doctorService.findDoctorBySurname(doctorSurname))
                .thenReturn(expectedDoctor);
        Mockito.when(medicalAppointmentService.addNoteToMedicalAppointment(expectedRequest))
                .thenReturn(expectedMedicalAppointment);

        // when, then
        mockMvc.perform(post(PatientCancelController.PATIENT_CANCEL)
                        .param("patientEmail", patientEmail)
                        .param("appointmentDate", appointmentDate)
                        .param("doctorSurname", doctorSurname)
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patientEmail"))
                .andExpect(model().attributeExists("medicalAppointmentDate"))
                .andExpect(model().attributeExists("doctorName"))
                .andExpect(model().attributeExists("doctorSurname"))
                .andExpect(model().attributeExists("patientName"))
                .andExpect(model().attributeExists("patientSurname"))
                .andExpect(view().name("patient_cancel_done"));

    }

}