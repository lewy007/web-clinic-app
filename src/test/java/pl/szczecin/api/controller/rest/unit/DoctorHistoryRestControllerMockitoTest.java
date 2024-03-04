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
import pl.szczecin.api.controller.rest.DoctorHistoryRestController;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.api.dto.MedicalAppointmentsDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.util.EntityFixtures;

import java.util.List;

import static pl.szczecin.util.EntityFixtures.*;

@ExtendWith(MockitoExtension.class)
class DoctorHistoryRestControllerMockitoTest {


    @Mock
    private MedicalAppointmentService medicalAppointmentService;
    @Mock
    private MedicalAppointmentMapper medicalAppointmentMapper;
    @Mock
    private MedicalAppointmentDateService medicalAppointmentDateService;

    @InjectMocks
    private DoctorHistoryRestController doctorHistoryRestController;


    @BeforeEach
    public void setUp() {
        System.out.println("checking for nulls");
        Assertions.assertNotNull(medicalAppointmentService);
        Assertions.assertNotNull(medicalAppointmentMapper);
        Assertions.assertNotNull(medicalAppointmentDateService);
    }


    @Test
    @DisplayName("That method should return correct all history medical appointment date Ids for doctor")
    void shouldReturnCorrectAllHistoryMedicalAppointmentDateIdsForDoctor() throws Exception {
        //given
        String doctorEmail = "anna.nowak@clinic.pl";

        List<Integer> expectedList = List.of(1, 2, 3);
        List<Integer> notExpectedList = List.of(1, 2, 4);

        // dane do mockowania
        List<MedicalAppointmentDate> medicalAppointmentDateList = List.of(
                someMedicalAppointmentDate1(),
                someMedicalAppointmentDate2(),
                someMedicalAppointmentDate3()
        );

        Mockito.when(medicalAppointmentDateService.getAllHistoryDatesByDoctorEmail(doctorEmail))
                .thenReturn(medicalAppointmentDateList);

        //when
        List<Integer> result = medicalAppointmentDateService.getAllHistoryDatesByDoctorEmail(doctorEmail).stream()
                .map(MedicalAppointmentDate::getMedicalAppointmentDateId)
                .toList();

        //then
        Assertions.assertEquals(result.size(), expectedList.size());
        Assertions.assertEquals(result, expectedList);
        //lista z inna iloscia elementow nie jest rowna, wiec test przechodzi
        Assertions.assertNotEquals(result, notExpectedList);

        Mockito.verify(medicalAppointmentDateService, Mockito.times(1))
                .getAllHistoryDatesByDoctorEmail(Mockito.anyString());
        Mockito.verify(medicalAppointmentDateService, Mockito.never())
                .getAllHistoryDatesByDoctorEmail("other.email@clinic.pl");

        Mockito.verifyNoInteractions(medicalAppointmentService);
        Mockito.verifyNoInteractions(medicalAppointmentMapper);
    }

    @Test
    @DisplayName("That method should return correct all history medical appointment DTO for doctor")
    void shouldReturnCorrectAllHistoryMedicalAppointmentDTOForDoctor() throws Exception {
        //given
        List<Integer> someIds = List.of(1, 2, 3);

        List<MedicalAppointmentDTO> expectedList = List.of(
                someMedicalAppointmentDTO1(),
                someMedicalAppointmentDTO2(),
                someMedicalAppointmentDTO3()
        );

        List<MedicalAppointmentDTO> notExpectedList = List.of(
                someMedicalAppointmentDTO1(),
                someMedicalAppointmentDTO2()
        );

        // dane do mockowania
        List<MedicalAppointment> someMedicalAppointmentList = List.of(
                someMedicalAppointment1(),
                someMedicalAppointment2(),
                someMedicalAppointment3()
        );

        Mockito.when(medicalAppointmentService.findAllMedicalAppointmentByMADateID(someIds))
                .thenReturn(someMedicalAppointmentList);
        Mockito.when(medicalAppointmentMapper.map(Mockito.any(MedicalAppointment.class)))
                .thenReturn(expectedList.get(0))
                .thenReturn(expectedList.get(1))
                .thenReturn(expectedList.get(2));

        //when
        List<MedicalAppointmentDTO> result = medicalAppointmentService.findAllMedicalAppointmentByMADateID(someIds).stream()
                .map(medicalAppointmentMapper::map)
                .toList();

        //then
        Assertions.assertEquals(result.size(), expectedList.size());
        Assertions.assertEquals(result, expectedList);
        //lista z inna iloscia elementow nie jest rowna, wiec test przechodzi
        Assertions.assertNotEquals(result, notExpectedList);

        Mockito.verify(medicalAppointmentService, Mockito.times(1))
                .findAllMedicalAppointmentByMADateID(someIds);
        Mockito.verify(medicalAppointmentService, Mockito.never())
                .findAllMedicalAppointmentByMADateID(List.of(4, 5, 6));
        Mockito.verify(medicalAppointmentMapper, Mockito.times(3))
                .map(Mockito.any(MedicalAppointment.class));

        Mockito.verifyNoInteractions(medicalAppointmentDateService);
    }

