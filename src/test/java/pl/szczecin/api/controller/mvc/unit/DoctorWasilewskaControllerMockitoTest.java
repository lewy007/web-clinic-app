package pl.szczecin.api.controller.mvc.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import pl.szczecin.api.controller.DoctorWasilewskaController;
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
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


@ExtendWith(MockitoExtension.class)
class DoctorWasilewskaControllerMockitoTest {


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
    private PatientService patientService;

    @InjectMocks
    private DoctorWasilewskaController doctorWasilewskaController;


    @Test
    @DisplayName("That method should return correct view")
    public void doctorWasilewskaPageShouldReturnCorrectViewName() {
        // given
        String patientEmail = "patient@example.com";
        String doctorSurname = "Wasilewska";
        Doctor expectedDoctor = EntityFixtures.someDoctor1();

        ExtendedModelMap model = new ExtendedModelMap();

        Mockito.when(patientService.getLoggedInPatientEmail()).thenReturn(patientEmail);
        Mockito.when(doctorService.findDoctorBySurname(doctorSurname)).thenReturn(expectedDoctor);

        // when
        String resultView = doctorWasilewskaController.medicalAppointmentPageToDoctorWasilewska(model);

        // then
        Assertions.assertThat("doctor_wasilewska_portal").isEqualTo(resultView);
    }


    @Test
    @DisplayName("That method should return correct email of logged in patient")
    void getLoggedInPatientEmailShouldReturnCorrectEmail() {
        // given
        String expectedEmail = "test@example.com";
        Mockito.when(patientService.getLoggedInPatientEmail()).thenReturn(expectedEmail);

        // when
        String result = patientService.getLoggedInPatientEmail();

        // then
        Assertions.assertThat(result).isEqualTo(expectedEmail);
    }


    @Test
    @DisplayName("That method should return correct doctor email")
    void getDoctorWasilewskaEmailShouldReturnCorrectDoctorEmail() {
        // given
        String doctorSurname = "Wasilewska";
        String expectedEmail = "edyta.kowalska@clinic.pl";
        Doctor expectedDoctor = EntityFixtures.someDoctor1();

        Mockito.when(doctorService.findDoctorBySurname(doctorSurname)).thenReturn(expectedDoctor);

        // when
        String resultEmail = doctorService.findDoctorBySurname(doctorSurname).getEmail();

        // then
        Assertions.assertThat(resultEmail).isEqualTo(expectedEmail);
    }

    @Test
    @DisplayName("That method should return correct dates for Doctor Wasilewska")
    void getAvailableDatesForDoctorWasilewskaShouldReturnDates() {
        // given
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

        // when
        var availableDates =
                medicalAppointmentDateService.getAvailableDatesByDoctorEmail(doctorEmail).stream()
                        .map(medicalAppointmentDateMapper::map)
                        .map(MedicalAppointmentDateDTO::getDateTime)
                        .toList();

        // then
        Assertions.assertThat(availableDates)
                .containsExactly("2023-11-15 10:00:00", "2023-11-16 10:00:00", "2023-11-17 10:00:00");
        Assertions.assertThat(availableDates).size().isEqualTo(3);

    }


    @Test
    @DisplayName("That method should correctly mapped MedicalAppointmentRequestDTO to MedicalAppointmentRequest")
    void medicalAppointmentRequestMapperShouldReturnCorrectRequest() {
        // given
        String patientEmail = "patient2@clinic.pl";
        String doctorEmail = "doctor2@clinic.pl";
        String date = "2022-08-27 09:28:00";

        MedicalAppointmentRequest expectedRequest =
                EntityFixtures.someMedicalAppointmentRequest();

        Mockito.when(medicalAppointmentRequestMapper.map(Mockito.any(MedicalAppointmentRequestDTO.class)))
                .thenReturn(expectedRequest);

        // when
        MedicalAppointmentRequest resultRequest = medicalAppointmentRequestMapper.map(
                MedicalAppointmentRequestDTO.builder()
                        .patientEmail(patientEmail)
                        .medicalAppointmentDate(date)
                        .doctorEmail(doctorEmail)
                        .build()
        );

        // then
        Assertions.assertThat(resultRequest).isEqualTo(expectedRequest);
    }

    @Test
    @DisplayName("That method should return correct a medical appointment")
    void medicalAppointmentServiceShouldReturnCorrectMedicalAppointment() {
        // given

        MedicalAppointmentRequest request = EntityFixtures.someMedicalAppointmentRequest();
        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment();

        Mockito.when(medicalAppointmentService.makeAppointment(request)).thenReturn(expectedMedicalAppointment);

        // when
        MedicalAppointment resultMedicalAppointment =
                medicalAppointmentService.makeAppointment(request);
        // then
        Assertions.assertThat(resultMedicalAppointment).isEqualTo(expectedMedicalAppointment);
    }

}