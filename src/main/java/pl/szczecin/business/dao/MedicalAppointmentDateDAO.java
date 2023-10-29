package pl.szczecin.business.dao;

import pl.szczecin.domain.MedicalAppointmentDate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface MedicalAppointmentDateDAO {

    List<MedicalAppointmentDate> findAvailableMedicalAppointmentDates();

    List<MedicalAppointmentDate> findAvailableDatesByDoctorEmail(String doctorEmail);

    List<MedicalAppointmentDate> findAllDatesByDoctorEmail(String doctorEmail);

    List<MedicalAppointmentDate> findAllFutureDatesByDoctorEmail(String doctorEmail);

    List<MedicalAppointmentDate> findAllHistoryDatesByDoctorEmail(String doctorEmail);

    List<MedicalAppointmentDate> findMedicalAppointmentDateByDate(OffsetDateTime medicalAppointmentDate);

    Optional<MedicalAppointmentDate> findMedicalAppointmentDateByDateAndDoctor(
            OffsetDateTime medicalAppointmentDate,
            String doctorEmail);

    MedicalAppointmentDate saveMedicalAppointmentDate(MedicalAppointmentDate medicalAppointmentDate);
}
