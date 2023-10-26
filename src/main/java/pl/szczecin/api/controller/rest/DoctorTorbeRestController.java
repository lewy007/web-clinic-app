package pl.szczecin.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentDateMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentRequest;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(DoctorTorbeRestController.API_DOCTOR_TORBE)
public class DoctorTorbeRestController {

    public static final String API_DOCTOR_TORBE = "/api/doctor/1";
    public static final String APPOINTMENT = "/appointment";

    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    private final MedicalAppointmentDateService medicalAppointmentDateService;
    private final MedicalAppointmentDateMapper medicalAppointmentDateMapper;
    private final DoctorService doctorService;
    private final MedicalAppointmentMapper medicalAppointmentMapper;

    @GetMapping()
    @Operation(
            summary = "Get Available Medical Appointment Dates For Doctor Torbe",
            description = "This endpoint returns a list of available dates for doctor Torbe." +
                    "The returned results can be used for the POST method: POST /api/doctor/1 ",
            tags = {"Patients"} // TAG do grupowania endpointów
    )
    public List<String> availableMedicalAppointmentDatesForDoctorTorbe() {
        return getAvailableDates();
    }

    private List<String> getAvailableDates() {
        String doctorTorbeEmail = getDoctorTorbeEmail();

        return medicalAppointmentDateService.getAvailableDatesByDoctorEmail(doctorTorbeEmail).stream()
                .map(medicalAppointmentDateMapper::map)
                .map(MedicalAppointmentDateDTO::getDateTime)
                .toList();
    }


    // TODO mozna zmienic metode i znalezc maila na podstawie peselu badz na chwile nazwiska
    private String getDoctorTorbeEmail() {
        String doctorSurname = "Torbe";
        return doctorService.findDoctorBySurname(doctorSurname).getEmail();
    }


    @PostMapping(value = APPOINTMENT)
    @Operation(
            summary = "Make Appointment For Doctor Torbe",
            description = "Patient can make appointment to doctor Torbe. " +
                    "This endpoint returns appointment date, patient name and surname.",
            tags = {"Patients"}
    )
    public MedicalAppointmentDTO makeAppointment(
            @Parameter(
                    description = "Please use a correct patient email according to the example",
                    example = "name@example.com")
            @RequestParam(value = "patientEmail")
            String patientEmail,
            @Parameter(
                    description = "Please use a correct format date according to the example." +
                            " Available dates can be checked using the GET /api/doctor/1",
                    example = "2024-10-23 09:30:00")
            @RequestParam(value = "date")
            String medicalAppointmentDate
    ) {

        // wyciagamy email doctora danej strony
        String doctorTorbeEmail = getDoctorTorbeEmail();


        // tworzymy request z parametrow
        MedicalAppointmentRequest request = medicalAppointmentRequestMapper.map(
                MedicalAppointmentRequestDTO.builder()
                        .patientEmail(patientEmail)
                        .medicalAppointmentDate(medicalAppointmentDate)
                        .doctorEmail(doctorTorbeEmail)
                        .build()
        );

        MedicalAppointment medicalAppointment = medicalAppointmentService.makeAppointment(request);
        return medicalAppointmentMapper.map(medicalAppointment);

    }

}
