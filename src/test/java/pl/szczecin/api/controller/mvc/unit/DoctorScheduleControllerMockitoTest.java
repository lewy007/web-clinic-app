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
import pl.szczecin.api.controller.DoctorHistoryController;
import pl.szczecin.api.controller.DoctorScheduleController;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
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

@ExtendWith(MockitoExtension.class)
class DoctorScheduleControllerMockitoTest {

    @Mock
    private DoctorService doctorService;
    @Mock
    private MedicalAppointmentService medicalAppointmentService;
    @Mock
    private MedicalAppointmentMapper medicalAppointmentMapper;
    @Mock
    private MedicalAppointmentDateService medicalAppointmentDateService;
    @Mock
    private MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;


    @InjectMocks
    private DoctorScheduleController doctorScheduleController;

    @Test
    @DisplayName("That method should return correct view")
    public void doctorSchedulePageShouldReturnCorrectViewName() {
        // given
        ExtendedModelMap model = new ExtendedModelMap();

        // when
        String resultView = doctorScheduleController.doctorSchedulePage(model);

        // then
        Assertions.assertThat("doctor_schedule").isEqualTo(resultView);

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
    void doctorHistoryPageShouldAddAttributesToModel() {
        // given
        ExtendedModelMap model = new ExtendedModelMap();
//        Mockito.when(doctorService.getLoggedInDoctorEmail()).thenReturn("test@example.com");

        // when
        doctorScheduleController.doctorSchedulePage(model);

        // then
        Assertions.assertThat(model).containsKeys("loggedInDoctorEmail", "medicalAppointmentDTOs");
    }

    @Test
    @DisplayName("That method should return correct IDs of future medical appointment dates for Doctor")
    void getFutureMedicalAppointmentDateIdsByDoctorEmailShouldReturnCorrectIds() {
        // given
        String doctorEmail = "doctor.test@clinic.pl";
        List<MedicalAppointmentDate> medicalAppointmentDateList = List.of(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate2(),
                EntityFixtures.someMedicalAppointmentDate3()
        );

        Mockito.when(medicalAppointmentDateService.getAllFutureDatesByDoctorEmail(doctorEmail))
                .thenReturn(medicalAppointmentDateList);

        // when
        List<Integer> result = medicalAppointmentDateService
                .getAllFutureDatesByDoctorEmail(doctorEmail).stream()
                .map(MedicalAppointmentDate::getMedicalAppointmentDateId)
                .toList();

        // then
        Assertions.assertThat(result).containsExactly(1, 2, 3);
        Assertions.assertThat(result).size().isEqualTo(3);

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
    }



    // Metoda POST
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
    @DisplayName("That method should return a correct note doctor")
    void medicalAppointmentServiceShouldCorrectlyAddNoteToMedicalAppointment() {
        // given

        MedicalAppointmentRequest request = EntityFixtures.someMedicalAppointmentRequest();
        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment();

        Mockito.when(medicalAppointmentService.addNoteToMedicalAppointment(request)).thenReturn(expectedMedicalAppointment);

        // when
        MedicalAppointment resultMedicalAppointment =
                medicalAppointmentService.addNoteToMedicalAppointment(request);
        // then
        Assertions.assertThat(resultMedicalAppointment).isEqualTo(expectedMedicalAppointment);
    }

}