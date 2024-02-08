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
import pl.szczecin.api.controller.rest.DoctorRestController;
import pl.szczecin.api.dto.DoctorDTO;
import pl.szczecin.api.dto.DoctorsDTO;
import pl.szczecin.api.dto.mapper.DoctorMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.domain.Doctor;
import pl.szczecin.util.EntityFixtures;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class DoctorRestControllerMockitoTest {

    @Mock
    private DoctorService doctorService;
    @Mock
    private DoctorMapper doctorMapper;


    @InjectMocks
    private DoctorRestController doctorRestController;


    @BeforeEach
    public void setUp() {
        System.out.println("checking for nulls");
        Assertions.assertNotNull(doctorService);
        Assertions.assertNotNull(doctorMapper);
    }


    @Test
    @DisplayName("That method should return correct list of available doctors (DoctorsDTO)")
    void shouldReturnCorrectListOfAvailableDoctors() {
        // Given
        List<Doctor> doctorList = List.of(
                EntityFixtures.someDoctor1(),
                EntityFixtures.someDoctor2()
        );
        DoctorsDTO expectedDoctorsDTO = DoctorsDTO.builder()
                .doctorsDTO(List.of(
                        EntityFixtures.someDoctorDTO1(),
                        EntityFixtures.someDoctorDTO2()))
                .build();
        DoctorsDTO notExpectedDoctorsDTO = DoctorsDTO.builder()
                .doctorsDTO(List.of(
                        EntityFixtures.someDoctorDTO1()))
                .build();

        Mockito.when(doctorService.findAvailableDoctors()).thenReturn(doctorList);
        Mockito.when(doctorMapper.map(Mockito.any(Doctor.class)))
                .thenReturn(expectedDoctorsDTO.getDoctorsDTO().get(0))
                .thenReturn(expectedDoctorsDTO.getDoctorsDTO().get(1));

        // When
        DoctorsDTO result = doctorRestController.availableDoctors();

        // Then
        Assertions.assertEquals(result.getDoctorsDTO().size(), expectedDoctorsDTO.getDoctorsDTO().size());
        Assertions.assertEquals(result.getDoctorsDTO(), expectedDoctorsDTO.getDoctorsDTO());
        //lista z inna iloscia elementow nie jest rowna, wiec test przechodzi
        Assertions.assertNotEquals(result.getDoctorsDTO().size(), notExpectedDoctorsDTO.getDoctorsDTO().size());
    }

    @Test
    @DisplayName("That method should return correct list of available DoctorDTO")
    void shouldReturnCorrectListOfDoctorDTO() {
        // Given
        List<Doctor> doctorList = List.of(
                EntityFixtures.someDoctor1(),
                EntityFixtures.someDoctor2()
        );
        List<DoctorDTO> expectedDoctorDTOList = List.of(
                EntityFixtures.someDoctorDTO1(),
                EntityFixtures.someDoctorDTO2()
        );
        List<DoctorDTO> notExpectedDoctorDTOList = List.of(
                EntityFixtures.someDoctorDTO1()
        );

        Mockito.when(doctorService.findAvailableDoctors()).thenReturn(doctorList);
        Mockito.when(doctorMapper.map(Mockito.any(Doctor.class)))
                .thenReturn(expectedDoctorDTOList.get(0))
                .thenReturn(expectedDoctorDTOList.get(1));

        // When
        List<DoctorDTO> result = doctorService.findAvailableDoctors().stream()
                .map(doctorMapper::map).toList();

        // Then
        Assertions.assertEquals(result.size(), expectedDoctorDTOList.size());
        Assertions.assertEquals(result, expectedDoctorDTOList);
        //lista z inna iloscia elementow nie jest rowna, wiec test przechodzi
        Assertions.assertNotEquals(result, notExpectedDoctorDTOList);
    }

}