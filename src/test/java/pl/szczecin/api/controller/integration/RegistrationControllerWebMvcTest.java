package pl.szczecin.api.controller.integration;

import lombok.AllArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import pl.szczecin.api.controller.RegistrationController;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.business.PatientService;
import pl.szczecin.business.UserService;
import pl.szczecin.domain.Patient;
import pl.szczecin.infrastructure.security.UserEntity;

import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false) //wylaczenie konfiguracji security na potrzeby testow
@AllArgsConstructor(onConstructor = @__(@Autowired))
class RegistrationControllerWebMvcTest {

    //klasa symuluje wywolania przegladarki
    private MockMvc mockMvc;

    @MockBean
    private MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    @MockBean
    private PatientService patientService;
    @MockBean
    private UserService userService;


    @Test
    @DisplayName("GET method should return the correct view")
    void registrationControllerMethodGetWorksCorrectly() throws Exception {

        //given, when, then
        mockMvc.perform(get(RegistrationController.REGISTRATION))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }


    @Test
    @DisplayName("POST method should return correct patient registration")
    void registrationControllerMethodPostWorksCorrectly() throws Exception {

        // given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        MedicalAppointmentRequestDTO.buildDefaultData().asMap().forEach(parameters::add);

        // Mocking the behavior of the patientService to save the patient
        Patient expectedPatient = Patient.builder()
                .email("jan_testowy@example.com")
                .name("Test")
                .userEntity(
                        UserEntity.builder()
                                .userId(1)
                                .build())
                .build();

        Mockito.when(patientService.savePatient(Mockito.any())).thenReturn(expectedPatient);

        // when, then
        mockMvc.perform(post(RegistrationController.REGISTRATION).params(parameters))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("patientNameDTO"))
                .andExpect(model().attributeExists("patientSurnameDTO"))
                .andExpect(model().attributeExists("patientEmailDTO"))
                .andExpect(view().name("registration_done"));

    }


    @ParameterizedTest
    @MethodSource("thatEmailValidationWorksCorrectly")
    @DisplayName("That method should check the correct validation of emails")
    void thatEmailValidationWorksCorrectly(Boolean correctEmail, String email) throws Exception {
        // given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = MedicalAppointmentRequestDTO.buildDefaultData().asMap();
        parametersMap.put("patientEmail", email);
        parametersMap.forEach(parameters::add);

        // when, then
        if (correctEmail) {
            // Mocking the behavior of the patientService to save the patient
            Patient expectedPatient = Patient.builder()
                    .email("jan_testowy@example.com")
                    .name("Test")
                    .userEntity(
                            UserEntity.builder()
                                    .userId(1)
                                    .build())
                    .build();

            Mockito.when(patientService.savePatient(Mockito.any())).thenReturn(expectedPatient);

            // metoda symuluje zachownie klienta, mowi serwerowi, ze wysylam POST na taki endpoint,
            // a serwer ma go obsluzyc
            // Jezeli poprawnie ten request typu POST z takimi parametrami na taki endpoint zostamie obsluzony
            // to status ma byc ok - czyli zwrocony 200, atrybut ma sie nie znajdowac a widok ma byc "registration_done"
            mockMvc.perform(post(RegistrationController.REGISTRATION).params(parameters))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("patientNameDTO"))
                    .andExpect(model().attributeExists("patientSurnameDTO"))
                    .andExpect(model().attributeExists("patientEmailDTO"))
                    .andExpect(view().name("registration_done"));
        } else {
            mockMvc.perform(post(RegistrationController.REGISTRATION).params(parameters))
                    .andExpect(status().isBadRequest())
                    .andExpect(model().attributeExists("errorMessage"))
                    // w wiadomosci blednej, ktora bedzie zwracana jako obsluga bledu powinien znalezc sie bledny email
                    .andExpect(model().attribute("errorMessage", Matchers.containsString(email)))
                    .andExpect(view().name("error"));
        }

    }


    @ParameterizedTest
    @MethodSource("thatPhoneValidationWorksCorrectly")
    @DisplayName("That method should check the correct validation of phones")
    void thatPhoneValidationWorksCorrectly(Boolean correctPhone, String phone) throws Exception {
        // given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = MedicalAppointmentRequestDTO.buildDefaultData().asMap();
        parametersMap.put("patientPhone", phone);
        parametersMap.forEach(parameters::add);

        // when, then
        if (correctPhone) {
            // Mocking the behavior of the patientService to save the patient
            Patient expectedPatient = Patient.builder()
                    .email("jan_testowy@example.com")
                    .name("Test")
                    .userEntity(
                            UserEntity.builder()
                                    .userId(1)
                                    .build())
                    .build();

            Mockito.when(patientService.savePatient(Mockito.any())).thenReturn(expectedPatient);

            mockMvc.perform(post(RegistrationController.REGISTRATION).params(parameters))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("patientNameDTO"))
                    .andExpect(model().attributeExists("patientSurnameDTO"))
                    .andExpect(model().attributeExists("patientEmailDTO"))
                    .andExpect(view().name("registration_done"));
        } else {
            mockMvc.perform(post(RegistrationController.REGISTRATION).params(parameters))
                    .andExpect(status().isBadRequest())
                    .andExpect(model().attributeExists("errorMessage"))
                    // w wiadomosci blednej, ktora bedzie zwracana jako obsluga bledu powinien znalezc sie bledny email
                    .andExpect(model().attribute("errorMessage", Matchers.containsString(phone)))
                    .andExpect(view().name("error"));
        }

    }


    public static Stream<Arguments> thatEmailValidationWorksCorrectly() {
        return Stream.of(
                // Poprawne adresy e-mail
                Arguments.of(true, "test@example.com"),
                Arguments.of(true, "user@gmail.com"),
                Arguments.of(true, "jan.kowalski@domain.com"),
                Arguments.of(true, "john.doe@sub.domain.com"),
                Arguments.of(true, "user+tag@example.com"),
                Arguments.of(true, "user.tag@example.com"),
                Arguments.of(true, "user_tag@example.com"),
                Arguments.of(true, "user123+tag456@example.com"),
                Arguments.of(true, "jan_kowalski@example.com"),
                Arguments.of(true, "jan.kowalski123@example.com"),
                Arguments.of(true, "jan-kowalski@example.com"),
                Arguments.of(true, "jan.kowalski@my-domain.com"),
                Arguments.of(true, "jan.kowalski@sub.my-domain.com"),
                Arguments.of(true, "test@domain"), // Brak domeny
                // Niepoprawne adresy e-mail
                Arguments.of(false, "invalid-email"), // Brak domeny
                Arguments.of(false, "invalid.com"), // Brak znaku '@'
                Arguments.of(false, "") // Pusty adres e-mail
        );
    }

    public static Stream<Arguments> thatPhoneValidationWorksCorrectly() throws NullPointerException {
        return Stream.of(
                Arguments.of(false, ""),
                Arguments.of(false, "+48 504 203 260@@"),
                Arguments.of(false, "+48.504.203.260"),
                Arguments.of(false, "+55(123) 456-78-90-"),
                Arguments.of(false, "+55(123) - 456-78-90"),
                Arguments.of(false, "504.203.260"),
                Arguments.of(false, " "),
                Arguments.of(false, "-"),
                Arguments.of(false, "()"),
                Arguments.of(false, "() + ()"),
                Arguments.of(false, "(21 7777"),
                Arguments.of(false, "+48 (21)"),
                Arguments.of(false, "+"),
                Arguments.of(false, " 1"),
                Arguments.of(false, "1"),
                Arguments.of(false, "555-5555-555"),
                Arguments.of(false, "+48 (12) 504 203 260"),
                Arguments.of(false, "+48 (12) 504-203-260"),
                Arguments.of(false, "+48(12)504203260"),
                Arguments.of(false, "+4812504203260"),
                Arguments.of(false, "4812504203260"),
                Arguments.of(true, "+48 504 203 260")
        );
    }

}