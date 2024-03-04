package pl.szczecin.api.controller.mvc.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.*;
import pl.szczecin.util.EntityFixtures;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PatientCancelControllerMockitoTest {

    @Mock
    private DoctorService doctorService;
    @Mock
    private PatientService patientService;
    @Mock
    private MedicalAppointmentService medicalAppointmentService;
    @Mock
    private PatientMapper patientMapper;
    @Mock
    private MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;

    @InjectMocks
    private PatientCancelController patientCancelController;


    @BeforeEach
    public void setUp() {
        System.out.println("checking for nulls");
        assertNotNull(doctorService);
        assertNotNull(patientService);
        assertNotNull(medicalAppointmentService);
        assertNotNull(patientMapper);
        assertNotNull(medicalAppointmentRequestMapper);
    }

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

        Mockito.verify(patientService, Mockito.times(1))
                .getLoggedInPatientEmail();

        Mockito.verifyNoInteractions(doctorService);
        Mockito.verifyNoInteractions(medicalAppointmentService);
        Mockito.verifyNoInteractions(patientMapper);
        Mockito.verifyNoInteractions(medicalAppointmentRequestMapper);

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

        Mockito.when(patientService.findPatientAppointmentsToCancelByEmail(patientEmail))
                .thenReturn(expectedPatientHistory);

        // when
        PatientHistory resultPatientHistory = patientService
                .findPatientAppointmentsToCancelByEmail(patientEmail);

        // then
        Assertions.assertThat(resultPatientHistory).isEqualTo(expectedPatientHistory);

        Mockito.verify(patientService, Mockito.times(1))
                .findPatientAppointmentsToCancelByEmail(Mockito.anyString());
        Mockito.verify(patientService, Mockito.never())
                .findPatientAppointmentsToCancelByEmail("other.email@clinic.pl");

        Mockito.verifyNoInteractions(doctorService);
        Mockito.verifyNoInteractions(medicalAppointmentService);
        Mockito.verifyNoInteractions(patientMapper);
        Mockito.verifyNoInteractions(medicalAppointmentRequestMapper);

    }

    @Test
    @DisplayName("That method should correctly mapped patientHistory to patientHistoryDTO")
    void patientMapperShouldReturnCorrectMappedPatientHistory() {
        // given
        PatientHistory somePatientHistory = EntityFixtures.somePatientHistory();
        PatientHistoryDTO expectedPatientHistoryDTO = EntityFixtures.somePatientHistoryDTO();

        Mockito.when(patientMapper.map(Mockito.any(PatientHistory.class)))
                .thenReturn(expectedPatientHistoryDTO);

        // when
        PatientHistoryDTO resultPatientHistoryDTO = patientMapper.map(somePatientHistory);

        // then
        Assertions.assertThat(resultPatientHistoryDTO).isEqualTo(expectedPatientHistoryDTO);

        Mockito.verify(patientMapper, Mockito.times(1))
                .map(Mockito.any(PatientHistory.class));
        Mockito.verify(patientMapper, Mockito.never())
                .map(somePatientHistory.withPatientEmail("other.email@clinic.pl"));

        Mockito.verifyNoInteractions(doctorService);
        Mockito.verifyNoInteractions(patientService);
        Mockito.verifyNoInteractions(medicalAppointmentService);
        Mockito.verifyNoInteractions(medicalAppointmentRequestMapper);

    }


    // metoda POST
    @Test
    @DisplayName("POST - That method should correctly mapped MedicalAppointmentRequestDTO to MedicalAppointmentRequest")
    void medicalAppointmentRequestMapperShouldReturnCorrectRequest() {
        // given
        String patientName = "Agnieszka";
        String patientSurname = "Testowa";
        String patientEmail = "patient@clinic.pl";
        String doctorEmail = "doctor@clinic.pl";
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
                        .patientEmail(patientEmail)
                        .doctorEmail(doctorEmail)
                        .build()
        );

        // then
        Assertions.assertThat(resultRequest).isEqualTo(expectedRequest);

        Mockito.verify(medicalAppointmentRequestMapper, Mockito.times(1))
                .map(Mockito.any(MedicalAppointmentRequestDTO.class));

        Mockito.verifyNoInteractions(doctorService);
        Mockito.verifyNoInteractions(patientService);
        Mockito.verifyNoInteractions(medicalAppointmentService);
        Mockito.verifyNoInteractions(patientMapper);
    }

    @Test
    @DisplayName("POST - That method should return correct Patient")
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

        Mockito.verify(patientService, Mockito.times(1))
                .findPatientByEmail(Mockito.anyString());
        Mockito.verify(patientService, Mockito.never())
                .findPatientByEmail("other.email@clinic.pl");

        Mockito.verifyNoInteractions(doctorService);
        Mockito.verifyNoInteractions(medicalAppointmentService);
        Mockito.verifyNoInteractions(patientMapper);
        Mockito.verifyNoInteractions(medicalAppointmentRequestMapper);

    }

    @Test
    @DisplayName("That method should return correct Doctor")
    void findDoctorByEmailShouldReturnCorrectDoctor() {
        // given
        String doctorEmail = "doctor@clinic.pl";
        Doctor expectedDoctor = EntityFixtures.someDoctor1();

        Mockito.when(doctorService.findDoctorByEmail(doctorEmail))
                .thenReturn(expectedDoctor);

        // when
        Doctor resultDoctor = doctorService.findDoctorByEmail(doctorEmail);

        // then
        Assertions.assertThat(resultDoctor).isEqualTo(expectedDoctor);

        Mockito.verify(doctorService, Mockito.times(1))
                .findDoctorByEmail(Mockito.anyString());
        Mockito.verify(doctorService, Mockito.never())
                .findDoctorByEmail("other.email@clinic.pl");

        Mockito.verifyNoInteractions(patientService);
        Mockito.verifyNoInteractions(medicalAppointmentService);
        Mockito.verifyNoInteractions(patientMapper);
        Mockito.verifyNoInteractions(medicalAppointmentRequestMapper);

    }

    @Test
    @DisplayName("That method should return correct MedicalAppointment to Cancel")
    void cancelAppointmentShouldReturnCorrectMedicalAppointmentToCancel() {
        // given
        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment();
        MedicalAppointmentRequest request = EntityFixtures.someMedicalAppointmentRequest();

        Mockito.when(medicalAppointmentService.cancelAppointment(request))
                .thenReturn(expectedMedicalAppointment);

        // when
        MedicalAppointment resultMedicalAppointment = medicalAppointmentService.cancelAppointment(request);

        // then
        Assertions.assertThat(resultMedicalAppointment).isEqualTo(expectedMedicalAppointment);

        Mockito.verify(medicalAppointmentService, Mockito.times(1))
                .cancelAppointment(Mockito.any(MedicalAppointmentRequest.class));
        Mockito.verify(medicalAppointmentService, Mockito.never())
                .cancelAppointment(request.withDoctorEmail("other.email@clinic.pl"));

        Mockito.verifyNoInteractions(patientService);
        Mockito.verifyNoInteractions(doctorService);
        Mockito.verifyNoInteractions(patientMapper);
        Mockito.verifyNoInteractions(medicalAppointmentRequestMapper);

    }

}