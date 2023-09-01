package pl.szczecin.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.PatientDTO;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.domain.PatientHistory;

import java.util.Objects;

@Controller
@AllArgsConstructor
public class PatientCancelledController {

    private static final String PATIENT_CANCELLED = "/patient/cancel";

    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentMapper medicalAppointmentMapper;
    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @GetMapping(value = PATIENT_CANCELLED)
    public String patientHistory(
            @RequestParam(value = "patientEmail", required = false) String patientEmail,
            Model model
    ) {
        var allPatients = patientService.findAvailablePatients().stream()
                .map(patientMapper::map).toList();
        var allPatientEmails = allPatients.stream().map(PatientDTO::getEmail).toList();

        model.addAttribute("allPatientDTOs", allPatients);
        model.addAttribute("allPatientEmails", allPatientEmails);

        if (Objects.nonNull(patientEmail)) {
            PatientHistory patientHistory = patientService.findCurrentPatientAppointmentsByEmail(patientEmail);
            model.addAttribute("patientHistoryDTO", patientMapper.map(patientHistory));
        } else {
            model.addAttribute("patientHistoryDTO", PatientHistoryDTO.buildDefault());
        }

        return "patient_cancelled";
    }

    @PostMapping(value = PATIENT_CANCELLED)
    public String cancelAppointment(
            @RequestParam(value = "patientEmail", required = false) String patientEmail,
            @RequestParam("appointmentDate") String appointmentDate,
            Model model
    ) {

        // tworze request z parametrow
        MedicalAppointmentRequest request = medicalAppointmentMapper.map(
                MedicalAppointmentRequestDTO.builder()
                        .patientEmail(patientEmail)
                        .medicalAppointmentDate(appointmentDate)
                        .build()
        );

        // Tu dodaj logikę do odwołania wizyty na podstawie patientEmail i appointmentDate
//        var allPatients = patientService.findAvailablePatients().stream()
//                .map(patientMapper::map).toList();
//        var allPatientEmails = allPatients.stream().map(PatientDTO::getEmail).toList();
//
//        var allFuturePatientMedicalAppointments =
//                patientService.findCurrentPatientAppointmentsByEmail(patientEmail);
//        var allFuturePatientDates =
//                allFuturePatientMedicalAppointments.getMedicalAppointments().stream()
//                        .map(PatientHistory.MedicalAppointment::getDateTime).toList();


        MedicalAppointment medicalAppointment =
                medicalAppointmentService.cancelAppointment(request);

        model.addAttribute("existingPatientEmail", patientEmail);
        model.addAttribute("medicalAppointmentDate", appointmentDate);

        // Logika zostanie dopisana
        model.addAttribute("doctorName", "nameDoctor");
        model.addAttribute("doctorSurname", "surnameDoctor");
        model.addAttribute("patientName", "patientName");
        model.addAttribute("patientSurname", "patientSurname");
//
        return "patient_cancelled_done";
    }
}
