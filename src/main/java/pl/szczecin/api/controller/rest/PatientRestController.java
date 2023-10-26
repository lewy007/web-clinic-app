package pl.szczecin.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szczecin.api.dto.DoctorDTO;
import pl.szczecin.api.dto.DoctorsDTO;
import pl.szczecin.api.dto.mapper.DoctorMapper;
import pl.szczecin.business.DoctorService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(PatientRestController.PATIENT_API)
public class PatientRestController {

    public static final String PATIENT_API = "/api/patient";

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;


    @GetMapping()
    @Operation(
            summary = "Get Available Doctors",
            description = "This endpoint returns a list of available doctors.",
            tags = {"Patients"} // TAG do grupowania endpointów
    )
    public DoctorsDTO availableDoctors() {
        return getAvailableDoctorsDTO();
    }


    private DoctorsDTO getAvailableDoctorsDTO() {
        return DoctorsDTO.builder()
                .doctorsDTO(getDoctorDTOList())
                .build();
    }

    private List<DoctorDTO> getDoctorDTOList() {
        return doctorService.findAvailableDoctors().stream()
                .map(doctorMapper::map).toList();
    }
}
