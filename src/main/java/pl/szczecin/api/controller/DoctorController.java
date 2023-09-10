package pl.szczecin.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.szczecin.api.dto.DoctorDTO;
import pl.szczecin.api.dto.mapper.*;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.business.PatientService;
import pl.szczecin.domain.MedicalAppointmentDate;

@Controller
@RequiredArgsConstructor
public class DoctorController {

    private static final String DOCTOR = "/doctor";

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final PatientMapper patientMapper;
    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentMapper medicalAppointmentMapper;
    private final MedicalAppointmentDateService medicalAppointmentDateService;
    private final MedicalAppointmentDateMapper medicalAppointmentDateMapper;


    @GetMapping(value = DOCTOR)
    public String doctorPage(
            @RequestParam(value = "doctorPesel", required = false) String doctorPesel,
            Model model
    ) {

        // pesele lakarzy do rozwijanej listy na stronie
        var availableDoctors = doctorService.findAvailableDoctors().stream()
                .map(doctorMapper::map)
                .toList();
        var allDoctorPesels = availableDoctors.stream().map(DoctorDTO::getPesel).toList();


        var allMedicalAppointmentDateForDoctorDTO =
                medicalAppointmentDateService.getAllDatesForDoctor(doctorPesel).stream()
//                        .map(medicalAppointmentDateMapper::map)
                        .toList();
        var allMedicalAppointmentDateIdForDoctor =
                allMedicalAppointmentDateForDoctorDTO.stream()
                        .map(MedicalAppointmentDate::getMedicalAppointmentDateId)
                        .toList();

        var allMedicalAppointment = medicalAppointmentService
                .findAllMedicalAppointmentByMADateID(allMedicalAppointmentDateIdForDoctor).stream()
                .map(medicalAppointmentMapper::map)
                .toList();


//        model.addAttribute("availableDoctorsDTOs", availableDoctors);
        model.addAttribute("allDoctorPesels", allDoctorPesels);
        model.addAttribute("allMedicalAppointment", allMedicalAppointment);

        return "doctor_portal";
    }

}
