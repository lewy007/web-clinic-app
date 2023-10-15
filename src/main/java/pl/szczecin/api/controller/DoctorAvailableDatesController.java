package pl.szczecin.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.api.dto.mapper.*;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;

@Controller
@RequiredArgsConstructor
public class DoctorAvailableDatesController {

    public static final String DOCTOR_AVAILABLE_DATES = "/doctor/dates";

    private final DoctorService doctorService;
    private final MedicalAppointmentDateService medicalAppointmentDateService;
    private final MedicalAppointmentDateMapper medicalAppointmentDateMapper;


    @GetMapping(value = DOCTOR_AVAILABLE_DATES)
    public String doctorAvailableDatesPage(
            Model model
    ) {

        // email zalogowanego doctora
        String loggedInDoctorEmail = doctorService.getLoggedInDoctorEmail();

        // wyszukujemy wszystkie niewykorzystane (wolne) daty wizyt (medical_appointment_date)
        // w medical_appointment dla danego lekarza
        var availableDatesByDoctorEmail =
                medicalAppointmentDateService.getAvailableDatesByDoctorEmail(loggedInDoctorEmail).stream()
                        .map(medicalAppointmentDateMapper::map)
                        .map(MedicalAppointmentDateDTO::getDateTime)
                        .toList();


        model.addAttribute("loggedInDoctorEmail", loggedInDoctorEmail);
        model.addAttribute("availableDatesByDoctorEmail", availableDatesByDoctorEmail);

        return "doctor_available_dates";
    }

}