    @Test
    @DisplayName("That method should return correct medical appointment history for doctor - list of (MedicalAppointmentsDTO)")
    void shouldReturnCorrectHistoryMedicalAppointmentsDTOForDoctor() throws Exception {
        //given
        String doctorEmail = "anna.nowak@clinic.pl";
        List<Integer> someIds = List.of(1, 2, 3);

        MedicalAppointmentsDTO expectedMedicalAppointmentsDTO = MedicalAppointmentsDTO.builder()
                .medicalAppointmentsDTO(List.of(
                        EntityFixtures.someMedicalAppointmentDTO1(),
                        EntityFixtures.someMedicalAppointmentDTO2(),
                        EntityFixtures.someMedicalAppointmentDTO2()))
                .build();
        MedicalAppointmentsDTO notExpectedMedicalAppointmentsDTO = MedicalAppointmentsDTO.builder()
                .medicalAppointmentsDTO(List.of(
                        EntityFixtures.someMedicalAppointmentDTO1()))
                .build();

        // dane do mockowania
        List<MedicalAppointmentDate> medicalAppointmentDateList = List.of(
                someMedicalAppointmentDate1(),
                someMedicalAppointmentDate2(),
                someMedicalAppointmentDate3()
        );
        List<MedicalAppointment> someMedicalAppointmentList = List.of(
                someMedicalAppointment1(),
                someMedicalAppointment2(),
                someMedicalAppointment3()
        );


        Mockito.when(medicalAppointmentDateService.getAllHistoryDatesByDoctorEmail(doctorEmail))
                .thenReturn(medicalAppointmentDateList);
        Mockito.when(medicalAppointmentService.findAllMedicalAppointmentByMADateID(someIds))
                .thenReturn(someMedicalAppointmentList);
        Mockito.when(medicalAppointmentMapper.map(Mockito.any(MedicalAppointment.class)))
                .thenReturn(expectedMedicalAppointmentsDTO.getMedicalAppointmentsDTO().get(0))
                .thenReturn(expectedMedicalAppointmentsDTO.getMedicalAppointmentsDTO().get(1))
                .thenReturn(expectedMedicalAppointmentsDTO.getMedicalAppointmentsDTO().get(2));

        //when
        MedicalAppointmentsDTO result = doctorHistoryRestController.doctorMedicalAppointmentHistory(doctorEmail);

        //then
        Assertions.assertEquals(result.getMedicalAppointmentsDTO().size(),
                expectedMedicalAppointmentsDTO.getMedicalAppointmentsDTO().size());
        Assertions.assertEquals(result, expectedMedicalAppointmentsDTO);

        //lista z inna iloscia elementow nie jest rowna, wiec test przechodzi
        Assertions.assertNotEquals(result, notExpectedMedicalAppointmentsDTO);

        Mockito.verify(medicalAppointmentDateService, Mockito.times(1))
                .getAllHistoryDatesByDoctorEmail(Mockito.anyString());
        Mockito.verify(medicalAppointmentDateService, Mockito.never())
                .getAllHistoryDatesByDoctorEmail("other.email@clinic.pl");

        Mockito.verify(medicalAppointmentService, Mockito.times(1))
                .findAllMedicalAppointmentByMADateID(someIds);
        Mockito.verify(medicalAppointmentService, Mockito.never())
                .findAllMedicalAppointmentByMADateID(List.of(4, 5, 6));

        Mockito.verify(medicalAppointmentMapper, Mockito.times(3))
                .map(Mockito.any(MedicalAppointment.class));
    }

}
