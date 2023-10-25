package pl.szczecin.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.api.dto.mapper.MedicalAppointmentDateMapper;
import pl.szczecin.business.MedicalAppointmentDateService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(DoctorAvailableDatesRestController.API_DOCTOR_AVAILABLE_DATES)
public class DoctorAvailableDatesRestController {

    public static final String API_DOCTOR_AVAILABLE_DATES = "/api/doctor/dates";

    private final MedicalAppointmentDateService medicalAppointmentDateService;
    private final MedicalAppointmentDateMapper medicalAppointmentDateMapper;


    @GetMapping()
    @Operation(
            summary = "Get Available Medical Appointments For Doctor",
            description = "This endpoint returns available appointment dates for selected doctor."
            //  tags = { "Doctors" } // Możesz dodawać tagi, aby grupować end-pointy
    )
    public List<String> availableDatesForDoctor(
            @Parameter(
                    description = "Please use a correct doctor email according to the example",
                    example = "name_surname@clinic.pl")
            @RequestParam(value = "doctorEmail")
            String doctorEmail
    ) {

        return getAvailableDatesByDoctorEmail(doctorEmail);
    }


    // wyszukujemy wszystkie niewykorzystane (wolne) daty wizyt (medical_appointment_date)
    // w medical_appointment dla danego lekarza
    private List<String> getAvailableDatesByDoctorEmail(String doctorEmail) {
        return medicalAppointmentDateService.getAvailableDatesByDoctorEmail(doctorEmail).stream()
                .map(medicalAppointmentDateMapper::map)
                .map(MedicalAppointmentDateDTO::getDateTime)
                .toList();
    }

}
