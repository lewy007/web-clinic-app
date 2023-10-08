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
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.util.EntityFixtures;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class DoctorControllerMockitoTest {

    @Mock
    private DoctorService doctorService;
    @Mock
    private MedicalAppointmentService medicalAppointmentService;
    @Mock
    private MedicalAppointmentMapper medicalAppointmentMapper;
    @Mock
    private MedicalAppointmentDateService medicalAppointmentDateService;

    @InjectMocks
    private DoctorController doctorController;


    @Test
    @DisplayName("That method should return correct view")
    public void doctorPageShouldReturnCorrectViewName() {
        // given
        ExtendedModelMap model = new ExtendedModelMap();

        // when
        String resultView = doctorController.doctorPage(model);

        // then
        Assertions.assertThat("doctor_portal").isEqualTo(resultView);

    }

    @Test
    @DisplayName("That method should return correct email of logged in doctor")
    public void getLoggedInDoctorEmailShouldReturnCorrectEmail() {
        // given
        String doctorEmail = "doctor.test@clinic.pl";
        Mockito.when(doctorService.getLoggedInDoctorEmail()).thenReturn(doctorEmail);

        // when
        String result = doctorService.getLoggedInDoctorEmail();

        // then
        Assertions.assertThat(result).isEqualTo(doctorEmail);

    }

    @Test
    @DisplayName("That method should return correct attributes of model")
    void doctorPageShouldAddAttributesToModel() {
        // Given
        ExtendedModelMap model = new ExtendedModelMap();
//        Mockito.when(doctorService.getLoggedInDoctorEmail()).thenReturn("test@example.com");

        // When
        doctorController.doctorPage(model);

        // Then
        Assertions.assertThat(model).containsKeys("loggedInDoctorEmail", "medicalAppointmentDTOs");
    }

    @Test
    @DisplayName("That method should return correct IDs of future medical appointment dates for Doctor")
    void getAllFutureMedicalAppointmentDateIdsByDoctorEmailShouldReturnCorrectIds() {
        // given
        String doctorEmail = "doctor.test@clinic.pl";
        List<MedicalAppointmentDate> medicalAppointmentDateList = List.of(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate2(),
                EntityFixtures.someMedicalAppointmentDate3()
        );

        Mockito.when(medicalAppointmentDateService.getAllFutureDatesByDoctorEmail(doctorEmail))
                .thenReturn(medicalAppointmentDateList);

        // When
        List<Integer> result = medicalAppointmentDateService
                .getAllFutureDatesByDoctorEmail(doctorEmail).stream()
                .map(MedicalAppointmentDate::getMedicalAppointmentDateId)
                .toList();

        // Then
        Assertions.assertThat(result).containsExactly(1, 2, 3);
        Assertions.assertThat(result).size().isEqualTo(3);

    }


    @Test
    @DisplayName("That method should return correct future medical appointments")
    void findAllMedicalAppointmentByMADateIDShouldReturnCorrectMedicalAppointments() {
        // Given
        List<Integer> medicalAppointmentDateIds = List.of(1, 2, 3);

        List<MedicalAppointment> medicalAppointmentList = List.of(
                EntityFixtures.someMedicalAppointment1(),
                EntityFixtures.someMedicalAppointment2(),
                EntityFixtures.someMedicalAppointment3()
        );
        List<MedicalAppointmentDTO> medicalAppointmentDTOs = List.of(
                EntityFixtures.someMedicalAppointmentDTO1(),
                EntityFixtures.someMedicalAppointmentDTO2(),
                EntityFixtures.someMedicalAppointmentDTO3()
        );


        Mockito.when(medicalAppointmentService.findAllMedicalAppointmentByMADateID(medicalAppointmentDateIds))
                .thenReturn(medicalAppointmentList);
        Mockito.when(medicalAppointmentMapper.map(Mockito.any(MedicalAppointment.class)))
                .thenReturn(medicalAppointmentDTOs.get(0))
                .thenReturn(medicalAppointmentDTOs.get(1))
                .thenReturn(medicalAppointmentDTOs.get(2));

        // When
        List<MedicalAppointmentDTO> result = medicalAppointmentService
                .findAllMedicalAppointmentByMADateID(medicalAppointmentDateIds).stream()
                .map(medicalAppointmentMapper::map)
                .toList();

        // Then
        Assertions.assertThat(result).containsExactlyElementsOf(medicalAppointmentDTOs);
    }
}
