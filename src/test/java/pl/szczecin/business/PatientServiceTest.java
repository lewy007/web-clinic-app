package pl.szczecin.business;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.szczecin.business.dao.PatientDAO;
import pl.szczecin.domain.Patient;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.util.EntityFixtures;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientDAO patientDAO;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PatientService patientService;


    @Test
    @DisplayName("That method should save and return correct patient")
    void savePatientShouldReturnCorrectPatient() {
        //given
//        MedicalAppointmentRequest medicalAppointmentRequest = EntityFixtures.someMedicalAppointmentRequest();
        Patient expectedPatient = EntityFixtures.somePatient1();
        Mockito.when(patientDAO.savePatient(expectedPatient)).thenReturn(expectedPatient);

        //when
        Patient resultPatient = patientDAO.savePatient(expectedPatient);

        //then
        Assertions.assertThat(resultPatient).isEqualTo(expectedPatient);
    }

    @Test
    @DisplayName("That method should return correct patient by email")
    void findPatientByEmailShouldReturnCorrectPatient() {
        //given
        String patientEmail = "janusz.pacjent@clinic.pl";
        Patient expectedPatient = EntityFixtures.somePatient1();
        Mockito.when(patientDAO.findPatientByEmail(patientEmail)).thenReturn(Optional.of(expectedPatient));

        //when
        Patient resultPatient = patientDAO.findPatientByEmail(patientEmail).orElseThrow();

        //then
        Assertions.assertThat(resultPatient).isEqualTo(expectedPatient);
    }

    @Test
    @DisplayName("That method should return correct patient history by email")
    void findPatientHistoryByEmailShouldReturnCorrectPatientHistory() {
        //given
        String patientEmail = "janusz.pacjent@clinic.pl";
        PatientHistory expectedPatientHistory = EntityFixtures.somePatientHistory();
        Mockito.when(patientDAO.findPatientHistoryByEmail(patientEmail)).thenReturn(expectedPatientHistory);

        //when
        PatientHistory resultPatientHistory = patientDAO.findPatientHistoryByEmail(patientEmail);

        //then
        Assertions.assertThat(resultPatientHistory).isEqualTo(expectedPatientHistory);
    }

    @Test
    @DisplayName("That method should return correct patient appointments by email")
    void indCurrentPatientAppointmentsByEmailShouldReturnCorrectPatientAppointments() {
        //given
        String patientEmail = "janusz.pacjent@clinic.pl";
        PatientHistory expectedPatientHistory = EntityFixtures.somePatientHistory();
        Mockito.when(patientDAO.findPatientScheduleByEmail(patientEmail)).thenReturn(expectedPatientHistory);

        //when
        PatientHistory resultPatientHistory = patientDAO.findPatientScheduleByEmail(patientEmail);

        //then
        Assertions.assertThat(resultPatientHistory).isEqualTo(expectedPatientHistory);
    }


}