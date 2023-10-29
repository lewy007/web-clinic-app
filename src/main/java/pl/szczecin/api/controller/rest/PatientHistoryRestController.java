package pl.szczecin.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.PatientHistory;

@RestController
@AllArgsConstructor
@RequestMapping(PatientHistoryRestController.API_PATIENT_HISTORY)
public class PatientHistoryRestController {

    public static final String API_PATIENT_HISTORY = "/api/patients/{patientEmail}/history";


    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @GetMapping()
    @Operation(
            summary = "Get Medical Appointment History For Selected Patient",
            description = "This endpoint returns medical appointment history (include future appointments)" +
                    " for selected patient.",
            tags = {"Patients"} // TAG do grupowania endpointów
    )
    public PatientHistoryDTO patientMedicalAppointmentHistory(
            @Parameter(
                    description = "Please use a correct patient email according to the example." +
                            " Available patient emails can be checked using method GET /api/patients",
                    example = "name@example.com")
            @PathVariable String patientEmail
    ) {

        PatientHistory patientHistory = patientService.findPatientHistoryByEmail(patientEmail);

        return patientMapper.map(patientHistory);

    }
}
