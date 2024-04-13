package pl.szczecin.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szczecin.api.dto.PatientDTO;
import pl.szczecin.api.dto.PatientsDTO;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.PatientService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(PatientRestController.PATIENT_API)
public class PatientRestController {

    public static final String PATIENT_API = "/api/patients";

    private final PatientService patientService;
    private final PatientMapper patientMapper;


    @GetMapping()
    @Operation(
            summary = "Get Available Patients",
            description = "This endpoint returns a list of available patients.",
            tags = {"Patients"} // TAG do grupowania endpointów
    )
    public ResponseEntity<PatientsDTO> availablePatients(
            @Parameter(
                    description = "Please use a correct number of page",
                    example = "0")
            @RequestParam(value = "pageNumber")
            String pageNumber,
            @Parameter(
                    description = "Please use a correct number of size",
                    example = "0")
            @RequestParam(value = "pageSize")
            String pageSize
    ) {
        return getAvailablePatientsDTO(Integer.parseInt(pageNumber), Integer.parseInt(pageSize));
    }


    private ResponseEntity<PatientsDTO> getAvailablePatientsDTO(int pageNumber, int pageSize) {
        return ResponseEntity.ok(PatientsDTO.builder()
                .patientsDTO(getPatientDTOList(pageNumber, pageSize))
                .build());
    }

    private List<PatientDTO> getPatientDTOList(int pageNumber, int pageSize) {
        return (patientService.findAvailablePatients(pageNumber, pageSize).stream()
                .map(patientMapper::map).toList());
    }
}
