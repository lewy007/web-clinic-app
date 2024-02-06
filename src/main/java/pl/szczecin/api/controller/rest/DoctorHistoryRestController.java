package pl.szczecin.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.api.dto.MedicalAppointmentsDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointmentDate;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(DoctorHistoryRestController.API_DOCTOR_HISTORY)
public class DoctorHistoryRestController {

    public static final String API_DOCTOR_HISTORY = "/api/doctors/{doctorEmail}/history";

    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentMapper medicalAppointmentMapper;
    private final MedicalAppointmentDateService medicalAppointmentDateService;


    @GetMapping()
    @Operation(
            summary = "Get Medical Appointment History For Selected Doctor",
            description = "This endpoint returns medical appointment history for selected doctor.",
            tags = {"Doctors"} // TAG do grupowania end-pointów
    )
    public MedicalAppointmentsDTO doctorMedicalAppointmentHistory(
            @Parameter(
                    description = "Please use a correct doctor email according to the example. " +
                            "Available doctor emails can be checked using method GET /api/doctors",
                    example = "name.surname@clinic.pl")
            @PathVariable String doctorEmail
    ) {

        return getMedicalAppointmentsDTO(doctorEmail);
    }


    // tworzymy MedicalAppointmentsDTO, czyli liste MedicalAppointmentDTO
    private MedicalAppointmentsDTO getMedicalAppointmentsDTO(String doctorEmail) {
        return MedicalAppointmentsDTO.builder()
                .medicalAppointmentsDTO(
                        getMedicalAppointmentListDTO(
                                getAllMedicalAppointmentDateIdsByDoctorEmail(doctorEmail)))
                .build();
    }

    // wyszukujemy wszystkie wykorzystane daty wizyt (medical_appointment_date) w medical_appointment
    // dla danego lekarza
    private List<MedicalAppointmentDTO> getMedicalAppointmentListDTO(List<Integer> allMedicalAppointmentDateIdsByDoctorEmail) {
        return medicalAppointmentService
                .findAllMedicalAppointmentByMADateID(allMedicalAppointmentDateIdsByDoctorEmail).stream()
                .map(medicalAppointmentMapper::map)
                .toList();
    }

    // wyciagamy wszystkie daty (dokladnie ich id) powiazane z lekarzem
    private List<Integer> getAllMedicalAppointmentDateIdsByDoctorEmail(String doctorEmail) {
        return medicalAppointmentDateService.getAllHistoryDatesByDoctorEmail(doctorEmail).stream()
                .map(MedicalAppointmentDate::getMedicalAppointmentDateId)
                .toList();
    }
}
