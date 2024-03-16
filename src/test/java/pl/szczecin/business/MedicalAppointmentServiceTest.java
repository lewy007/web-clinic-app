package pl.szczecin.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szczecin.business.dao.MedicalAppointmentDAO;
import pl.szczecin.domain.Doctor;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.domain.exception.NotFoundException;
import pl.szczecin.util.EntityFixtures;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MedicalAppointmentServiceTest {

    @Mock
    private MedicalAppointmentDAO medicalAppointmentDAO;
    @Mock
    private PatientService patientService;
    @Mock
    private DoctorService doctorService;
    @Mock
    private MedicalAppointmentDateService medicalAppointmentDateService;

    @InjectMocks
    private MedicalAppointmentService medicalAppointmentService;


    @BeforeEach
    public void setUp() {
        System.out.println("checking for nulls");
        Assertions.assertNotNull(medicalAppointmentDAO);
        Assertions.assertNotNull(patientService);
        Assertions.assertNotNull(doctorService);
        Assertions.assertNotNull(medicalAppointmentDateService);
    }

    @Test
    @DisplayName("That method should return all medical appointments with given MedicalAppointmentDate ID for Doctor")
    void shouldReturnAllMedicalAppointmentsWithGivenMadId() {
        // given
        List<Integer> medicalAppointmentDateIds = Arrays.asList(1, 2, 3);
        List<MedicalAppointment> expectedMedicalAppointments = Arrays.asList(
                EntityFixtures.someMedicalAppointment1(),
                EntityFixtures.someMedicalAppointment2(),
                EntityFixtures.someMedicalAppointment3()
        );
        List<MedicalAppointment> notExpectedMedicalAppointments = Arrays.asList(
                EntityFixtures.someMedicalAppointment1(),
                EntityFixtures.someMedicalAppointment2()
        );

        Mockito.when(medicalAppointmentDAO.findAllMedicalAppointmentByMADateID(medicalAppointmentDateIds))
                .thenReturn(expectedMedicalAppointments);

        // when
        List<MedicalAppointment> result = medicalAppointmentService
                .findAllMedicalAppointmentByMADateID(medicalAppointmentDateIds);

        // then
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(expectedMedicalAppointments, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointments, result);

        Mockito.verify(medicalAppointmentDAO, Mockito.times(1))
                .findAllMedicalAppointmentByMADateID(medicalAppointmentDateIds);
        Mockito.verify(medicalAppointmentDAO, Mockito.never())
                .findAllMedicalAppointmentByMADateID(List.of(4, 5, 6));

        Mockito.verifyNoInteractions(patientService);
        Mockito.verifyNoInteractions(doctorService);
        Mockito.verifyNoInteractions(medicalAppointmentDateService);
    }

    @Test
    @DisplayName("That method should correctly make appointment and return medical appointment with given request")
    void shouldAddedAndReturnCorrectMedicalAppointment() {
        // given
        Doctor someDoctor = EntityFixtures.someDoctor1();
        MedicalAppointmentRequest request = EntityFixtures.someMedicalAppointmentRequest();
        MedicalAppointmentDate someMedicalAppointmentDate = EntityFixtures.someMedicalAppointmentDate1();

        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment1();
        MedicalAppointment notExpectedMedicalAppointment = EntityFixtures.someMedicalAppointment2();

        Mockito.when(doctorService.findDoctorByEmail(someDoctor.getEmail())).thenReturn(someDoctor);
        Mockito.when(medicalAppointmentDateService.findMedicalAppointmentDateByDateAndDoctor(
                        request.getMedicalAppointmentDate(),
                        request.getDoctorEmail()))
                .thenReturn(someMedicalAppointmentDate);
        Mockito.when(medicalAppointmentDAO.makeAppointment(Mockito.any(MedicalAppointment.class)))
                .thenReturn(expectedMedicalAppointment);

        // when
        MedicalAppointment result = medicalAppointmentService.makeAppointment(request);

        // then
        Assertions.assertEquals(expectedMedicalAppointment, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointment, result);

        Mockito.verify(doctorService, Mockito.times(1))
                .findDoctorByEmail(Mockito.anyString());
        Mockito.verify(patientService, Mockito.times(1))
                .findPatientByEmail(Mockito.anyString());
        Mockito.verify(medicalAppointmentDateService, Mockito.times(1))
                .findMedicalAppointmentDateByDateAndDoctor(Mockito.any(OffsetDateTime.class), Mockito.anyString());
        Mockito.verify(medicalAppointmentDAO, Mockito.times(1))
                .makeAppointment(Mockito.any(MedicalAppointment.class));

    }

    @Test
    @DisplayName("That method should correctly delete medical appointment and return data about cancelled medical appointment")
    void shouldRemoveAndReturnCorrectMedicalAppointment() {
        // given
        MedicalAppointmentRequest request = EntityFixtures.someMedicalAppointmentRequest();
        MedicalAppointmentDate someMedicalAppointmentDate = EntityFixtures.someMedicalAppointmentDate1();

        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment1();
        MedicalAppointment notExpectedMedicalAppointment = EntityFixtures.someMedicalAppointment2();

        Mockito.when(medicalAppointmentDateService.findMedicalAppointmentDateByDateAndDoctor(
                        request.getMedicalAppointmentDate(),
                        request.getDoctorEmail()))
                .thenReturn(someMedicalAppointmentDate);
        Mockito.when(medicalAppointmentDAO.cancelMedicalAppointment(1))
                .thenReturn(expectedMedicalAppointment);

        // when
        MedicalAppointment result = medicalAppointmentService.cancelAppointment(request);

        // then
        Assertions.assertEquals(expectedMedicalAppointment, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointment, result);

        Mockito.verify(medicalAppointmentDateService, Mockito.times(1))
                .findMedicalAppointmentDateByDateAndDoctor(Mockito.any(OffsetDateTime.class), Mockito.anyString());
        Mockito.verify(medicalAppointmentDAO, Mockito.times(1))
                .cancelMedicalAppointment(1);
        Mockito.verify(medicalAppointmentDAO, Mockito.never())
                .cancelMedicalAppointment(2);

        Mockito.verifyNoInteractions(patientService);
        Mockito.verifyNoInteractions(doctorService);
    }

    @Test
    @DisplayName("That method should correctly added note to medical appointment with given request")
    void shouldCorrectlyAddedNoteToMedicalAppointment() {
        // given
        MedicalAppointmentRequest request = EntityFixtures.someMedicalAppointmentRequest();

        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment1();
        MedicalAppointment notExpectedMedicalAppointment = EntityFixtures.someMedicalAppointment2();

        Mockito.when(medicalAppointmentDAO.addNoteToMedicalAppointment(request))
                .thenReturn(Optional.of(expectedMedicalAppointment));

        // when
        MedicalAppointment result = medicalAppointmentService.addNoteToMedicalAppointment(request);

        // then
        Assertions.assertEquals(expectedMedicalAppointment, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointment, result);

        Mockito.verify(medicalAppointmentDAO, Mockito.times(1))
                .addNoteToMedicalAppointment(Mockito.any(MedicalAppointmentRequest.class));
        Mockito.verify(medicalAppointmentDAO, Mockito.never())
                .addNoteToMedicalAppointment(request.withDoctorNote("bad note"));

        Mockito.verifyNoInteractions(patientService);
        Mockito.verifyNoInteractions(doctorService);
        Mockito.verifyNoInteractions(medicalAppointmentDateService);
    }

    @Test
    @DisplayName("That method should return empty Medical Appointment and throw NotFoundException with given request")
    void shouldReturnEmptyMedicalAppointmentAndThrowNotFoundException() {
        // given
        MedicalAppointmentRequest request = EntityFixtures.someMedicalAppointmentRequest();

        Optional<MedicalAppointment> expectedMedicalAppointment = Optional.empty();

        Mockito.when(medicalAppointmentDAO.addNoteToMedicalAppointment(request))
                .thenReturn(expectedMedicalAppointment);

        // when, then
        NotFoundException exception =
                Assertions.assertThrows(NotFoundException.class,
                        () -> medicalAppointmentService.addNoteToMedicalAppointment(request));
        Assertions.assertEquals((
                        "Could not add note to medical appointment with given request: [%s]".formatted(request)),
                exception.getMessage());

        Mockito.verifyNoInteractions(patientService);
        Mockito.verifyNoInteractions(doctorService);
        Mockito.verifyNoInteractions(medicalAppointmentDateService);
    }


    @Test
    @DisplayName("That method should return all medical appointments")
    void shouldReturnAllMedicalAppointments() {
        // given
        List<MedicalAppointment> expectedMedicalAppointments = Arrays.asList(
                EntityFixtures.someMedicalAppointment1(),
                EntityFixtures.someMedicalAppointment2(),
                EntityFixtures.someMedicalAppointment3()
        );
        List<MedicalAppointment> notExpectedMedicalAppointments = Arrays.asList(
                EntityFixtures.someMedicalAppointment1(),
                EntityFixtures.someMedicalAppointment2()
        );

        Mockito.when(medicalAppointmentDAO.findAllMedicalAppointment())
                .thenReturn(expectedMedicalAppointments);

        // when
        List<MedicalAppointment> result = medicalAppointmentService.findAllMedicalAppointment();

        // then
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(expectedMedicalAppointments, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointments, result);

        Mockito.verify(medicalAppointmentDAO, Mockito.times(1))
                .findAllMedicalAppointment();

        Mockito.verifyNoInteractions(patientService);
        Mockito.verifyNoInteractions(doctorService);
        Mockito.verifyNoInteractions(medicalAppointmentDateService);
    }
}