package pl.szczecin.business;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.szczecin.business.dao.MedicalAppointmentDateDAO;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.domain.exception.NotFoundException;

import java.time.OffsetDateTime;
import java.util.Collections;
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

    public List<MedicalAppointmentDate> findMedicalAppointmentDateByDate(
            OffsetDateTime medicalAppointmentDate) {
        List <MedicalAppointmentDate> date =
                medicalAppointmentDateDAO.findMedicalAppointmentDateByDate(medicalAppointmentDate);
        log.info("Available medicalAppointmentDates: [{}]", date.size());
        // lista z takimi samymi datami dla roznych lekarzy, wiec nas interesuje data
        return date;
    }
    public MedicalAppointmentDate findMedicalAppointmentDateByDateAndDoctor(
            OffsetDateTime medicalAppointmentDate,
            String doctorSurname) {
        Optional<MedicalAppointmentDate> date =
                medicalAppointmentDateDAO.findMedicalAppointmentDateByDateAndDoctor(
                        medicalAppointmentDate,
                        doctorSurname);
        if (date.isEmpty()) {
            throw new NotFoundException("Could not find medicalAppointmentDate by date and Doctor: [%s]".formatted(date));
        }
        return date.get();
    }

    public List<MedicalAppointmentDate> getAvailableDatesForDoctor(String doctorEmail) {
        List<MedicalAppointmentDate> availableDatesForDoctor =
                medicalAppointmentDateDAO.findAvailableDatesForDoctor(doctorEmail);
        log.info("Available dates for Doctor: [{}]", availableDatesForDoctor.size());
        return availableDatesForDoctor;
    }

    public List<MedicalAppointmentDate> getAllDatesForDoctor(String doctorEmail) {
        List<MedicalAppointmentDate> allDatesForDoctor =
                medicalAppointmentDateDAO.findAllDatesForDoctor(doctorEmail);
        log.info("All dates for Doctor: [{}]", allDatesForDoctor.size());
        return allDatesForDoctor;
    }

    @Transactional
    public MedicalAppointmentDate saveMedicalAppointmentDate(MedicalAppointmentDate medicalAppointmentDate) {
        return medicalAppointmentDateDAO.saveMedicalAppointmentDate(medicalAppointmentDate);
    }


}
