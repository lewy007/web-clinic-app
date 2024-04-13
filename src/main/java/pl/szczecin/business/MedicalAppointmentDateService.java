package pl.szczecin.business;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szczecin.business.dao.MedicalAppointmentDateDAO;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.domain.exception.NotFoundException;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MedicalAppointmentDateService {

    private final MedicalAppointmentDateDAO medicalAppointmentDateDAO;


    public MedicalAppointmentDate findMedicalAppointmentDateByDateAndDoctorEmail(
            OffsetDateTime medicalAppointmentDate,
            String doctorEmail) {
        return medicalAppointmentDateDAO.findMedicalAppointmentDateByDateAndDoctorEmail(medicalAppointmentDate, doctorEmail)
                .orElseThrow(() -> new NotFoundException(
                        "Could not find medicalAppointmentDate by date: [%s] and doctor: [%s]"
                                .formatted(medicalAppointmentDate, doctorEmail)));
    }


    public List<MedicalAppointmentDate> getAvailableDatesByDoctorEmail(String doctorEmail) {
        List<MedicalAppointmentDate> availableDatesByDoctorEmail =
                medicalAppointmentDateDAO.findAvailableDatesByDoctorEmail(doctorEmail);
        log.info("Available dates for Doctor: [{}]", availableDatesByDoctorEmail.size());
        return availableDatesByDoctorEmail;
    }


    public List<MedicalAppointmentDate> getAllHistoryDatesByDoctorEmail(String doctorEmail) {
        List<MedicalAppointmentDate> allHistoryDatesByDoctorEmail =
                medicalAppointmentDateDAO.findAllHistoryDatesByDoctorEmail(doctorEmail);
        log.info("All history dates for Doctor: [{}]", allHistoryDatesByDoctorEmail.size());
        return allHistoryDatesByDoctorEmail;
    }


    public List<MedicalAppointmentDate> getAllFutureDatesByDoctorEmail(String doctorEmail) {
        List<MedicalAppointmentDate> allFutureDatesByDoctorEmail =
                medicalAppointmentDateDAO.findAllFutureDatesByDoctorEmail(doctorEmail);
        log.info("All future dates for Doctor: [{}]", allFutureDatesByDoctorEmail.size());
        return allFutureDatesByDoctorEmail;
    }


    // TODO nie uzywana metoda - do weryfikacji, ale testy do niej sa napisane
    @Transactional
    public MedicalAppointmentDate saveMedicalAppointmentDate(MedicalAppointmentDate medicalAppointmentDate) {
        return medicalAppointmentDateDAO.saveMedicalAppointmentDate(medicalAppointmentDate);
    }

}
