package pl.szczecin.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<PatientsDTO> availablePatients() {
        return getAvailablePatientsDTO();
    }


    private ResponseEntity<PatientsDTO> getAvailablePatientsDTO() {
        return ResponseEntity.ok(PatientsDTO.builder()
                .patientsDTO(getPatientDTOList())
                .build());
    }

    private List<PatientDTO> getPatientDTOList() {
        return (patientService.findAvailablePatients().stream()
                .map(patientMapper::map).toList());
    }
}
