package pl.szczecin.business;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szczecin.business.dao.DoctorDAO;
import pl.szczecin.domain.Doctor;
import pl.szczecin.domain.exception.NotFoundException;
import pl.szczecin.util.EntityFixtures;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorDAO doctorDAO;

    @BeforeEach
    public void setUp() {
        assertNotNull(doctorDAO);
    }

    @Test
    @DisplayName("That method should return correct list of available doctors")
    void findAvailableDoctorsShouldReturnCorrectListOfDoctors() {
        //given
        List<Doctor> expectedListDoctor = List.of(
                EntityFixtures.someDoctor1(),
                EntityFixtures.someDoctor2(),
                EntityFixtures.someDoctor3()
        );
        Mockito.when(doctorDAO.findAvailableDoctors()).thenReturn(expectedListDoctor);

        //when
        List<Doctor> resultAvailableDoctors = doctorDAO.findAvailableDoctors();

        //then
        Assertions.assertThat(resultAvailableDoctors).hasSize(3);
    }

    @Test
    @DisplayName("That method should return correct doctor by email")
    void findDoctorByEmailShouldReturnCorrectDoctor() {
        //given

        String doctorEmail = "edyta.kowalska@clinic.pl";
        Doctor expectedDoctor = EntityFixtures.someDoctor1();
        Mockito.when(doctorDAO.findDoctorByEmail(doctorEmail)).thenReturn(Optional.of(expectedDoctor));

        //when
        Doctor resultDoctor = doctorDAO.findDoctorByEmail(doctorEmail).orElseThrow();

        //then
        Assertions.assertThat(resultDoctor).isEqualTo(expectedDoctor);
    }

    @ParameterizedTest
    @MethodSource("findDoctorBySurnameShouldReturnCorrectDoctor")
    @DisplayName("That method should return correct doctor by surname")
    void findDoctorBySurnameShouldReturnCorrectDoctor(Boolean correctSurname, String surname) {
        //given

        Doctor expectedDoctor = EntityFixtures.someDoctor1();


        //when, then
        if (correctSurname) {
            Mockito.when(doctorDAO.findDoctorBySurname(surname)).thenReturn(Optional.of(expectedDoctor));
            Doctor resultDoctor = doctorDAO.findDoctorBySurname(surname).orElseThrow();

            Assertions.assertThat(resultDoctor).isEqualTo(expectedDoctor);
        } else {
            // Wywołanie testowanej metody i sprawdzenie wyjątku NotFoundException
            Mockito.when(doctorDAO.findDoctorBySurname(surname))
                    .thenThrow(new NotFoundException("Could not find doctor by surname: [%s]".formatted(surname)));
            NotFoundException expectedException = assertThrows(NotFoundException.class,
                    () -> doctorDAO.findDoctorBySurname(surname));

            // Sprawdzenie treści wiadomości wyjątku
            Assertions.assertThat(expectedException.getMessage())
                    .isEqualTo("Could not find doctor by surname: [%s]".formatted(surname));

        }

    }

    public static Stream<Arguments> findDoctorBySurnameShouldReturnCorrectDoctor() {
        return Stream.of(
                Arguments.of(true, "Kowalski"),
                Arguments.of(true, "Nowak"),
                Arguments.of(true, "Jankowski"),
                Arguments.of(true, "Janiak"),
                Arguments.of(true, "Wasilewski"),
                Arguments.of(false, "Unknown"),
                Arguments.of(false, "Nieznany")
        );
    }

}