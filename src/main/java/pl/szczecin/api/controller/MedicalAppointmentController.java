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
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.api.dto.mapper.DoctorMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentDateMapper;
import pl.szczecin.api.dto.mapper.MedicalAppointmentMapper;
import pl.szczecin.business.DoctorService;
import pl.szczecin.business.MedicalAppointmentDateService;
import pl.szczecin.business.MedicalAppointmentService;
import pl.szczecin.domain.MedicalAppointmentRequest;

import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class MedicalAppointmentController {

    private static final String APPOINTMENT = "/appointment";

    private final MedicalAppointmentService medicalAppointmentService;
    private final MedicalAppointmentMapper medicalAppointmentMapper;
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

        var availableDatesAndStatus = medicalAppointmentDateService.findAvailableDates().stream()
                .map(medicalAppointmentDateMapper::map)
                .toList();
        var availableDates = availableDatesAndStatus.stream()
                .map(MedicalAppointmentDateDTO::getDateTime)
                .toList();

        return Map.of(
                "availableDoctorDTOs", availableDoctors,
                "availableDoctorPesels", availableDoctorPesels,
                "availableDates", availableDates,
                "medicalAppointmentDTO", MedicalAppointmentDTO.buildDefaultData()
        );
    }


    // @ModelAttribute("carPurchaseDTO") CarPurchaseDTO carPurchaseDTO - sa to dane z formularze wpisane przez usera
    @PostMapping(value = APPOINTMENT)
    public String makePurchase(
            @Valid @ModelAttribute("medicalAppointmentDTO") MedicalAppointmentDTO medicalAppointmentDTO,
            ModelMap model
    ) {
        MedicalAppointmentRequest request = medicalAppointmentMapper.map(medicalAppointmentDTO);
        medicalAppointmentService.makeAppointment(request);


//        CarPurchaseRequest request = carPurchaseMapper.map(medicalAppointmentDTO);
//        Invoice invoice = carPurchaseService.purchase(request);

        if (existingCustomerEmailExists(medicalAppointmentDTO.getExistingPatientEmail())) {
            model.addAttribute("existingPatientEmail", medicalAppointmentDTO.getExistingPatientEmail());
        } else {
            model.addAttribute("patientName", medicalAppointmentDTO.getPatientName());
            model.addAttribute("patientSurname", medicalAppointmentDTO.getPatientSurname());
        }

//        model.addAttribute("invoiceNumber", invoice.getInvoiceNumber());

        return "medical_appointment";
//        return "car_purchase_done";
    }

    private boolean existingCustomerEmailExists(String existingCustomerEmail) {
        return Objects.nonNull(existingCustomerEmail) && !existingCustomerEmail.isBlank();
    }

}
