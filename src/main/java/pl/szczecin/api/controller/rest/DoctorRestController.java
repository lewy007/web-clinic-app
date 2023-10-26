package pl.szczecin.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointmentDate;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(DoctorRestController.API_DOCTOR)
public class DoctorRestController {

    public static final String API_DOCTOR = "/api/doctor";

    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentMapper medicalAppointmentMapper;
    private final MedicalAppointmentDateService medicalAppointmentDateService;


    @GetMapping()
    @Operation(
            summary = "Get Future Medical Appointments For Selected Doctor",
            description = "This endpoint returns future appointment dates," +
                    " patient names and surnames for selected doctor.",
            tags = {"Doctors"} // TAG do grupowania end-pointów
    )
    public List<MedicalAppointmentDTO> futureMedicalAppointmentsForDoctor(
            @Parameter(description = "Please use a correct doctor email", example = "name_surname@clinic.pl")
            @RequestParam(value = "doctorEmail")
            String doctorEmail
    ) {

        // wyciagamy wszystkie przyszłe daty (dokladnie ich id) powiazane z lekarzem
        var allFutureMedicalAppointmentDateIdsByDoctorEmail
                = getAllFutureMedicalAppointmentDateIdsByDoctorEmail(doctorEmail);


        // wyszukujemy wszystkie wykorzystane daty wizyt (medical_appointment_date) w medical_appointment
        // dla danego lekarza
        return getMedicalAppointmentDTOS(allFutureMedicalAppointmentDateIdsByDoctorEmail);

    }

    private List<MedicalAppointmentDTO> getMedicalAppointmentDTOS(
            List<Integer> allFutureMedicalAppointmentDateIdsByDoctorEmail
    ) {
        return medicalAppointmentService
                .findAllMedicalAppointmentByMADateID(allFutureMedicalAppointmentDateIdsByDoctorEmail).stream()
                .map(medicalAppointmentMapper::map)
                .toList();
    }

    private List<Integer> getAllFutureMedicalAppointmentDateIdsByDoctorEmail(
            String doctorEmail
    ) {
        return medicalAppointmentDateService.getAllFutureDatesByDoctorEmail(doctorEmail).stream()
                .map(MedicalAppointmentDate::getMedicalAppointmentDateId)
                .toList();
    }

}
