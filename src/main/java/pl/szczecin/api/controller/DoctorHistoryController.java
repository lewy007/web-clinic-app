package pl.szczecin.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointmentDate;

@Controller
@RequiredArgsConstructor
public class DoctorHistoryController {

    public static final String DOCTOR_HISTORY = "/doctor/history";

    private final DoctorService doctorService;
    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentMapper medicalAppointmentMapper;
    private final MedicalAppointmentDateService medicalAppointmentDateService;


    @GetMapping(value = DOCTOR_HISTORY)
    public String doctorHistoryPage(
            Model model
    ) {

        // email zalogowanego doctora
        String loggedInDoctorEmail = doctorService.getLoggedInDoctorEmail();

        // wyciagamy wszystkie daty (dokladnie ich id) powiazane z lekarzem
        // Założenia biznesu-> zakres dat to daty do aktualnego dnia wyłącznie.
        // Metoda zazębia się z pobieraniem przyszłych i teraźniejszych dat
        var allMedicalAppointmentDateIdsByDoctorEmail =
                medicalAppointmentDateService.getAllHistoryDatesByDoctorEmail(loggedInDoctorEmail).stream()
                        .map(MedicalAppointmentDate::getMedicalAppointmentDateId)
                        .toList();

        // wyszukujemy wszystkie historyczne, wykorzystane daty wizyt (medical_appointment_date) w medical_appointment
        // dla danego lekarza
        var medicalAppointmentDTOs = medicalAppointmentService
                .findAllMedicalAppointmentByMADateID(allMedicalAppointmentDateIdsByDoctorEmail).stream()
                .map(medicalAppointmentMapper::map)
                .toList();


        model.addAttribute("loggedInDoctorEmail", loggedInDoctorEmail);
        model.addAttribute("medicalAppointmentDTOs", medicalAppointmentDTOs);

        return "doctor_history";
    }

}
