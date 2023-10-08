package pl.szczecin.api.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.mapper.DoctorMapper;
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


@ExtendWith(MockitoExtension.class)
class DoctorTorbeControllerMockitoTest {


    @Mock
    private MedicalAppointmentService medicalAppointmentService;
    @Mock
    private MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    @Mock
    private MedicalAppointmentDateService medicalAppointmentDateService;
    @Mock
    private MedicalAppointmentDateMapper medicalAppointmentDateMapper;
    @Mock
    private DoctorService doctorService;
    @Mock
    private DoctorMapper doctorMapper;
    @Mock
    private PatientService patientService;

    @InjectMocks
    private DoctorTorbeController doctorTorbeController;


    @Test
    @DisplayName("That method should return correct view")
    public void doctorTorbePageShouldReturnCorrectViewName() {
        // given
        String patientEmail = "patient@example.com";
        String doctorSurname = "Torbe";
        Doctor expectedDoctor = EntityFixtures.someDoctor1();

        ExtendedModelMap model = new ExtendedModelMap();

        Mockito.when(patientService.getLoggedInPatientEmail()).thenReturn(patientEmail);
        Mockito.when(doctorService.findDoctorBySurname(doctorSurname)).thenReturn(expectedDoctor);

        // when
        String resultView = doctorTorbeController.medicalAppointmentPageToDoctorTorbe(model);

        // then
        Assertions.assertThat("doctor_torbe_portal").isEqualTo(resultView);
    }


    @Test
    @DisplayName("That method should return correct email of logged in patient")
    void getLoggedInPatientEmailShouldReturnCorrectEmail() {
        // Given
        String expectedEmail = "test@example.com";
        Mockito.when(patientService.getLoggedInPatientEmail()).thenReturn(expectedEmail);

        // When
        String result = patientService.getLoggedInPatientEmail();

        // Then
        Assertions.assertThat(result).isEqualTo(expectedEmail);
    }


    @Test
    @DisplayName("That method should return correct doctor email")
    void getDoctorTorbeEmailShouldReturnCorrectDoctorEmail() {
        // Given
        String doctorSurname = "Torbe";
        String expectedEmail = "edyta.kowalska@clinic.pl";
        Doctor expectedDoctor = EntityFixtures.someDoctor1();

        Mockito.when(doctorService.findDoctorBySurname(doctorSurname)).thenReturn(expectedDoctor);

        // When
        String resultEmail = doctorService.findDoctorBySurname(doctorSurname).getEmail();

        // Then
        Assertions.assertThat(resultEmail).isEqualTo(expectedEmail);
    }

    @Test
    @DisplayName("That method should return correct dates for Doctor Torbe")
    void getAvailableDatesForDoctorTorbeShouldReturnDates() {
        // Given
        String doctorEmail = "test@example.com";

        List<MedicalAppointmentDate> medicalAppointmentDateList =
                List.of(
                        EntityFixtures.someMedicalAppointmentDate1(),
                        EntityFixtures.someMedicalAppointmentDate2(),
                        EntityFixtures.someMedicalAppointmentDate3()
                );

        List<MedicalAppointmentDateDTO> medicalAppointmentDateDTOs =
                List.of(
                        EntityFixtures.someMedicalAppointmentDateDTO1(),
                        EntityFixtures.someMedicalAppointmentDateDTO2(),
                        EntityFixtures.someMedicalAppointmentDateDTO3()
                );

        Mockito.when(medicalAppointmentDateService.getAvailableDatesByDoctorEmail(doctorEmail))
                .thenReturn(medicalAppointmentDateList);
        Mockito.when(medicalAppointmentDateMapper.map(Mockito.any(MedicalAppointmentDate.class)))
                .thenReturn(medicalAppointmentDateDTOs.get(0))
                .thenReturn(medicalAppointmentDateDTOs.get(1))
                .thenReturn(medicalAppointmentDateDTOs.get(2));

        // When
        var availableDates =
                medicalAppointmentDateService.getAvailableDatesByDoctorEmail(doctorEmail).stream()
                        .map(medicalAppointmentDateMapper::map)
                        .map(MedicalAppointmentDateDTO::getDateTime)
                        .toList();

        // Then
        Assertions.assertThat(availableDates)
                .containsExactly("2023-11-15 10:00:00", "2023-11-16 10:00:00", "2023-11-17 10:00:00");
        Assertions.assertThat(availableDates).size().isEqualTo(3);

    }


    @Test
    @DisplayName("That method should correctly mapped MedicalAppointmentRequestDTO to MedicalAppointmentRequest")
    void medicalAppointmentRequestMapperShouldReturnCorrectRequest() {
        // Given
        String patientEmail = "patient2@clinic.pl";
        String doctorEmail = "doctor2@clinic.pl";
        String date = "2022-08-27 09:28:00";

        MedicalAppointmentRequest expectedRequest =
                EntityFixtures.someMedicalAppointmentRequest();

        Mockito.when(medicalAppointmentRequestMapper.map(Mockito.any(MedicalAppointmentRequestDTO.class)))
                .thenReturn(expectedRequest);

        // When
        MedicalAppointmentRequest resultRequest = medicalAppointmentRequestMapper.map(
                MedicalAppointmentRequestDTO.builder()
                        .patientEmail(patientEmail)
                        .medicalAppointmentDate(date)
                        .doctorEmail(doctorEmail)
                        .build()
        );

        // Then
        Assertions.assertThat(resultRequest).isEqualTo(expectedRequest);
    }

    @Test
    @DisplayName("That method should return correct a medical appointment")
    void medicalAppointmentServiceShouldReturnCorrectMedicalAppointment() {
        // Given

        MedicalAppointmentRequest request = EntityFixtures.someMedicalAppointmentRequest();
        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment();

        Mockito.when(medicalAppointmentService.makeAppointment(request)).thenReturn(expectedMedicalAppointment);

        // When
        MedicalAppointment resultMedicalAppointment =
                medicalAppointmentService.makeAppointment(request);
        // Then
        Assertions.assertThat(resultMedicalAppointment).isEqualTo(expectedMedicalAppointment);
    }

}