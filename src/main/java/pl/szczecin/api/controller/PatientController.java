package pl.szczecin.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.szczecin.api.dto.mapper.DoctorMapper;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.PatientService;

@Controller
@RequiredArgsConstructor
public class PatientController {

    private static final String PATIENT = "/patient";

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final PatientMapper patientMapper;


    @GetMapping(value = PATIENT)
    public String homePage(Model model) {
        var availableDoctors = doctorService.findAvailableDoctors().stream()
                .map(doctorMapper::map)
                .toList();

        // do modelu przekazujmey samochody typu CarDTO, dzieki temy ja ustalam jaki typ danych wychodzi na zewnatrz
        // i ograniczam w ten sposob mozliwosc wycieku danych, ktore nie chcialbym zeby wyszly z backendu
        model.addAttribute("availableDoctorsDTOs", availableDoctors);

        return "patient_portal";
    }

}
