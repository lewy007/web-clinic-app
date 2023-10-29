package pl.szczecin.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.PatientHistory;

@Controller
@AllArgsConstructor
public class PatientScheduleController {

    public static final String PATIENT_SCHEDULE = "/patient/schedule";

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @GetMapping(value = PATIENT_SCHEDULE)
    public String patientSchedulePage(
            Model model
    ) {

        // email zalogowanego pacjenta
        String loggedInPatientEmail = patientService.getLoggedInPatientEmail();

        PatientHistory patientHistory = patientService.findPatientScheduleByEmail(loggedInPatientEmail);
        PatientHistoryDTO patientHistoryDTO = patientMapper.map(patientHistory);

        model.addAttribute("patientHistoryDTO", patientHistoryDTO);
        model.addAttribute("loggedInPatientEmail", loggedInPatientEmail);


        return "patient_schedule";

    }
}
