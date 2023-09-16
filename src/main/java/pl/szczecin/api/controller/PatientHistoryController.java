package pl.szczecin.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.PatientHistory;

import java.util.Objects;

@Controller
@AllArgsConstructor
public class PatientHistoryController {

    private static final String PATIENT_HISTORY = "/patient/history";

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @GetMapping(value = PATIENT_HISTORY)
    public String patientHistory(
            Model model
    ) {

        // email zalogowanego pacjenta
        String loggedInPatientEmail = patientService.getLoggedInPatientEmail();

        if (Objects.nonNull(loggedInPatientEmail)) {
            PatientHistory patientHistory = patientService.findPatientHistoryByEmail(loggedInPatientEmail);
            PatientHistoryDTO patientHistoryDTO = patientMapper.map(patientHistory);

            model.addAttribute("patientHistoryDTO", patientHistoryDTO);
            model.addAttribute("loggedInPatientEmail", loggedInPatientEmail);
        } else {
            model.addAttribute("patientHistoryDTO", PatientHistoryDTO.buildDefault());
        }

        return "patient_history";

    }
}
