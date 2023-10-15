package pl.szczecin.api.controller.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import pl.szczecin.api.controller.PatientCancelController;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.*;
import pl.szczecin.util.EntityFixtures;

@ExtendWith(MockitoExtension.class)
class PatientCancelControllerMockitoTest {

    @Mock
    private MedicalAppointmentService medicalAppointmentService;
    @Mock
    private MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    @Mock
    private PatientService patientService;
    @Mock
    private PatientMapper patientMapper;
    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private PatientCancelController patientCancelController;

    @Test
    @DisplayName("That method should return correct view")
    public void patientCancelAppointmentPageShouldReturnCorrectViewName() {
        // given
        ExtendedModelMap model = new ExtendedModelMap();

        // when
        String resultView = patientCancelController.patientCancelAppointmentPage(model);

        // then
        Assertions.assertThat("patient_cancel").isEqualTo(resultView);

    }

    @Test
    @DisplayName("That method should return correct email of logged in patient")
    public void patientCancelAppointmentPageShouldReturnCorrectEmailPatient() {
        // given
        String patientEmail = "test@example.com";
        Mockito.when(patientService.getLoggedInPatientEmail()).thenReturn(patientEmail);

        // when
        String result = patientService.getLoggedInPatientEmail();

        // then
        Assertions.assertThat(result).isEqualTo(patientEmail);

    }

    @Test
    @DisplayName("That method should return correct attributes of model")
    void patientCancelAppointmentPageShouldAddAttributesToModel() {
        // given
        ExtendedModelMap model = new ExtendedModelMap();

        // when
        patientCancelController.patientCancelAppointmentPage(model);

        // then
        Assertions.assertThat(model).containsKeys("patientHistoryDTO", "loggedInPatientEmail");
    }

    @Test
    @DisplayName("That method should return correct medical appointments for Patient")
    void findCurrentPatientAppointmentsByEmailShouldReturnCorrectAppointments() {
        // given
        String patientEmail = "patient.test@clinic.pl";
        PatientHistory expectedPatientHistory = EntityFixtures.somePatientHistory();

        Mockito.when(patientService.findCurrentPatientAppointmentsByEmail(patientEmail))
                .thenReturn(expectedPatientHistory);

        // when
        PatientHistory resultPatientHistory = patientService
                .findCurrentPatientAppointmentsByEmail(patientEmail);

        // then
        Assertions.assertThat(resultPatientHistory).isEqualTo(expectedPatientHistory);

    }


    // metoda POST
    @Test
    @DisplayName("That method should correctly mapped MedicalAppointmentRequestDTO to MedicalAppointmentRequest")
    void medicalAppointmentRequestMapperShouldReturnCorrectRequest() {
        // given
        String patientName = "Agnieszka";
        String patientSurname = "Testowa";
        String date = "2022-08-27 09:28:00";

        MedicalAppointmentRequest expectedRequest =
                EntityFixtures.someMedicalAppointmentRequest();

        Mockito.when(medicalAppointmentRequestMapper.map(Mockito.any(MedicalAppointmentRequestDTO.class)))
                .thenReturn(expectedRequest);

        // when
        MedicalAppointmentRequest resultRequest = medicalAppointmentRequestMapper.map(
                MedicalAppointmentRequestDTO.builder()
                        .medicalAppointmentDate(date)
                        .patientName(patientName)
                        .patientSurname(patientSurname)
                        .build()
        );

        // then
        Assertions.assertThat(resultRequest).isEqualTo(expectedRequest);
    }

    @Test
    @DisplayName("That method should return correct Patient")
    void findPatientByEmailShouldReturnCorrectPatient() {
        // given
        String patientEmail = "patient.test@clinic.pl";
        Patient expectedPatient = EntityFixtures.somePatient1();

        Mockito.when(patientService.findPatientByEmail(patientEmail))
                .thenReturn(expectedPatient);

        // when
        Patient resultPatient = patientService.findPatientByEmail(patientEmail);

        // then
        Assertions.assertThat(resultPatient).isEqualTo(expectedPatient);

    }

    @Test
    @DisplayName("That method should return correct Doctor")
    void findDoctorBySurnameShouldReturnCorrectPatient() {
        // given
        String doctorSurname = "Testowa";
        Doctor expectedDoctor = EntityFixtures.someDoctor1();

        Mockito.when(doctorService.findDoctorBySurname(doctorSurname))
                .thenReturn(expectedDoctor);

        // when
        Doctor resultDoctor = doctorService.findDoctorBySurname(doctorSurname);

        // then
        Assertions.assertThat(resultDoctor).isEqualTo(expectedDoctor);

    }

    @Test
    @DisplayName("That method should return correct MedicalAppointment to Cancel")
    void cancelAppointmentShouldReturnCorrectMedicalAppointmentToCancel() {
        // given
        MedicalAppointment medicalAppointmentExpected = EntityFixtures.someMedicalAppointment();
        MedicalAppointmentRequest request = EntityFixtures.someMedicalAppointmentRequest();

        Mockito.when(medicalAppointmentService.cancelAppointment(request))
                .thenReturn(medicalAppointmentExpected);

        // when
        MedicalAppointment medicalAppointmentResult = medicalAppointmentService.cancelAppointment(request);

        // then
        Assertions.assertThat(medicalAppointmentResult).isEqualTo(medicalAppointmentExpected);

    }

}