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
import pl.szczecin.api.controller.rest.DoctorScheduleRestController;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.api.dto.MedicalAppointmentsDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.util.EntityFixtures;

import java.util.List;

import static pl.szczecin.util.EntityFixtures.*;

@ExtendWith(MockitoExtension.class)
class DoctorScheduleRestControllerMockitoTest {


    @Mock
    private MedicalAppointmentService medicalAppointmentService;
    @Mock
    private MedicalAppointmentMapper medicalAppointmentMapper;
    @Mock
    private MedicalAppointmentDateService medicalAppointmentDateService;
    @Mock
    private MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;

    @InjectMocks
    private DoctorScheduleRestController doctorScheduleRestController;


    @BeforeEach
    public void setUp() {
        System.out.println("checking for nulls");
        Assertions.assertNotNull(medicalAppointmentService);
        Assertions.assertNotNull(medicalAppointmentMapper);
        Assertions.assertNotNull(medicalAppointmentDateService);
        Assertions.assertNotNull(medicalAppointmentRequestMapper);
    }


    @Test
    @DisplayName("GET - That method should return correct all future medical appointment date Ids for doctor")
    void shouldReturnCorrectAllFutureMedicalAppointmentDateIdsForDoctor() throws Exception {
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

        Mockito.when(medicalAppointmentDateService.getAllFutureDatesByDoctorEmail(doctorEmail))
                .thenReturn(medicalAppointmentDateList);

        //when
        List<Integer> result = medicalAppointmentDateService.getAllFutureDatesByDoctorEmail(doctorEmail).stream()
                .map(MedicalAppointmentDate::getMedicalAppointmentDateId)
                .toList();

        //then
        Assertions.assertEquals(result.size(), expectedList.size());
        Assertions.assertEquals(result, expectedList);
        //lista z inna iloscia elementow nie jest rowna, wiec test przechodzi
        Assertions.assertNotEquals(result, notExpectedList);

        Mockito.verify(medicalAppointmentDateService, Mockito.times(1))
                .getAllFutureDatesByDoctorEmail(Mockito.anyString());
        Mockito.verify(medicalAppointmentDateService, Mockito.never())
                .getAllFutureDatesByDoctorEmail("other.email@clinic.pl");

        Mockito.verifyNoInteractions(medicalAppointmentService);
        Mockito.verifyNoInteractions(medicalAppointmentMapper);
        Mockito.verifyNoInteractions(medicalAppointmentRequestMapper);
    }

    @Test
    @DisplayName("GET - That method should return correct all history medical appointment DTO for doctor")
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
        Mockito.verifyNoInteractions(medicalAppointmentRequestMapper);
    }

    @Test
    @DisplayName("GET - That method should return correct medical appointment schedule " +
            "for doctor - list of (MedicalAppointmentsDTO)")
    void shouldReturnCorrectFutureMedicalAppointmentsDTOForDoctor() throws Exception {
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


        Mockito.when(medicalAppointmentDateService.getAllFutureDatesByDoctorEmail(doctorEmail))
                .thenReturn(medicalAppointmentDateList);
        Mockito.when(medicalAppointmentService.findAllMedicalAppointmentByMADateID(someIds))
                .thenReturn(someMedicalAppointmentList);
        Mockito.when(medicalAppointmentMapper.map(Mockito.any(MedicalAppointment.class)))
                .thenReturn(expectedMedicalAppointmentsDTO.getMedicalAppointmentsDTO().get(0))
                .thenReturn(expectedMedicalAppointmentsDTO.getMedicalAppointmentsDTO().get(1))
                .thenReturn(expectedMedicalAppointmentsDTO.getMedicalAppointmentsDTO().get(2));

        //when
        MedicalAppointmentsDTO result = doctorScheduleRestController.doctorMedicalAppointmentSchedule(doctorEmail);

        //then
        Assertions.assertEquals(result.getMedicalAppointmentsDTO().size(),
                expectedMedicalAppointmentsDTO.getMedicalAppointmentsDTO().size());
        Assertions.assertEquals(result, expectedMedicalAppointmentsDTO);

        //lista z inna iloscia elementow nie jest rowna, wiec test przechodzi
        Assertions.assertNotEquals(result, notExpectedMedicalAppointmentsDTO);

        Mockito.verify(medicalAppointmentDateService, Mockito.times(1))
                .getAllFutureDatesByDoctorEmail(Mockito.anyString());
        Mockito.verify(medicalAppointmentDateService, Mockito.never())
                .getAllFutureDatesByDoctorEmail("other.email@clinic.pl");
        Mockito.verify(medicalAppointmentService, Mockito.times(1))
                .findAllMedicalAppointmentByMADateID(someIds);
        Mockito.verify(medicalAppointmentService, Mockito.never())
                .findAllMedicalAppointmentByMADateID(List.of(4, 5, 6));
        Mockito.verify(medicalAppointmentMapper, Mockito.times(3))
                .map(Mockito.any(MedicalAppointment.class));

        Mockito.verifyNoInteractions(medicalAppointmentRequestMapper);
    }


    @Test
    @DisplayName("PATCH - That method should return correct medical appointment with added note.")
    void shouldReturnCorrectMedicalAppointmentWithAddedNote() {
        //given
        MedicalAppointmentRequest request = someMedicalAppointmentRequest();
        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment1();

        Mockito.when(medicalAppointmentService.addNoteToMedicalAppointment(request))
                .thenReturn(expectedMedicalAppointment);

        //when
        MedicalAppointment result = medicalAppointmentService.addNoteToMedicalAppointment(request);

        //then
        Assertions.assertEquals(result, expectedMedicalAppointment);
        Assertions.assertNotEquals(result.getDoctorNote(), "some not equal note");

        Mockito.verify(medicalAppointmentService, Mockito.times(1))
                .addNoteToMedicalAppointment(Mockito.any(MedicalAppointmentRequest.class));
        Mockito.verify(medicalAppointmentService, Mockito.never())
                .addNoteToMedicalAppointment(request.withPatientEmail("other.email@clinic.pl"));

        Mockito.verifyNoInteractions(medicalAppointmentRequestMapper);
        Mockito.verifyNoInteractions(medicalAppointmentDateService);
        Mockito.verifyNoInteractions(medicalAppointmentMapper);
    }

}
