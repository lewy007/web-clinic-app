package pl.szczecin.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.szczecin.business.dao.MedicalAppointmentDAO;
import pl.szczecin.domain.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class MedicalAppointmentService {

    private MedicalAppointmentDAO medicalAppointmentDAO;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final MedicalAppointmentDateService medicalAppointmentDateService;


    public List<MedicalAppointment> findAllMedicalAppointmentByMADateID(
            List<Integer> allMedicalAppointmentDateIdForDoctor
    ) {
        List<MedicalAppointment> allMedicalAppointments =
                medicalAppointmentDAO.findAllMedicalAppointmentByMADateID(allMedicalAppointmentDateIdForDoctor);
        log.info("All medical appointments: [{}]", allMedicalAppointments.size());
        return allMedicalAppointments;
    }

//    @Transactional
//    public MedicalAppointment makeAppointment(MedicalAppointmentRequest request) {
//        return existingPatientEmailExists(request.getExistingPatientEmail())
//                ? processNextTimeToMakeAnAppointment(request)
//                : processFirstTimeToMakeAnAppointment(request);
//    }

//    private MedicalAppointment processFirstTimeToMakeAnAppointment(MedicalAppointmentRequest request) {
//
//        // wyciagamy doktora, ktorego pacjent wybral
//        Doctor doctor = doctorService.findDoctorByEmail(request.getDoctorEmail());
//
//        // wyciagamy date przypisana do lekarza, ktora pacjent wybral
//        MedicalAppointmentDate medicalAppointmentDate = medicalAppointmentDateService
//                .findMedicalAppointmentDateByDateAndDoctor(
//                        request.getMedicalAppointmentDate(),
//                        doctor.getSurname());
//
//        // dodajemy do MedicalAppointmentDate Pole Doctor, ktore nie moze byc nullem
//        MedicalAppointmentDate medicalAppointmentDateToSave = medicalAppointmentDate.withDoctor(doctor);
//
//        Patient newPatient = buildPatient(request);
//        Patient savedPatient = patientService.savePatient(newPatient);
//
//        MedicalAppointment medicalAppointment = buildMedicalAppointment(savedPatient, medicalAppointmentDateToSave);
//
//        medicalAppointmentDAO.makeAppointment(medicalAppointment);
//        return medicalAppointment;
//
//    }

    @Transactional
    public MedicalAppointment processNextTimeToMakeAnAppointment(MedicalAppointmentRequest request) {

        // pobieramy pacjenta, doktora oraz date
        Patient existingPatient = patientService.findPatientByEmail(request.getPatientEmail());
        Doctor doctor = doctorService.findDoctorByEmail(request.getDoctorEmail());

        MedicalAppointmentDate medicalAppointmentDate = medicalAppointmentDateService
                .findMedicalAppointmentDateByDateAndDoctor(
                        request.getMedicalAppointmentDate(),
                        doctor.getSurname());

        // dodajemy do MedicalAppointmentDate Pole Doctor
        MedicalAppointmentDate medicalAppointmentDateToSave = medicalAppointmentDate.withDoctor(doctor);

        MedicalAppointment medicalAppointment = buildMedicalAppointment(existingPatient, medicalAppointmentDateToSave);

        medicalAppointmentDAO.makeAppointment(medicalAppointment);
        return medicalAppointment;

    }

    @Transactional
    public void cancelAppointment(MedicalAppointmentRequest request) {


        // tutaj szukam na podstawie daty i lekarza, bo moze byc kilku lekarzy przyjmujacych na te sama godzine
        var medicalAppointmentDateId =
                medicalAppointmentDateService
                        .findMedicalAppointmentDateByDateAndDoctor(
                                request.getMedicalAppointmentDate(),
                                request.getDoctorSurname())
                        .getMedicalAppointmentDateId();


        // szukamy medicalAppointment w bazie na podstawie podanych parametrow
        medicalAppointmentDAO.cancelMedicalAppointment(medicalAppointmentDateId);

    }

    public void addNoteToMedicalAppointment(MedicalAppointmentRequest request) {
        medicalAppointmentDAO.addNoteToMedicalAppointment(request);
    }


    private boolean existingPatientEmailExists(String email) {
        return Objects.nonNull(email) && !email.isBlank();
    }

    private MedicalAppointment buildMedicalAppointment(
            Patient existingPatient,
            MedicalAppointmentDate medicalAppointmentDate) {
        return MedicalAppointment.builder()
                .doctorNote(null)
                .patient(existingPatient)
                .medicalAppointmentDate(medicalAppointmentDate)
                .build();
    }


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
                .build();
    }


    // TODO nie uzywana metoda - do weryfikacji
    public List<MedicalAppointment> findAllMedicalAppointment() {
        List<MedicalAppointment> allMedicalAppointments = medicalAppointmentDAO.findAllMedicalAppointment();
        log.info("Available doctors: [{}]", allMedicalAppointments.size());
        return allMedicalAppointments;
    }
}
