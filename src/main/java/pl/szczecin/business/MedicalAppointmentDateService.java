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
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MedicalAppointmentDateService {

    private final MedicalAppointmentDateDAO medicalAppointmentDateDAO;


    public List<MedicalAppointmentDate> findAvailableMedicalAppointmentDates() {
        List<MedicalAppointmentDate> availableDates = medicalAppointmentDateDAO.findAvailableMedicalAppointmentDates();
        log.info("Available medicalAppointmentDates: [{}]", availableDates.size());
        return availableDates;
    }


    public MedicalAppointmentDate findMedicalAppointmentDateByDateAndDoctor(
            OffsetDateTime medicalAppointmentDate,
            String doctorEmail) {
        Optional<MedicalAppointmentDate> date =
                medicalAppointmentDateDAO.findMedicalAppointmentDateByDateAndDoctor(
                        medicalAppointmentDate,
                        doctorEmail);
        if (date.isEmpty()) {
            throw new NotFoundException("Could not find medicalAppointmentDate by date and Doctor: [%s]".formatted(date));
        }
        return date.get();
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

    // ta metoda zluzyla do wyciagania wszytskich dat (przeszlych i przyszlych) do History
    //    public List<MedicalAppointmentDate> getAllDatesByDoctorEmail(String doctorEmail) {
    //        List<MedicalAppointmentDate> allDatesByDoctorEmail =
    //                medicalAppointmentDateDAO.findAllDatesByDoctorEmail(doctorEmail);
    //        log.info("All dates for Doctor: [{}]", allDatesByDoctorEmail.size());
    //        return allDatesByDoctorEmail;
    //    }


    // TODO nie uzywane metody - do weryfikacji

    @Transactional
    public MedicalAppointmentDate saveMedicalAppointmentDate(MedicalAppointmentDate medicalAppointmentDate) {
        return medicalAppointmentDateDAO.saveMedicalAppointmentDate(medicalAppointmentDate);
    }


    public List<MedicalAppointmentDate> findMedicalAppointmentDateByDate(
            OffsetDateTime medicalAppointmentDate) {
        List<MedicalAppointmentDate> date =
                medicalAppointmentDateDAO.findMedicalAppointmentDateByDate(medicalAppointmentDate);
        log.info("Available medicalAppointmentDates: [{}]", date.size());
        // lista z takimi samymi datami dla roznych lekarzy, wiec nas interesuje data
        return date;
    }
}
