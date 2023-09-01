package pl.szczecin.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.szczecin.api.dto.PatientDTO;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.business.PatientService;

import java.util.Objects;

@Controller
@AllArgsConstructor
public class PatientHistoryController {

    private static final String PATIENT_HISTORY = "/patient/history";

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @GetMapping(value = PATIENT_HISTORY)
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
            PatientHistory patientHistory = patientService.findPatientHistoryByEmail(patientEmail);
            model.addAttribute("patientHistoryDTO", patientMapper.map(patientHistory));
        } else {
            model.addAttribute("patientHistoryDTO", PatientHistoryDTO.buildDefault());
        }

        return "patient_history";
    }
}
