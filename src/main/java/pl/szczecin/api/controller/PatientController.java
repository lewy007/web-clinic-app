package pl.szczecin.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.szczecin.api.dto.mapper.DoctorMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.PatientService;

@Controller
@RequiredArgsConstructor
public class PatientController {

    public static final String PATIENT = "/patient";

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;


    @GetMapping(value = PATIENT)
    public String patientPage(Model model) {

        var availableDoctors = doctorService.findAvailableDoctors().stream()
                .map(doctorMapper::map)
                .toList();

        // email zalogowanego pacjenta
        String loggedInPatientEmail = patientService.getLoggedInPatientEmail();


        // do modelu przekazujmey lekarzy typu DoctorDTO, dzieki temu ja ustalam jaki typ danych wychodzi na zewnatrz
        // i ograniczam w ten sposob mozliwosc wycieku danych, ktore nie chcialbym zeby wyszly z backendu
        model.addAttribute("availableDoctorsDTOs", availableDoctors);
        model.addAttribute("loggedInPatientEmail", loggedInPatientEmail);

        return "patient_portal";
    }

}
