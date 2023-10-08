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
public class DoctorController {

    static final String DOCTOR = "/doctor";

    private final DoctorService doctorService;
    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentMapper medicalAppointmentMapper;
    private final MedicalAppointmentDateService medicalAppointmentDateService;


    @GetMapping(value = DOCTOR)
    public String doctorPage(
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

        return "doctor_portal";
    }

}
