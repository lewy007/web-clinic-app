package pl.szczecin.business;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    public MedicalAppointmentDate findMedicalAppointmentDateByDate(OffsetDateTime medicalAppointmentDate) {
        Optional<MedicalAppointmentDate> date =
                medicalAppointmentDateDAO.findMedicalAppointmentDateByDate(medicalAppointmentDate);
        if (date.isEmpty()) {
            throw new NotFoundException("Could not find medicalAppointmentDate by date: [%s]".formatted(date));
        }
        return date.get();
    }

    public List<MedicalAppointmentDate> getAvailableDatesForDoctor(String doctorPesel) {
        List<MedicalAppointmentDate> availableDatesForDoctor =
                medicalAppointmentDateDAO.findAvailableDatesForDoctor(doctorPesel);
        log.info("Available dates for Doctor: [{}]", availableDatesForDoctor.size());
        return availableDatesForDoctor;
    }


    @Transactional
    public MedicalAppointmentDate saveMedicalAppointmentDate(MedicalAppointmentDate medicalAppointmentDate) {
        return medicalAppointmentDateDAO.saveMedicalAppointmentDate(medicalAppointmentDate);
    }
}
