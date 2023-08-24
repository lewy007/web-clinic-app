package pl.szczecin.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szczecin.business.dao.MedicalAppointmentDAO;
import pl.szczecin.domain.Address;
import pl.szczecin.domain.Doctor;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.domain.Patient;

import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class MedicalAppointmentService {

    private MedicalAppointmentDAO medicalAppointmentDAO;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final MedicalAppointmentDateService medicalAppointmentDateService;

    @Transactional
    public MedicalAppointment makeAppointment(MedicalAppointmentRequest request) {
        return existingPatientEmailExists(request.getExistingPatientEmail())
                ? processNextTimeToMakeAnAppointment(request)
                : processFirstTimeToMakeAnAppointment(request);
    }

    private MedicalAppointment processFirstTimeToMakeAnAppointment(MedicalAppointmentRequest request) {

        // wyciagamy doktora, ktorego pacjent wybral
        Doctor doctorByPesel = doctorService.findDoctorByPesel(request.getDoctorPesel());
        // wyciagamy date, ktora pacjent wybral
        MedicalAppointmentDate date =
                medicalAppointmentDateService.findMedicalAppointmentDateByDate(request.getMedicalAppointmentDate());
//        CarToBuy car = carService.findCarToBuy(request.getCarVin());
//        Salesman salesman = salesmanService.findSalesman(request.getSalesmanPesel());
//        Invoice invoice = buildInvoice(car, salesman);
//
        Patient patient = buildPatient(request);
//        customerService.issueInvoice(customer);
//        return invoice;
        return null;
    }

    private MedicalAppointment processNextTimeToMakeAnAppointment(MedicalAppointmentRequest request) {

        Patient existingPatient = patientService.findPatientByEmail(request.getExistingPatientEmail());
        Doctor doctor = doctorService.findDoctorByPesel(request.getDoctorPesel());
        MedicalAppointmentDate medicalAppointmentDate =
                medicalAppointmentDateService.findMedicalAppointmentDateByDate(request.getMedicalAppointmentDate());
        MedicalAppointmentDate medicalAppointmentAndDoctor = medicalAppointmentDate.withDoctor(doctor);

        // pobieramy liste(set) dat doktora i dodajemy date wybrana przez klienta
//        Set<MedicalAppointmentDate> existingAppointmentsDate = doctor.getAppointmentsDate();
//        existingAppointmentsDate.add(medicalAppointmentDate);


        MedicalAppointment medicalAppointment = buildMedicalAppointment(existingPatient, medicalAppointmentAndDoctor);

        medicalAppointmentDAO.makeAppointment(medicalAppointment);
        return medicalAppointment;


//        Customer existingCustomer = customerService.findCustomer(request.getExistingCustomerEmail());
//        CarToBuy car = carService.findCarToBuy(request.getCarVin());
//        Salesman salesman = salesmanService.findSalesman(request.getSalesmanPesel());
//        Invoice invoice = buildInvoice(car, salesman);
//        Set<Invoice> existingInvoices = existingCustomer.getInvoices();
//        existingInvoices.add(invoice);
//        customerService.issueInvoice(existingCustomer.withInvoices(existingInvoices));
//        return invoice;
    }

    private boolean existingPatientEmailExists(String email) {
        return Objects.nonNull(email) && !email.isBlank();
    }

    private MedicalAppointment buildMedicalAppointment(
            Patient existingPatient,
            MedicalAppointmentDate medicalAppointmentDate) {
        return MedicalAppointment.builder()
                .medicalAppointmentId(100)
                .doctorNote("")
                .patient(existingPatient)
                .medicalAppointmentDate(medicalAppointmentDate)
                .build();
    }

//    private Invoice buildInvoice(CarToBuy car, Salesman salesman) {
//        return Invoice.builder()
//                .invoiceNumber(UUID.randomUUID().toString())
//                .dateTime(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.UTC))
//                .car(car)
//                .salesman(salesman)
//                .build();
//    }

    private Patient buildPatient(MedicalAppointmentRequest inputData) {
        return Patient.builder()
                .name(inputData.getPatientName())
                .surname(inputData.getPatientSurname())
                .phone(inputData.getPatientPhone())
                .email(inputData.getPatientEmail())
                .address(Address.builder()
                        .country(inputData.getPatientAddressCountry())
                        .city(inputData.getPatientAddressCity())
                        .postalCode(inputData.getPatientAddressPostalCode())
                        .address(inputData.getPatientAddressStreet())
                        .build())
//                .invoices(Set.of(invoice))
                .build();
    }

}
