package pl.szczecin.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.domain.MedicalAppointmentRequest;

@Controller
@RequiredArgsConstructor
public class DoctorScheduleController {

    public static final String DOCTOR_SCHEDULE = "/doctor/schedule";

    private final DoctorService doctorService;
    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentMapper medicalAppointmentMapper;
    private final MedicalAppointmentDateService medicalAppointmentDateService;
    private final MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;


    @GetMapping(value = DOCTOR_SCHEDULE)
    public String doctorSchedulePage(
            Model model
    ) {

        // email zalogowanego doctora
        String loggedInDoctorEmail = doctorService.getLoggedInDoctorEmail();

        // wyciagamy wszystkie przyszłe daty (dokladnie ich id) powiazane z lekarzem
        var allFutureMedicalAppointmentDateIdsByDoctorEmail =
                medicalAppointmentDateService.getAllFutureDatesByDoctorEmail(loggedInDoctorEmail).stream()
                        .map(MedicalAppointmentDate::getMedicalAppointmentDateId)
                        .toList();

        // wyszukujemy wszystkie wykorzystane daty wizyt (medical_appointment_date) w medical_appointment
        // dla danego lekarza
        var medicalAppointmentDTOs = medicalAppointmentService
                .findAllMedicalAppointmentByMADateID(allFutureMedicalAppointmentDateIdsByDoctorEmail).stream()
                .map(medicalAppointmentMapper::map)
                .toList();


        model.addAttribute("loggedInDoctorEmail", loggedInDoctorEmail);
        model.addAttribute("medicalAppointmentDTOs", medicalAppointmentDTOs);

        return "doctor_schedule";
    }


    @PostMapping(value = DOCTOR_SCHEDULE)
    public String addNoteToAppointment(
            @RequestParam("appointmentDate") String appointmentDate,
            @RequestParam("patientName") String patientName,
            @RequestParam("patientSurname") String patientSurname,
            @RequestParam("doctorNote") String doctorNote,
            Model model
    ) {

        // email zalogowanego doctora
        String loggedInDoctorEmail = doctorService.getLoggedInDoctorEmail();

        // tworzymy request z parametrow
        MedicalAppointmentRequest request = medicalAppointmentRequestMapper.map(
                MedicalAppointmentRequestDTO.builder()
                        .medicalAppointmentDate(appointmentDate)
                        .patientName(patientName)
                        .patientSurname(patientSurname)
                        .doctorEmail(loggedInDoctorEmail)
                        .doctorNote(doctorNote)
                        .build()
        );

        // zwrócony medicalAppoinmnet wykorzystywany w testach do porównania
        MedicalAppointment medicalAppointment = medicalAppointmentService.addNoteToMedicalAppointment(request);

        // Dodajemy atrybut informujący o sukcesie w zwracanym widoku
        model.addAttribute("successMessage", "Notatka została dodana pomyślnie.");

        return "doctor_schedule_added_note";
    }

}
