package pl.szczecin.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.domain.PatientHistory;

@RestController
@AllArgsConstructor
@RequestMapping(PatientCancelRestController.API_PATIENT_APPOINTMENTS)
public class PatientCancelRestController {

    public static final String API_PATIENT_APPOINTMENTS = "/api/patient/appointments";
    public static final String CANCEL = "/cancel";

    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final MedicalAppointmentMapper medicalAppointmentMapper;

    @GetMapping()
    @Operation(
            summary = "Get Future Medical Appointment For Selected Patient",
            description = "This endpoint returns future medical appointments (minimum 24h after now)" +
                    " for selected patient.",
            tags = {"Patients"} // TAG do grupowania endpointów
    )
    public PatientHistoryDTO patientCancelAppointmentPage(
            @Parameter(
                    description = "Please use a correct patient email according to the example",
                    example = "name@example.com")
            @RequestParam(value = "patientEmail")
            String patientEmail
    ) {

        PatientHistory patientHistory = patientService.findCurrentPatientAppointmentsByEmail(patientEmail);

        return patientMapper.map(patientHistory);

    }

    @DeleteMapping(value = CANCEL)
    @Operation(
            summary = "Cancel Appointment For Selected Patient and Doctor",
            description = "Selected patient can cancel appointment to selected doctor. " +
                    "This endpoint returns data about cancelled appointment. " +
                    "NOTICE: Required fields can be checked using the GET /api/patient/appointments",
            tags = {"Patients"}
    )
    public MedicalAppointmentDTO cancelAppointment(
            @Parameter(
                    description = "Please use a correct patient email according to the example",
                    example = "name@example.com")
            @RequestParam(value = "patientEmail")
            String patientEmail,
            @Parameter(
                    description = "Please use a correct doctor surname according to the example",
                    example = "Surname")
            @RequestParam(value = "doctorSurname")
            String doctorSurname,

            @Parameter(
                    description = "Please use a correct patient email according to the example",
                    example = "2024-10-23 09:30:00")
            @RequestParam(value = "appointmentDate")
            String appointmentDate
    ) {

        // tworzymy request z parametrow
        MedicalAppointmentRequest request = getMedicalAppointmentRequest(patientEmail, doctorSurname, appointmentDate);

        // wartosc zwrocona do wykorzystania w testach
        MedicalAppointment medicalAppointment = medicalAppointmentService.cancelAppointment(request);

        return medicalAppointmentMapper.map(medicalAppointment);

    }

    private MedicalAppointmentRequest getMedicalAppointmentRequest(
            String patientEmail,
            String doctorSurname,
            String appointmentDate) {
        return medicalAppointmentRequestMapper.map(
                MedicalAppointmentRequestDTO.builder()
                        .patientEmail(patientEmail)
                        .medicalAppointmentDate(appointmentDate)
                        .doctorSurname(doctorSurname)
                        .build()
        );
    }
}
