package pl.szczecin.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.api.dto.mapper.PatientMapper;
import pl.szczecin.business.PatientService;
import pl.szczecin.business.UserService;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.domain.Patient;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RegistrationController {


    private static final String REGISTRATION = "/registration";

    private final MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final UserService userService;

    @GetMapping(value = REGISTRATION)
    public ModelAndView registrationFormPage() {

        Map<String, ?> model = prepareMedicalAppointmentData();

        return new ModelAndView("registration", model);
    }

    private Map<String, ?> prepareMedicalAppointmentData() {

        return Map.of(
                "medicalAppointmentRequestDTO", MedicalAppointmentRequestDTO.buildDefaultData()
        );
    }


    @PostMapping(value = REGISTRATION)
    public String makeRegistration(
            @Valid @ModelAttribute("medicalAppointmentRequestDTO") MedicalAppointmentRequestDTO medicalAppointmentRequestDTO,
            ModelMap model
    ) {

        MedicalAppointmentRequest request = medicalAppointmentRequestMapper.map(medicalAppointmentRequestDTO);

        // zapisujemy dane pacjenta do bazy danych
        Patient savePatient = patientService.savePatient(request);

        // pobieramy id pacjenta z tabeli web_clinic_user (UserEntity)
        // i zapisujemy w tabeli web_clinic_eser_role w celu nadania uprawnien
        int userId = savePatient.getUserEntity().getUserId();
        userService.assignRoleToUser(userId);

        // dodajemy credentiale dla nowego pacjenta
//        patientService.addCredential(request.getPassword());


        model.addAttribute("patientNameDTO", medicalAppointmentRequestDTO.getPatientName());
        model.addAttribute("patientSurnameDTO", medicalAppointmentRequestDTO.getPatientSurname());
        model.addAttribute("patientEmailDTO", medicalAppointmentRequestDTO.getPatientEmail());
//
//
//        model.addAttribute("medicalAppointmentDate", medicalAppointmentRequestDTO.getMedicalAppointmentDate());
//        model.addAttribute("doctorName", medicalAppointment.getMedicalAppointmentDate().getDoctor().getName());
//        model.addAttribute("doctorSurname", medicalAppointment.getMedicalAppointmentDate().getDoctor().getSurname());

        return "registration_done";
    }

}
