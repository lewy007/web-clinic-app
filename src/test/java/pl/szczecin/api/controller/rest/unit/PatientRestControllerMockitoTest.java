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
import org.springframework.http.ResponseEntity;
import pl.szczecin.api.controller.rest.PatientRestController;
import pl.szczecin.api.dto.PatientDTO;
import pl.szczecin.api.dto.PatientsDTO;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.Patient;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.util.EntityFixtures;

import java.util.List;
import java.util.Objects;


@ExtendWith(MockitoExtension.class)
class PatientRestControllerMockitoTest {

    @Mock
    private PatientService patientService;
    @Mock
    private PatientMapper patientMapper;


    @InjectMocks
    private PatientRestController patientRestController;


    @BeforeEach
    public void setUp() {
        System.out.println("checking for nulls");
        Assertions.assertNotNull(patientService);
        Assertions.assertNotNull(patientMapper);
    }


    @Test
    @DisplayName("That method should return correct list of available patients (PatientsDTO)")
    void shouldReturnCorrectListOfAvailablePatients() {
        // Given
        List<Patient> patientList = List.of(
                EntityFixtures.somePatient1(),
                EntityFixtures.somePatient2()
        );
        PatientsDTO expectedPatientsDTO = PatientsDTO.builder()
                .patientsDTO(List.of(
                        EntityFixtures.somePatientDTO1(),
                        EntityFixtures.somePatientDTO2()))
                .build();
        PatientsDTO notExpectedPatientsDTO = PatientsDTO.builder()
                .patientsDTO(List.of(
                        EntityFixtures.somePatientDTO1()))
                .build();

        Mockito.when(patientService.findAvailablePatients()).thenReturn(patientList);
        Mockito.when(patientMapper.map(Mockito.any(Patient.class)))
                .thenReturn(expectedPatientsDTO.getPatientsDTO().get(0))
                .thenReturn(expectedPatientsDTO.getPatientsDTO().get(1));

        // When
        ResponseEntity<PatientsDTO> result = patientRestController.availablePatients();

        // Then
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).getPatientsDTO().size(),
                expectedPatientsDTO.getPatientsDTO().size());
        Assertions.assertEquals(result.getBody().getPatientsDTO(), expectedPatientsDTO.getPatientsDTO());
        //lista z inna iloscia elementow nie jest rowna, wiec test przechodzi
        Assertions.assertNotEquals(result.getBody().getPatientsDTO().size(),
                notExpectedPatientsDTO.getPatientsDTO().size());

        Mockito.verify(patientService, Mockito.times(1))
                .findAvailablePatients();
        Mockito.verify(patientMapper, Mockito.times(2))
                .map(Mockito.any(Patient.class));
        Mockito.verify(patientMapper, Mockito.never())
                .map(Mockito.any(PatientHistory.class));
    }

    @Test
    @DisplayName("That method should return correct list of available PatientDTO")
    void shouldReturnCorrectListOfPatientDTO() {
        // Given
        List<Patient> patientList = List.of(
                EntityFixtures.somePatient1(),
                EntityFixtures.somePatient2()
        );
        List<PatientDTO> expectedPatientDTOList = List.of(
                EntityFixtures.somePatientDTO1(),
                EntityFixtures.somePatientDTO2()
        );
        List<PatientDTO> notExpectedPatientDTOList = List.of(
                EntityFixtures.somePatientDTO1()
        );

        Mockito.when(patientService.findAvailablePatients()).thenReturn(patientList);
        Mockito.when(patientMapper.map(Mockito.any(Patient.class)))
                .thenReturn(expectedPatientDTOList.get(0))
                .thenReturn(expectedPatientDTOList.get(1));

        // When
        List<PatientDTO> result = patientService.findAvailablePatients().stream()
                .map(patientMapper::map).toList();

        // Then
        Assertions.assertEquals(result.size(), expectedPatientDTOList.size());
        Assertions.assertEquals(result, expectedPatientDTOList);
        //lista z inna iloscia elementow nie jest rowna, wiec test przechodzi
        Assertions.assertNotEquals(result, notExpectedPatientDTOList);

        Mockito.verify(patientService, Mockito.times(1))
                .findAvailablePatients();
        Mockito.verify(patientMapper, Mockito.times(2))
                .map(Mockito.any(Patient.class));
        Mockito.verify(patientMapper, Mockito.never())
                .map(Mockito.any(PatientHistory.class));
    }

}