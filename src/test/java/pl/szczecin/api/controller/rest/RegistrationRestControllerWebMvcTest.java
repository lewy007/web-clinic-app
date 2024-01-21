package pl.szczecin.api.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.PatientDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.PatientService;
import pl.szczecin.business.UserService;
import pl.szczecin.domain.Patient;
import pl.szczecin.infrastructure.security.UserEntity;
import pl.szczecin.util.EntityFixtures;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RegistrationRestController.class)
@AutoConfigureMockMvc(addFilters = false) //wylaczenie konfiguracji security na potrzeby testow
@AllArgsConstructor(onConstructor = @__(@Autowired))
class RegistrationRestControllerWebMvcTest {

    //klasa symuluje wywolania przegladarki
    private MockMvc mockMvc;
    // do zamiany jsonow na obiekty i odwrotnie
    private ObjectMapper objectMapper;

    @MockBean
    private MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    @MockBean
    private PatientService patientService;
    @MockBean
    private PatientMapper patientMapper;
    @MockBean
    private UserService userService;


    // TODO metoda GET - polskie znaki przy odpowiedzi sie krzacza i sa bledy w tescie,
    //  ale normalnie metoda na produkcji dziala z polskimi znakami, wiec do zmiany jest sposob testowania

    @Test
    @DisplayName("GET method should return the correct response body")
    void registrationRestControllerMethodGetWorksCorrectly() throws Exception {

        //given
        MedicalAppointmentRequestDTO requestDTO = MedicalAppointmentRequestDTO.buildDefaultData();
        // mapujemy na jsona
        String responseBody = objectMapper.writeValueAsString(requestDTO);

        //when, then
        //Wykorzystana w tym przypadku została biblioteka Hamcrest, to z niej pochodzi klasa Matchers.
        MvcResult result = mockMvc.perform(get(RegistrationRestController.API_REGISTRATION)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientName", Matchers.is(requestDTO.getPatientName())))
                .andExpect(jsonPath("$.patientSurname", Matchers.is(requestDTO.getPatientSurname())))
                .andExpect(jsonPath("$.patientPhone", Matchers.is(requestDTO.getPatientPhone())))
                .andExpect(jsonPath("$.patientEmail", Matchers.is(requestDTO.getPatientEmail())))
                .andExpect(jsonPath("$.patientAddressCountry", Matchers.is(requestDTO.getPatientAddressCountry())))
                .andExpect(jsonPath("$.patientAddressCity", Matchers.is(requestDTO.getPatientAddressCity())))
                .andExpect(jsonPath("$.patientAddressPostalCode", Matchers.is(requestDTO.getPatientAddressPostalCode())))
                .andExpect(jsonPath("$.patientAddressStreet", Matchers.is(requestDTO.getPatientAddressStreet())))
                .andExpect(jsonPath("$.password", Matchers.is(requestDTO.getPassword())))
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);

    }


    @Test
    @DisplayName("POST method should return correct response body - patientDTO")
    void registrationRestControllerMethodPostWorksCorrectly() throws Exception {

        //given
        MedicalAppointmentRequestDTO requestDTO = MedicalAppointmentRequestDTO.buildDefaultData();
        PatientDTO somePatientDTO = EntityFixtures.somePatientDTO1();
        // mapujemy na jsona
        String requestBody = objectMapper.writeValueAsString(requestDTO);
        String responseBody = objectMapper.writeValueAsString(somePatientDTO);

        Patient expectedPatient = EntityFixtures.somePatient1();

        // Mockowanie usługi patientService w celu zapisania pacjenta
        Mockito.when(patientService.savePatient(Mockito.any())).thenReturn(expectedPatient);
        Mockito.when(patientMapper.map(expectedPatient)).thenReturn(somePatientDTO);

        // when, then
        MvcResult result = mockMvc.perform(post(RegistrationRestController.API_REGISTRATION)
                        // wywołaniem tej metody jest json
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // za pomoca biblioteki Hamcrest sprawdzamy czy w jsonie znajduja sie nw. pola
                .andExpect(jsonPath("$.name", Matchers.is(somePatientDTO.getName())))
                .andExpect(jsonPath("$.surname", Matchers.is(somePatientDTO.getSurname())))
                .andExpect(jsonPath("$.phone", Matchers.is(somePatientDTO.getPhone())))
                .andExpect(jsonPath("$.email", Matchers.is(somePatientDTO.getEmail())))
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);

    }


