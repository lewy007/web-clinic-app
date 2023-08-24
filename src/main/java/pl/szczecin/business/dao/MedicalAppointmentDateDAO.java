package pl.szczecin.business.dao;

import pl.szczecin.domain.MedicalAppointmentDate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface MedicalAppointmentDateDAO {

    List<MedicalAppointmentDate> findAvailableDates();

    Optional<MedicalAppointmentDate> findMedicalAppointmentDateByDate(OffsetDateTime medicalAppointmentDate);
}
