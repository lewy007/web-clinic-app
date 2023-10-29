package pl.szczecin.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.szczecin.business.dao.MedicalAppointmentDAO;
import pl.szczecin.domain.*;

import java.util.List;

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

    @Transactional
    public MedicalAppointment makeAppointment(MedicalAppointmentRequest request) {

        // pobieramy pacjenta, doktora oraz date
        Patient existingPatient = patientService.findPatientByEmail(request.getPatientEmail());
        Doctor doctor = doctorService.findDoctorByEmail(request.getDoctorEmail());

        MedicalAppointmentDate medicalAppointmentDate = medicalAppointmentDateService
                .findMedicalAppointmentDateByDateAndDoctor(
                        request.getMedicalAppointmentDate(),
                        doctor.getEmail());

        // dodajemy do MedicalAppointmentDate Pole Doctor
        MedicalAppointmentDate medicalAppointmentDateToSave = medicalAppointmentDate.withDoctor(doctor);

        MedicalAppointment medicalAppointment = buildMedicalAppointment(existingPatient, medicalAppointmentDateToSave);

        medicalAppointmentDAO.makeAppointment(medicalAppointment);

        return medicalAppointment;
    }

    @Transactional
    public MedicalAppointment cancelAppointment(MedicalAppointmentRequest request) {

        // tutaj szukam na podstawie daty i lekarza, bo moze byc kilku lekarzy przyjmujacych na te sama godzine
        var medicalAppointmentDateId =
                medicalAppointmentDateService
                        .findMedicalAppointmentDateByDateAndDoctor(
                                request.getMedicalAppointmentDate(),
                                request.getDoctorEmail())
                        .getMedicalAppointmentDateId();


        // szukamy medicalAppointment w bazie na podstawie podanych parametrow
        return medicalAppointmentDAO.cancelMedicalAppointment(medicalAppointmentDateId);

    }

    @Transactional
    public MedicalAppointment addNoteToMedicalAppointment(MedicalAppointmentRequest request) {
       return medicalAppointmentDAO.addNoteToMedicalAppointment(request);
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


    // TODO nie uzywana metoda - do weryfikacji
    public List<MedicalAppointment> findAllMedicalAppointment() {
        List<MedicalAppointment> allMedicalAppointments = medicalAppointmentDAO.findAllMedicalAppointment();
        log.info("Available doctors: [{}]", allMedicalAppointments.size());
        return allMedicalAppointments;
    }
}
