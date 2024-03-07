package pl.szczecin.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentDateMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.Doctor;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentRequest;

@Controller
@RequiredArgsConstructor
public class DoctorKrukController {

    public static final String DOCTOR_KRUK = "/patient/doctor/2";

    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    private final MedicalAppointmentDateService medicalAppointmentDateService;
    private final MedicalAppointmentDateMapper medicalAppointmentDateMapper;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @GetMapping(value = DOCTOR_KRUK)
    public String medicalAppointmentPageToDoctorKruk
            (Model model
            ) {

        // email zalogowanego pacjenta
        String loggedInPatientEmail = patientService.getLoggedInPatientEmail();

        // wyciagamy email doctora danej strony
        var doctorKrukEmail = getDoctorKruk().getEmail();

        // wyciagamy wolne terminy dla danego lekarza
        var availableDates =
                medicalAppointmentDateService.getAvailableDatesByDoctorEmail(doctorKrukEmail).stream()
                        .map(medicalAppointmentDateMapper::map)
                        .map(MedicalAppointmentDateDTO::getDateTime)
                        .toList();

        model.addAttribute("loggedInPatientEmail", loggedInPatientEmail);
        model.addAttribute("doctorKrukEmail", doctorKrukEmail);
        model.addAttribute("availableDates", availableDates);

        return "doctor_kruk_portal";
    }


    @PostMapping(value = DOCTOR_KRUK)
    public String makeAppointment(
            @RequestParam("medicalAppointmentDate") String medicalAppointmentDate,
            ModelMap model
    ) {

        // email zalogowanego pacjenta
        String loggedInPatientEmail = patientService.getLoggedInPatientEmail();

        // wyciagamy email doctora danej strony
        var doctorKrukEmail = getDoctorKruk().getEmail();

        // tworzymy request z parametrow
        MedicalAppointmentRequest request = medicalAppointmentRequestMapper.map(
                MedicalAppointmentRequestDTO.builder()
                        .patientEmail(loggedInPatientEmail)
                        .medicalAppointmentDate(medicalAppointmentDate)
                        .doctorEmail(doctorKrukEmail)
                        .build()
        );

        MedicalAppointment medicalAppointment =
                medicalAppointmentService.makeAppointment(request);


        model.addAttribute("patientName", request.getPatientName());
        model.addAttribute("patientSurname", request.getPatientSurname());
        model.addAttribute("patientEmail", request.getPatientEmail());
        model.addAttribute("medicalAppointmentDate", medicalAppointmentDate);
        model.addAttribute("doctorName", getDoctorKruk().getName());
        model.addAttribute("doctorSurname", getDoctorKruk().getSurname());

        return "medical_appointment_done";
    }

    // TODO mozna zmienic metode i znalezc maila na podstawie peselu badz na chwile nazwiska
    private Doctor getDoctorKruk() {
        String doctorSurname = "Kruk";
        return doctorService.findDoctorBySurname(doctorSurname);
    }

}
