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
import org.springframework.ui.Model;
import pl.szczecin.api.controller.PatientController;
import pl.szczecin.api.dto.DoctorDTO;
import pl.szczecin.api.dto.mapper.DoctorMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.Doctor;
import pl.szczecin.util.EntityFixtures;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class PatientControllerMockitoTest {

    @Mock
    private PatientService patientService;
    @Mock
    private DoctorService doctorService;
    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private PatientController patientController;


    @Test
    @DisplayName("That method should return correct view")
    public void patientPageShouldReturnCorrectViewName() {
        // given
        ExtendedModelMap model = new ExtendedModelMap();

        // when
        String resultView = patientController.patientPage(model);

        // then
        Assertions.assertThat("patient_portal").isEqualTo(resultView);

    }

    @Test
    @DisplayName("That method should return correct email of logged in patient")
    public void patientPageShouldReturnCorrectEmailPatient() {
        // given
        String patientEmail = "test@example.com";
        Mockito.when(patientService.getLoggedInPatientEmail()).thenReturn(patientEmail);

        // when
        String result = patientService.getLoggedInPatientEmail();

        // then
        Assertions.assertThat(result).isEqualTo(patientEmail);

    }

    @Test
    @DisplayName("That method should return correct list of doctors")
    public void patientPageShouldReturnCorrectDoctorsList() {
        // given
        List<Doctor> doctorsList = List.of(
                EntityFixtures.someDoctor1(),
                EntityFixtures.someDoctor2(),
                EntityFixtures.someDoctor3());
        Mockito.when(doctorService.findAvailableDoctors()).thenReturn(doctorsList);

        // when
        List<Doctor> result = doctorService.findAvailableDoctors();

        // then
        Assertions.assertThat(result).isEqualTo(doctorsList);

    }

    @Test
    @DisplayName("That method should return correct data for model attributes")
    public void patientPageShouldAddCorrectDataToModel() {
        // given
        String patientEmail = "test@example.com";
        Mockito.when(patientService.getLoggedInPatientEmail()).thenReturn(patientEmail);

        List<Doctor> doctorsList = List.of(
                EntityFixtures.someDoctor1(),
                EntityFixtures.someDoctor2(),
                EntityFixtures.someDoctor3());

        List<DoctorDTO> doctorsListDTO = List.of(
                EntityFixtures.someDoctorDTO1(),
                EntityFixtures.someDoctorDTO2(),
                EntityFixtures.someDoctorDTO3());

        Mockito.when(doctorService.findAvailableDoctors()).thenReturn(doctorsList);
        Mockito.when(doctorMapper.map(Mockito.any(Doctor.class))).thenAnswer(
                invocation -> {
                    Doctor inputDoctor = invocation.getArgument(0);
                    return DoctorDTO.builder()
                            .name(inputDoctor.getName())
                            .surname(inputDoctor.getSurname())
                            .email(inputDoctor.getEmail())
                            .build();
                });

        // when
        Model model = new ExtendedModelMap();
        patientController.patientPage(model);

        // then
        Assertions.assertThat(patientEmail).isEqualTo(model.getAttribute("loggedInPatientEmail"));
        Assertions.assertThat(doctorsListDTO).isEqualTo(model.getAttribute("availableDoctorsDTOs"));
    }

}
