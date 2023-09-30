package pl.szczecin.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.szczecin.api.dto.DoctorDTO;
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
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

@Controller
@RequiredArgsConstructor
public class DoctorTorbeController {

    private static final String DOCTOR_TORBE = "/patient/doctor/1";

    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    private final MedicalAppointmentDateService medicalAppointmentDateService;
    private final MedicalAppointmentDateMapper medicalAppointmentDateMapper;
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final PatientService patientService;

    @GetMapping(value = DOCTOR_TORBE)
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
        var availableDates =
                medicalAppointmentDateService.getAvailableDatesByDoctorEmail(doctorTorbeEmail).stream()
                        .map(medicalAppointmentDateMapper::map)
                        .map(MedicalAppointmentDateDTO::getDateTime)
                        .toList();

        return Map.of(
                "loggedInPatientEmail", loggedInPatientEmail,
                "doctorTorbeEmail", doctorTorbeEmail,
                "availableDates", availableDates
        );
    }


    @PostMapping(value = DOCTOR_TORBE)
    public String makeAppointment(
            @RequestParam("medicalAppointmentDate") String medicalAppointmentDate,
            ModelMap model
    ) {

        // email zalogowanego pacjenta
        String loggedInPatientEmail = patientService.getLoggedInPatientEmail();

        // wyciagamy doktora po nazwisku i jego email z jednoelemntowej listy
        var doctorTorbeEmail = doctorService.findAvailableDoctors().stream()
                .map(doctorMapper::map)
                .filter(doctor -> doctor.getSurname().equals("Torbe"))
                .map(DoctorDTO::getEmail)
                .toList()
                .get(0);

        // tworzymy request z parametrow
        MedicalAppointmentRequest request = medicalAppointmentRequestMapper.map(
                MedicalAppointmentRequestDTO.builder()
                        .patientEmail(loggedInPatientEmail)
                        .medicalAppointmentDate(medicalAppointmentDate)
                        .doctorEmail(doctorTorbeEmail)
                        .build()
        );

        MedicalAppointment medicalAppointment =
                medicalAppointmentService.makeAppointment(request);


        model.addAttribute("patientName", request.getPatientName());
        model.addAttribute("patientSurname", request.getPatientSurname());
        model.addAttribute("patientEmail", request.getPatientEmail());
        model.addAttribute("medicalAppointmentDate", medicalAppointmentDate);
        model.addAttribute("doctorName", medicalAppointment.getMedicalAppointmentDate().getDoctor().getName());
        model.addAttribute("doctorSurname", medicalAppointment.getMedicalAppointmentDate().getDoctor().getSurname());

        return "medical_appointment_done";
    }
}
