package pl.szczecin.business.dao;

import pl.szczecin.domain.MedicalAppointmentDate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface MedicalAppointmentDateDAO {

    List<MedicalAppointmentDate> findAvailableMedicalAppointmentDates();

    List<MedicalAppointmentDate> findAvailableDatesForDoctor(String doctorEmail);

    List<MedicalAppointmentDate> findAllDatesForDoctor(String doctorEmail);

    List<MedicalAppointmentDate> findMedicalAppointmentDateByDate(OffsetDateTime medicalAppointmentDate);

    Optional<MedicalAppointmentDate> findMedicalAppointmentDateByDateAndDoctor(
            OffsetDateTime medicalAppointmentDate,
            String doctorSurname);

    MedicalAppointmentDate saveMedicalAppointmentDate(MedicalAppointmentDate medicalAppointmentDate);
}
