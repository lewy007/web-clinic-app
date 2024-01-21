package pl.szczecin.api.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.szczecin.api.dto.DoctorDTO;
import pl.szczecin.api.dto.DoctorsDTO;
import pl.szczecin.api.dto.mapper.DoctorMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.domain.Doctor;
import pl.szczecin.util.EntityFixtures;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.szczecin.util.EntityFixtures.*;

@WebMvcTest(controllers = DoctorRestController.class)
@AutoConfigureMockMvc(addFilters = false) //wylaczenie konfiguracji security na potrzeby testow
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DoctorRestControllerWebMvcTest {

    //klasa symuluje wywolania przegladarki
    private MockMvc mockMvc;
    // do zamiany jsonow na obiekty i odwrotnie
    private ObjectMapper objectMapper;

    @MockBean
    DoctorService doctorService;
    @MockBean
    DoctorMapper doctorMapper;

    @Test
    @DisplayName("GET Method should return the correct DTO List Doctors")
    void doctorRestControllerMethodGetWorksCorrectly() throws Exception {

        //given
        DoctorsDTO someDoctorListDTO = someDoctorListDTO();
        String responseBody = objectMapper.writeValueAsString(someDoctorListDTO);


        // dane do mockowania
        List<DoctorDTO> doctorDTOList = List.of(
                someDoctorDTO1(),
                someDoctorDTO2(),
                someDoctorDTO3()
        );
        List<Doctor> doctorList = List.of(
                EntityFixtures.someDoctor1(),
                EntityFixtures.someDoctor2(),
                EntityFixtures.someDoctor3()
        );

        Mockito.when(doctorService.findAvailableDoctors()).thenReturn(doctorList);
        Mockito.when(doctorMapper.map(Mockito.any(Doctor.class)))
                .thenReturn(doctorDTOList.get(0))
                .thenReturn(doctorDTOList.get(1))
                .thenReturn(doctorDTOList.get(2));


        //when, then
        //Wykorzystana w tym przypadku została biblioteka Hamcrest, to z niej pochodzi klasa Matchers.
        // doctorsDTO to nazwa zmiennej z klasy DoctorsDTO
        MvcResult result = mockMvc.perform(get(DoctorRestController.DOCTOR_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorsDTO", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.doctorsDTO[0].name", Matchers.is(doctorDTOList.get(0).getName())))
                .andExpect(jsonPath("$.doctorsDTO[0].surname", Matchers.is(doctorDTOList.get(0).getSurname())))
                .andExpect(jsonPath("$.doctorsDTO[0].email", Matchers.is(doctorDTOList.get(0).getEmail())))
                .andExpect(jsonPath("$.doctorsDTO[1].name", Matchers.is(doctorDTOList.get(1).getName())))
                .andExpect(jsonPath("$.doctorsDTO[1].surname", Matchers.is(doctorDTOList.get(1).getSurname())))
                .andExpect(jsonPath("$.doctorsDTO[1].email", Matchers.is(doctorDTOList.get(1).getEmail())))
                .andExpect(jsonPath("$.doctorsDTO[2].name", Matchers.is(doctorDTOList.get(2).getName())))
                .andExpect(jsonPath("$.doctorsDTO[2].surname", Matchers.is(doctorDTOList.get(2).getSurname())))
                .andExpect(jsonPath("$.doctorsDTO[2].email", Matchers.is(doctorDTOList.get(2).getEmail())))
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo(responseBody);

    }

}