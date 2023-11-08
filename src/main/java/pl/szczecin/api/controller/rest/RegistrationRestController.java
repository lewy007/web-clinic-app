package pl.szczecin.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.PatientDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.PatientService;
import pl.szczecin.business.UserService;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.domain.Patient;

@RestController
@RequiredArgsConstructor
@RequestMapping(RegistrationRestController.API_REGISTRATION)
public class RegistrationRestController {


    public static final String API_REGISTRATION = "/api/registration";

    private final MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final UserService userService;

    @GetMapping()
    @Operation(
            summary = "Get Example Patient Registration Data",
            description = "This endpoint returns example patient registration data. " +
                    "You can use this template to enter your registration data in the system " +
                    "using method POST /api/registration.",
            tags = {"Patients"} // TAG do grupowania endpointów
    )
    public MedicalAppointmentRequestDTO getExampleRegistrationPatientData() {

        return MedicalAppointmentRequestDTO.buildDefaultData();

    }


    @PostMapping()
    @Operation(
            summary = "Make Registration Patient To System",
            description = "This endpoint returns patient added to system.",
            tags = {"Patients"} // TAG do grupowania endpointów
    )
    public PatientDTO makeRegistration(
            @Valid @RequestBody MedicalAppointmentRequestDTO medicalAppointmentRequestDTO
    ) {

        MedicalAppointmentRequest request = medicalAppointmentRequestMapper.map(medicalAppointmentRequestDTO);

        // zapisujemy dane pacjenta do bazy danych
        Patient savePatient = patientService.savePatient(request);

        // pobieramy id pacjenta z tabeli web_clinic_user (UserEntity)
        // i zapisujemy w tabeli web_clinic_eser_role w celu nadania uprawnien
        int userId = savePatient.getUserEntity().getUserId();
        userService.assignRoleToUser(userId);

        return patientMapper.map(savePatient);
    }

}
