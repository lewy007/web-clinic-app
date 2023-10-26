package pl.szczecin.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.domain.MedicalAppointmentRequest;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(DoctorHistoryRestController.API_DOCTOR_HISTORY)
public class DoctorHistoryRestController {

    public static final String API_DOCTOR_HISTORY = "/api/doctor/history";
    public static final String NOTE = "/note";

    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentMapper medicalAppointmentMapper;
    private final MedicalAppointmentDateService medicalAppointmentDateService;
    private final MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;


    @GetMapping()
    @Operation(
            summary = "Get Medical Appointment History For Selected Doctor",
            description = "This endpoint returns medical appointment history (include future appointments)" +
                    "for selected doctor.",
            tags = {"Doctors"} // TAG do grupowania end-pointów
    )
    public List<MedicalAppointmentDTO> doctorMedicalAppointmentHistory(
            @Parameter(
                    description = "Please use a correct doctor email according to the example",
                    example = "name_surname@clinic.pl")
            @RequestParam(value = "doctorEmail")
            String doctorEmail
    ) {

        var allMedicalAppointmentDateIdsByDoctorEmail =
                getAllMedicalAppointmentDateIdsByDoctorEmail(doctorEmail);

        return getMedicalAppointmentDTOS(allMedicalAppointmentDateIdsByDoctorEmail);
    }

    // wyszukujemy wszystkie wykorzystane daty wizyt (medical_appointment_date) w medical_appointment
    // dla danego lekarza
    private List<MedicalAppointmentDTO> getMedicalAppointmentDTOS(List<Integer> allMedicalAppointmentDateIdsByDoctorEmail) {
        return medicalAppointmentService
                .findAllMedicalAppointmentByMADateID(allMedicalAppointmentDateIdsByDoctorEmail).stream()
                .map(medicalAppointmentMapper::map)
                .toList();
    }

    // wyciagamy wszystkie daty (dokladnie ich id) powiazane z lekarzem
    private List<Integer> getAllMedicalAppointmentDateIdsByDoctorEmail(String doctorEmail) {
        return medicalAppointmentDateService.getAllDatesByDoctorEmail(doctorEmail).stream()
                .map(MedicalAppointmentDate::getMedicalAppointmentDateId)
                .toList();
    }


    // Dodawanie danych pacjenta jest dodatkowe, ponieważ na daną godzinę jeden lekarz może przyjąć
    // tylko jednego pacjenta, ale dla pewności dodałem parametry pacjenta

    @PostMapping(value = NOTE)
    @Operation(
            summary = "Add Note to Appointment",
            description = "Doctor can add note to appointment after patient visit." +
                    "This endpoint returns medical appointment with added note.",
            tags = {"Doctors"} // TAG do grupowania end-pointów
    )
    public MedicalAppointmentDTO addNoteToAppointment(
            @Parameter(
                    description = "Please use a correct format date according to the example." +
                            " Available dates can be checked using the GET /api/doctor/history",
                    example = "2024-10-23 09:30:00")
            @RequestParam(value = "appointmentDate")
            String appointmentDate,
            @Parameter(
                    description = "Please use a correct patient name according to the example. " +
                            "Available names can be checked using the GET /api/doctor/history",
                    example = "Agnieszka")
            @RequestParam(value = "patientName")
            String patientName,
            @Parameter(
                    description = "Please use a correct patient surname according to the example. " +
                            "Available surnames can be checked using the GET /api/doctor/history",
                    example = "Nowak")
            @RequestParam(value = "patientSurname")
            String patientSurname,
            @Parameter(
                    description = "Please use a correct doctor email according to the example. " +
                            "Available doctors can be checked using the GET /api/patient",
                    example = "name_surname@clinic.pl")
            @RequestParam(value = "doctorEmail")
            String doctorEmail,
            @Parameter(
                    description = "Please add a note for patient after visit.")
            @RequestParam(value = "doctor note")
            String doctorNote
    ) {

        MedicalAppointmentRequest request
                = getRequest(appointmentDate, patientName, patientSurname, doctorEmail, doctorNote);

        // zwrócony medicalAppoinmnet wykorzystywany w testach do porównania
        MedicalAppointment medicalAppointment = medicalAppointmentService.addNoteToMedicalAppointment(request);
        return medicalAppointmentMapper.map(medicalAppointment);

    }


    // tworzymy request z parametrow
    private MedicalAppointmentRequest getRequest(
            String appointmentDate,
            String patientName,
            String patientSurname,
            String doctorEmail,
            String doctorNote) {
        return medicalAppointmentRequestMapper.map(
                MedicalAppointmentRequestDTO.builder()
                        .medicalAppointmentDate(appointmentDate)
                        .patientName(patientName)
                        .patientSurname(patientSurname)
                        .doctorEmail(doctorEmail)
                        .doctorNote(doctorNote)
                        .build()
        );
    }

}
