package pl.szczecin.api.controller.integration;

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
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.util.EntityFixtures;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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


//    @Test
//    @DisplayName("GET Method should return the correct view")
//    void patientCancelControllerMethodGetWorksCorrectly() throws Exception {
//        //given, when
//        String patientEmail = "test@example.com";
//        PatientHistory expectedPatientHistory = EntityFixtures.somePatientHistory();
//
//        Mockito.when(patientService.getLoggedInPatientEmail()).thenReturn(patientEmail);
//        Mockito.when(patientService.findCurrentPatientAppointmentsByEmail(patientEmail))
//                .thenReturn(expectedPatientHistory);
//
//        //then
//        mockMvc.perform(get(PatientCancelController.PATIENT_CANCELLED))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("patientHistoryDTO"))
//                .andExpect(model().attributeExists("loggedInPatientEmail"))
//                .andExpect(view().name("patient_cancel"));
//    }

//    @Test
//    @DisplayName("POST Method should correctly add a note")
//    void doctorTorbeControllerShouldCorrectAddNote() throws Exception {
//        // given
//        String patientName = "Agnieszka";
//        String patientSurname = "Testowa";
//        String appointmentDate = "2022-08-27 09:28:00";
//        String doctorNote = "some test note";
//
//
//        MedicalAppointmentRequest expectedRequest = EntityFixtures.someMedicalAppointmentRequest();
//        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment();
//
//
//        Mockito.when(medicalAppointmentRequestMapper.map(Mockito.any(MedicalAppointmentRequestDTO.class)))
//                .thenReturn(expectedRequest);
//        Mockito.when(medicalAppointmentService.addNoteToMedicalAppointment(expectedRequest))
//                .thenReturn(expectedMedicalAppointment);
//
//        // when, then
//        mockMvc.perform(post(DoctorHistoryController.DOCTOR_HISTORY)
//                        .param("appointmentDate", appointmentDate)
//                        .param("patientName", patientName)
//                        .param("patientSurname", patientSurname)
//                        .param("doctorNote", doctorNote)
//                )
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("successMessage"))
//                .andExpect(view().name("doctor_history_added_note"));
//
//    }

}