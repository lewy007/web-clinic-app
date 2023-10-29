package pl.szczecin.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.domain.PatientHistory;

@Controller
@AllArgsConstructor
public class PatientCancelController {

    public static final String PATIENT_CANCEL = "/patient/cancel";

    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final DoctorService doctorService;

    @GetMapping(value = PATIENT_CANCEL)
    public String patientCancelAppointmentPage(
            Model model
    ) {

        // email zalogowanego pacjenta
        String loggedInPatientEmail = patientService.getLoggedInPatientEmail();

        PatientHistory patientHistory = patientService.findPatientAppointmentsToCancelByEmail(loggedInPatientEmail);
        PatientHistoryDTO patientHistoryDTO = patientMapper.map(patientHistory);

        model.addAttribute("patientHistoryDTO", patientHistoryDTO);
        model.addAttribute("loggedInPatientEmail", loggedInPatientEmail);

        return "patient_cancel";
    }

    @PostMapping(value = PATIENT_CANCEL)
    public String cancelAppointment(
            @RequestParam(value = "patientEmail", required = false) String patientEmail,
            @RequestParam("appointmentDate") String appointmentDate,
            @RequestParam("doctorEmail") String doctorEmail,
            Model model
    ) {

        // tworzymy request z parametrow
        MedicalAppointmentRequest request = medicalAppointmentRequestMapper.map(
                MedicalAppointmentRequestDTO.builder()
                        .patientEmail(patientEmail)
                        .medicalAppointmentDate(appointmentDate)
                        .doctorEmail(doctorEmail)
                        .build()
        );

        var doctorByEmail = doctorService.findDoctorByEmail(doctorEmail);
        var patientByEmail = patientService.findPatientByEmail(patientEmail);


        // wartosc zwrocona do wykorzystania w testach
        MedicalAppointment medicalAppointment = medicalAppointmentService.cancelAppointment(request);

        model.addAttribute("patientEmail", patientEmail);
        model.addAttribute("medicalAppointmentDate", appointmentDate);

        model.addAttribute("doctorName", doctorByEmail.getName());
        model.addAttribute("doctorSurname", doctorByEmail.getSurname());
        model.addAttribute("patientName", patientByEmail.getName());
        model.addAttribute("patientSurname", patientByEmail.getSurname());

        return "patient_cancel_done";
    }
}
