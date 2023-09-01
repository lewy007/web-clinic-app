package pl.szczecin.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.szczecin.api.dto.DoctorDTO;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.api.dto.mapper.DoctorMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentDateMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MedicalAppointmentController {

    private static final String APPOINTMENT = "/appointment";

//    private final MedicalAppointmentService medicalAppointmentService;
//    private final MedicalAppointmentMapper medicalAppointmentMapper;
    private final MedicalAppointmentDateService medicalAppointmentDateService;
    private final MedicalAppointmentDateMapper medicalAppointmentDateMapper;
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @GetMapping(value = APPOINTMENT)
    public ModelAndView medicalAppointmentPage() {

        Map<String, ?> model = prepareMedicalAppointmentData();
        return new ModelAndView("medical_appointment", model);

    }

    private Map<String, ?> prepareMedicalAppointmentData() {

        var availableDoctors = doctorService.findAvailableDoctors().stream()
                .map(doctorMapper::map)
                .toList();
        var availableDoctorPesels = availableDoctors.stream()
                .map(DoctorDTO::getPesel)
                .toList();


        var availableMedicalAppointmentDates =
                medicalAppointmentDateService.findAvailableMedicalAppointmentDates().stream()
                        .map(medicalAppointmentDateMapper::map)
                        .toList();
        var availableDates = availableMedicalAppointmentDates.stream()
                .map(MedicalAppointmentDateDTO::getDateTime)
                .toList();


        return Map.of(
                "availableDoctorDTOs", availableDoctors,
                "availableDoctorPesels", availableDoctorPesels,
                "availableDates", availableDates,
                "medicalAppointmentDTO", MedicalAppointmentRequestDTO.buildDefaultData()
        );
    }

}
