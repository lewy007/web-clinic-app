package pl.szczecin.api.controller.rest.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szczecin.api.controller.rest.DoctorAvailableDatesRestController;
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentDateMapper;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.domain.Patient;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.util.EntityFixtures;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class DoctorAvailableDatesRestControllerMockitoTest {

    @Mock
    private MedicalAppointmentDateService medicalAppointmentDateService;
    @Mock
    private MedicalAppointmentDateMapper medicalAppointmentDateMapper;

    @InjectMocks
    private DoctorAvailableDatesRestController doctorAvailableDatesRestController;


    @BeforeEach
    public void setUp() {
        System.out.println("checking for nulls");
        Assertions.assertNotNull(medicalAppointmentDateService);
        Assertions.assertNotNull(medicalAppointmentDateMapper);
    }

    @Test
    @DisplayName("GET - That method should return correct available appointment dates for selected doctor")
    void shouldReturnCorrectAvailableAppointmentDatesByDoctorEmail() throws Exception {
        //given
        String doctorEmail = "anna.nowak@clinic.pl";
        List<String> expectedDates = List.of(
                "2023-11-15 10:00:00",
                "2023-11-16 10:00:00",
                "2023-11-17 10:00:00"
        );
        List<String> notExpectedDates = List.of(
                "2023-12-15 10:00:00",
                "2023-12-16 10:00:00",
                "2023-12-17 10:00:00"
        );

        // dane do mockowania
        List<MedicalAppointmentDate> medicalAppointmentDateList = List.of(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate2(),
                EntityFixtures.someMedicalAppointmentDate3()
        );
        List<MedicalAppointmentDateDTO> medicalAppointmentDateDTOList = List.of(
                EntityFixtures.someMedicalAppointmentDateDTO1(),
                EntityFixtures.someMedicalAppointmentDateDTO2(),
                EntityFixtures.someMedicalAppointmentDateDTO3()
        );


        Mockito.when(medicalAppointmentDateService.getAvailableDatesByDoctorEmail(doctorEmail))
                .thenReturn(medicalAppointmentDateList);
        Mockito.when(medicalAppointmentDateMapper.map(Mockito.any(MedicalAppointmentDate.class)))
                .thenReturn(medicalAppointmentDateDTOList.get(0))
                .thenReturn(medicalAppointmentDateDTOList.get(1))
                .thenReturn(medicalAppointmentDateDTOList.get(2));

        //when
        List<String> result = doctorAvailableDatesRestController.availableDatesForDoctor(doctorEmail);

        //then
        Assertions.assertEquals(result.size(), expectedDates.size());
        Assertions.assertEquals(result, expectedDates);

        //lista z inna iloscia elementow nie jest rowna, wiec test przechodzi
        Assertions.assertNotEquals(result, notExpectedDates);

        Mockito.verify(medicalAppointmentDateService, Mockito.times(1))
                .getAvailableDatesByDoctorEmail(Mockito.anyString());
        Mockito.verify(medicalAppointmentDateService, Mockito.never())
                .getAvailableDatesByDoctorEmail("other.email@clinic.pl");
        Mockito.verify(medicalAppointmentDateMapper, Mockito.times(3))
                .map(Mockito.any(MedicalAppointmentDate.class));
    }

}
