package pl.szczecin.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentRequest;

@RestController
@AllArgsConstructor
@RequestMapping(PatientMakeAppointmentRestController.API_PATIENT_APPOINTMENT)
public class PatientMakeAppointmentRestController {

    public static final String API_PATIENT_APPOINTMENT = "/api/patients/{patientEmail}/appointment";

    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    private final MedicalAppointmentMapper medicalAppointmentMapper;


    @PostMapping()
    @Operation(
            summary = "Make Appointment For Selected Doctor",
            description = "Patient can make appointment to selected doctor. " +
                    "This endpoint returns appointment date, patient name and surname.",
            tags = {"Patients"}
    )
    public MedicalAppointmentDTO makeAppointment(
            @Parameter(
                    description = "Please use a correct patient email according to the example",
                    example = "name@example.com")
            @PathVariable String patientEmail,
            @Parameter(
                    description = "Please use a correct doctor email according to the example",
                    example = "name_surname@clinic.pl")
            @RequestParam(value = "doctorEmail")
            String doctorEmail,
            @Parameter(
                    description = "Please use a correct format date according to the example." +
                            " Available dates can be checked using the GET /api/doctor/1",
                    example = "2024-10-23 09:30:00")
            @RequestParam(value = "date")
            String medicalAppointmentDate
    ) {


        // tworzymy request z parametrow
        MedicalAppointmentRequest request = medicalAppointmentRequestMapper.map(
                MedicalAppointmentRequestDTO.builder()
                        .patientEmail(patientEmail)
                        .medicalAppointmentDate(medicalAppointmentDate)
                        .doctorEmail(doctorEmail)
                        .build()
        );

        MedicalAppointment medicalAppointment = medicalAppointmentService.makeAppointment(request);
        return medicalAppointmentMapper.map(medicalAppointment);

    }

}