    @ParameterizedTest
    @MethodSource("thatEmailValidationWorksCorrectly")
    @DisplayName("That method should check the correct validation of emails")
    void thatEmailValidationWorksCorrectly(Boolean correctEmail, String email) throws Exception {
        // given
        MedicalAppointmentRequestDTO requestDTO = MedicalAppointmentRequestDTO.buildDefaultData().withPatientEmail(email);
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        // when, then
        if (correctEmail) {
            Patient expectedPatient = EntityFixtures.somePatient1();

            PatientDTO somePatientDTO = EntityFixtures.somePatientDTO1();
            String responseBody = objectMapper.writeValueAsString(somePatientDTO);

            // Mockowanie usługi patientService w celu zapisania pacjenta
            Mockito.when(patientService.savePatient(Mockito.any())).thenReturn(expectedPatient);
            Mockito.when(patientMapper.map(expectedPatient)).thenReturn(somePatientDTO);

            MvcResult result = mockMvc.perform(post(RegistrationRestController.API_REGISTRATION)
                            // wywołaniem tej metody jest json
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    // za pomoca biblioteki Hamcrest sprawdzamy czy w jsonie znajduja sie nw. pola
                    .andExpect(jsonPath("$.name", Matchers.is(somePatientDTO.getName())))
                    .andExpect(jsonPath("$.surname", Matchers.is(somePatientDTO.getSurname())))
                    .andExpect(jsonPath("$.phone", Matchers.is(somePatientDTO.getPhone())))
                    .andExpect(jsonPath("$.email", Matchers.is(somePatientDTO.getEmail())))
                    .andReturn();

            Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);

        } else {
            mockMvc.perform(post(RegistrationRestController.API_REGISTRATION)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorId", Matchers.notNullValue()));
        }

    }


    @ParameterizedTest
    @MethodSource("thatPhoneValidationWorksCorrectly")
    @DisplayName("That method should check the correct validation of phones")
    void thatPhoneValidationWorksCorrectly(Boolean correctPhone, String phone) throws Exception {
        // given
        MedicalAppointmentRequestDTO requestDTO = MedicalAppointmentRequestDTO.buildDefaultData().withPatientPhone(phone);
        String requestBody = objectMapper.writeValueAsString(requestDTO);

        // when, then
        if (correctPhone) {
            Patient expectedPatient = Patient.builder()
                    .email("jan_testowy@example.com")
                    .name("Test")
                    .userEntity(
                            UserEntity.builder()
                                    .userId(1)
                                    .build())
                    .build();

            PatientDTO somePatientDTO = EntityFixtures.somePatientDTO1();
            String responseBody = objectMapper.writeValueAsString(somePatientDTO);

            // Mockowanie usługi patientService w celu zapisania pacjenta
            Mockito.when(patientService.savePatient(Mockito.any())).thenReturn(expectedPatient);
            Mockito.when(patientMapper.map(expectedPatient)).thenReturn(somePatientDTO);

            MvcResult result = mockMvc.perform(post(RegistrationRestController.API_REGISTRATION)
                            // wywołaniem tej metody jest json
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    // za pomoca biblioteki Hamcrest sprawdzamy czy w jsonie znajduja sie nw. pola
                    .andExpect(jsonPath("$.name", Matchers.is(somePatientDTO.getName())))
                    .andExpect(jsonPath("$.surname", Matchers.is(somePatientDTO.getSurname())))
                    .andExpect(jsonPath("$.phone", Matchers.is(somePatientDTO.getPhone())))
                    .andExpect(jsonPath("$.email", Matchers.is(somePatientDTO.getEmail())))
                    .andReturn();

            Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);

        } else {
            mockMvc.perform(post(RegistrationRestController.API_REGISTRATION)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorId", Matchers.notNullValue()));
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