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
import pl.szczecin.api.controller.DoctorAvailableDatesController;
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentDateMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.util.EntityFixtures;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class DoctorAvailableDatesControllerMockitoTest {


    @Mock
    private DoctorService doctorService;
    @Mock
    private MedicalAppointmentDateService medicalAppointmentDateService;
    @Mock
    private MedicalAppointmentDateMapper medicalAppointmentDateMapper;

    @InjectMocks
    private DoctorAvailableDatesController doctorAvailableDatesController;


    @Test
    @DisplayName("That method should return correct view")
    public void doctorPageShouldReturnCorrectViewName() {
        // given
        ExtendedModelMap model = new ExtendedModelMap();

        // when
        String resultView = doctorAvailableDatesController.doctorAvailableDatesPage(model);

        // then
        Assertions.assertThat("doctor_available_dates").isEqualTo(resultView);

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
    @DisplayName("That method should return correct dates of logged in doctor")
    public void getAvailableDatesByDoctorEmailShouldReturnCorrectDates() {
        // given
        String doctorEmail = "doctor.test@clinic.pl";
        List<MedicalAppointmentDate> medicalAppointmentDateList = List.of(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate2(),
                EntityFixtures.someMedicalAppointmentDate3()
        );
        List<MedicalAppointmentDateDTO> medicalAppointmentDTOs = List.of(
                EntityFixtures.someMedicalAppointmentDateDTO1(),
                EntityFixtures.someMedicalAppointmentDateDTO2(),
                EntityFixtures.someMedicalAppointmentDateDTO3()
        );
        List<String> expectedDatesList = List.of(
                "2023-11-15 10:00:00",
                "2023-11-16 10:00:00",
                "2023-11-17 10:00:00"
        );
        Mockito.when(medicalAppointmentDateService
                .getAvailableDatesByDoctorEmail(doctorEmail)).thenReturn(medicalAppointmentDateList);
        Mockito.when(medicalAppointmentDateMapper.map(Mockito.any(MedicalAppointmentDate.class)))
                .thenReturn(medicalAppointmentDTOs.get(0))
                .thenReturn(medicalAppointmentDTOs.get(1))
                .thenReturn(medicalAppointmentDTOs.get(2));

        // when
        List<String> resultDatesList = medicalAppointmentDateService
                .getAvailableDatesByDoctorEmail(doctorEmail).stream()
                .map(medicalAppointmentDateMapper::map)
                .map(MedicalAppointmentDateDTO::getDateTime)
                .toList();

        // then
//        Assertions.assertThat(resultDatesList).isEqualTo(ex);
        Assertions.assertThat(resultDatesList).containsExactlyElementsOf(expectedDatesList);

    }

}
