package pl.szczecin.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.szczecin.api.dto.DoctorDTO;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.api.dto.mapper.DoctorMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentDateMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentRequest;

import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class DoctorTorbeController {

    private static final String DOCTOR = "/patient/doctor/1";

    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    private final MedicalAppointmentDateService medicalAppointmentDateService;
    private final MedicalAppointmentDateMapper medicalAppointmentDateMapper;
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final PatientService patientService;

    @GetMapping(value = DOCTOR)
    public ModelAndView medicalAppointmentPage() {

        Map<String, ?> model = prepareMedicalAppointmentData();

        return new ModelAndView("doctor_torbe_portal", model);
    }

    private Map<String, ?> prepareMedicalAppointmentData() {

        // email zalogowanego pacjenta
        String loggedInPatientEmail = patientService.getLoggedInPatientEmail();

        var availableDoctors = doctorService.findAvailableDoctors().stream()
                .map(doctorMapper::map)
                .filter(doctor -> doctor.getSurname().equals("Torbe"))
                .toList();

        // wyciagamy email z jednoelemntowej listy
        var doctorTorbeEmail = availableDoctors.stream()
                .map(DoctorDTO::getEmail)
                .toList()
                .get(0);

        // wyciagamy wolne terminy dla danego lekarza
        var availableMedicalAppointmentDatesForDoctor =
                medicalAppointmentDateService.getAvailableDatesForDoctor(doctorTorbeEmail).stream()
                        .map(medicalAppointmentDateMapper::map)
                        .toList();

        var availableDates = availableMedicalAppointmentDatesForDoctor.stream()
                .map(MedicalAppointmentDateDTO::getDateTime)
                .toList();


        return Map.of(
                "loggedInPatientEmail", loggedInPatientEmail,
                "availableDoctorDTOs", availableDoctors,
                "doctorTorbeEmail",doctorTorbeEmail,
                "availableDates", availableDates,
                "medicalAppointmentRequestDTO", MedicalAppointmentRequestDTO.buildDefaultData()
        );
    }


    @PostMapping(value = DOCTOR)
    public String makeAppointment(
            @Valid @ModelAttribute("medicalAppointmentDTO") MedicalAppointmentRequestDTO medicalAppointmentRequestDTO,
            ModelMap model
    ) {
        MedicalAppointmentRequest request = medicalAppointmentRequestMapper.map(medicalAppointmentRequestDTO);


        // wyciagamy doktora po nazwisku i jego email z jednoelemntowej listy
        var doctorTorbeEmail = doctorService.findAvailableDoctors().stream()
                .map(doctorMapper::map)
                .filter(doctor -> doctor.getSurname().equals("Torbe"))
                .map(DoctorDTO::getEmail)
                .toList()
                .get(0);

        MedicalAppointmentRequest requestWithDoctorEmail = request.withDoctorEmail(doctorTorbeEmail);

        MedicalAppointment medicalAppointment = medicalAppointmentService.makeAppointment(requestWithDoctorEmail);

        if (existingCustomerEmailExists(medicalAppointmentRequestDTO.getExistingPatientEmail())) {
            model.addAttribute("existingPatientEmail", medicalAppointmentRequestDTO.getExistingPatientEmail());
        } else {
            model.addAttribute("patientName", medicalAppointmentRequestDTO.getPatientName());
            model.addAttribute("patientSurname", medicalAppointmentRequestDTO.getPatientSurname());
        }

        model.addAttribute("medicalAppointmentDate", medicalAppointmentRequestDTO.getMedicalAppointmentDate());
        model.addAttribute("doctorName", medicalAppointment.getMedicalAppointmentDate().getDoctor().getName());
        model.addAttribute("doctorSurname", medicalAppointment.getMedicalAppointmentDate().getDoctor().getSurname());

        return "medical_appointment_done";
    }

    private boolean existingCustomerEmailExists(String existingCustomerEmail) {
        return Objects.nonNull(existingCustomerEmail) && !existingCustomerEmail.isBlank();
    }

}
