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
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.mapper.DoctorMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentDateMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentRequestMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentRequest;

import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class DoctorKrukController {

    private static final String DOCTOR = "/patient/doctor/2";

    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentRequestMapper medicalAppointmentRequestMapper;
    private final MedicalAppointmentDateService medicalAppointmentDateService;
    private final MedicalAppointmentDateMapper medicalAppointmentDateMapper;
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @GetMapping(value = DOCTOR)
    public ModelAndView medicalAppointmentPage() {

        Map<String, ?> model = prepareMedicalAppointmentData();

        return new ModelAndView("doctor_kruk_portal", model);
    }

    private Map<String, ?> prepareMedicalAppointmentData() {

        var availableDoctors = doctorService.findAvailableDoctors().stream()
                .map(doctorMapper::map)
                .filter(doctor -> doctor.getSurname().equals("Kruk"))
                .toList();

        // wyciagamy pesel z jednoelemntowej listy
        var doctorKrukPesel = availableDoctors.stream()
                .map(DoctorDTO::getPesel)
                .toList()
                .get(0);

        // wyciagamy wolne terminy dla danego lekarza
        var availableMedicalAppointmentDatesForDoctor =
                medicalAppointmentDateService.getAvailableDatesForDoctor(doctorKrukPesel).stream()
                        .map(medicalAppointmentDateMapper::map)
                        .toList();

        var availableDates = availableMedicalAppointmentDatesForDoctor.stream()
                .map(MedicalAppointmentDateDTO::getDateTime)
                .toList();


        return Map.of(
                "availableDoctorDTOs", availableDoctors,
                "doctorKrukPesel",doctorKrukPesel,
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

        var doctorKrukPesel = doctorService.findAvailableDoctors().stream()
                .map(doctorMapper::map)
                .filter(doctor -> doctor.getSurname().equals("Kruk"))
                .map(DoctorDTO::getPesel)
                .toList()
                .get(0);

        MedicalAppointmentRequest requestWithDoctorPesel = request.withDoctorPesel(doctorKrukPesel);

        MedicalAppointment medicalAppointment = medicalAppointmentService.makeAppointment(requestWithDoctorPesel);

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
