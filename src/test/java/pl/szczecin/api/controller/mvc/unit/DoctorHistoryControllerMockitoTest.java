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
import pl.szczecin.api.controller.DoctorHistoryController;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.util.EntityFixtures;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DoctorHistoryControllerMockitoTest {

    @Mock
    private DoctorService doctorService;
    @Mock
    private MedicalAppointmentService medicalAppointmentService;
    @Mock
    private MedicalAppointmentDateService medicalAppointmentDateService;
    @Mock
    private MedicalAppointmentMapper medicalAppointmentMapper;
    @Mock
    private MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;

    @InjectMocks
    private DoctorHistoryController doctorHistoryController;


    @BeforeEach
    public void setUp() {
        System.out.println("checking for nulls");
        assertNotNull(doctorService);
        assertNotNull(medicalAppointmentService);
        assertNotNull(medicalAppointmentDateService);
        assertNotNull(medicalAppointmentMapper);
        assertNotNull(medicalAppointmentRequestMapper);
    }

    @Test
    @DisplayName("That method should return correct view")
    public void doctorHistoryPageShouldReturnCorrectViewName() {
        // given
        ExtendedModelMap model = new ExtendedModelMap();

        // when
        String resultView = doctorHistoryController.doctorHistoryPage(model);

        // then
        Assertions.assertThat("doctor_history").isEqualTo(resultView);

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

        Mockito.verify(doctorService, Mockito.times(1))
                .getLoggedInDoctorEmail();

        Mockito.verifyNoInteractions(medicalAppointmentService);
        Mockito.verifyNoInteractions(medicalAppointmentMapper);
        Mockito.verifyNoInteractions(medicalAppointmentDateService);
        Mockito.verifyNoInteractions(medicalAppointmentRequestMapper);

    }

    @Test
    @DisplayName("That method should return correct attributes of model")
    void doctorHistoryPageShouldAddAttributesToModel() {
        // given
        ExtendedModelMap model = new ExtendedModelMap();
//        Mockito.when(doctorService.getLoggedInDoctorEmail()).thenReturn("test@example.com");

        // when
        doctorHistoryController.doctorHistoryPage(model);

        // then
        Assertions.assertThat(model).containsKeys("loggedInDoctorEmail", "medicalAppointmentDTOs");
    }

    @Test
    @DisplayName("That method should return correct IDs of all medical appointment dates for Doctor")
    void getAllMedicalAppointmentDateIdsByDoctorEmailShouldReturnCorrectIds() {
        // given
        String doctorEmail = "doctor.test@clinic.pl";
        List<MedicalAppointmentDate> medicalAppointmentDateList = List.of(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate2(),
                EntityFixtures.someMedicalAppointmentDate3()
        );

        Mockito.when(medicalAppointmentDateService.getAllHistoryDatesByDoctorEmail(doctorEmail))
                .thenReturn(medicalAppointmentDateList);

        // when
        List<Integer> result = medicalAppointmentDateService
                .getAllHistoryDatesByDoctorEmail(doctorEmail).stream()
                .map(MedicalAppointmentDate::getMedicalAppointmentDateId)
                .toList();

        // then
        Assertions.assertThat(result).containsExactly(1, 2, 3);
        Assertions.assertThat(result).size().isEqualTo(3);

        Mockito.verify(medicalAppointmentDateService, Mockito.times(1))
                .getAllHistoryDatesByDoctorEmail(Mockito.anyString());
        Mockito.verify(medicalAppointmentDateService, Mockito.never())
                .getAllHistoryDatesByDoctorEmail("other.email@clinic.pl");

        Mockito.verifyNoInteractions(doctorService);
        Mockito.verifyNoInteractions(medicalAppointmentService);
        Mockito.verifyNoInteractions(medicalAppointmentMapper);
        Mockito.verifyNoInteractions(medicalAppointmentRequestMapper);

    }

    @Test
    @DisplayName("That method should return correct all medical appointments")
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

        Mockito.verify(medicalAppointmentService, Mockito.times(1))
                .findAllMedicalAppointmentByMADateID(medicalAppointmentDateIds);
        Mockito.verify(medicalAppointmentService, Mockito.never())
                .findAllMedicalAppointmentByMADateID(List.of(4, 5, 6));
        Mockito.verify(medicalAppointmentMapper, Mockito.times(3))
                .map(Mockito.any(MedicalAppointment.class));

        Mockito.verifyNoInteractions(doctorService);
        Mockito.verifyNoInteractions(medicalAppointmentDateService);
        Mockito.verifyNoInteractions(medicalAppointmentRequestMapper);
    }

}