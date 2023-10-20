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
import pl.szczecin.api.controller.DoctorHistoryController;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.util.EntityFixtures;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DoctorHistoryController.class)
@AutoConfigureMockMvc(addFilters = false) //wylaczenie konfiguracji security na potrzeby testow
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DoctorHistoryControllerWebMvcTest {

    //klasa symuluje wywolania przegladarki
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;
    @MockBean
    private MedicalAppointmentService medicalAppointmentService;
    @MockBean
    private MedicalAppointmentMapper medicalAppointmentMapper;
    @MockBean
    private MedicalAppointmentDateService medicalAppointmentDateService;
    @MockBean
    private MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;


    @Test
    @DisplayName("GET Method should return the correct view")
    void doctorHistoryControllerMethodGetWorksCorrectly() throws Exception {
        //given, when
        String doctorEmail = "doctor.test@clinic.pl";
        List<MedicalAppointmentDate> medicalAppointmentDateList = List.of(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate2(),
                EntityFixtures.someMedicalAppointmentDate3()
        );

        Mockito.when(doctorService.getLoggedInDoctorEmail()).thenReturn(doctorEmail);
        Mockito.when(medicalAppointmentDateService.getAllDatesByDoctorEmail(doctorEmail))
                .thenReturn(medicalAppointmentDateList);

        //then
        mockMvc.perform(get(DoctorHistoryController.DOCTOR_HISTORY))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("loggedInDoctorEmail"))
                .andExpect(model().attributeExists("medicalAppointmentDTOs"))
                .andExpect(view().name("doctor_history"));
    }

    @Test
    @DisplayName("POST Method should correctly add a note")
    void doctorTorbeControllerShouldCorrectAddNote() throws Exception {
        // given
        String patientName = "Agnieszka";
        String patientSurname = "Testowa";
        String appointmentDate = "2022-08-27 09:28:00";
        String doctorNote = "some test note";


        MedicalAppointmentRequest expectedRequest = EntityFixtures.someMedicalAppointmentRequest();
        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment();


        Mockito.when(medicalAppointmentRequestMapper.map(Mockito.any(MedicalAppointmentRequestDTO.class)))
                .thenReturn(expectedRequest);
        Mockito.when(medicalAppointmentService.addNoteToMedicalAppointment(expectedRequest))
                .thenReturn(expectedMedicalAppointment);

        // when, then
        mockMvc.perform(post(DoctorHistoryController.DOCTOR_HISTORY)
                        .param("appointmentDate", appointmentDate)
                        .param("patientName", patientName)
                        .param("patientSurname", patientSurname)
                        .param("doctorNote", doctorNote)
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(view().name("doctor_history_added_note"));

    }

}