package pl.szczecin.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentDateEntity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalAppointmentDateJpaRepository extends JpaRepository<MedicalAppointmentDateEntity, Integer> {


    @Query("""
            SELECT mad FROM MedicalAppointmentDateEntity mad
            WHERE mad.dateTime = :dateTime
            """)
    List<MedicalAppointmentDateEntity> findByDateTime(
            final @Param("dateTime") OffsetDateTime medicalAppointmentDate);


    @Query("""
            SELECT mad FROM MedicalAppointmentDateEntity mad
            WHERE mad.dateTime = :dateTime
            AND mad.doctor.email = :doctorEmail
            """)
    Optional<MedicalAppointmentDateEntity> findByDateTimeAndDoctor(
            final @Param("dateTime") OffsetDateTime medicalAppointmentDate,
            final @Param("doctorEmail") String doctorEmail);


    @Query("""
            SELECT mad FROM MedicalAppointmentDateEntity mad 
            WHERE mad.status = true 
            AND mad.doctor.email = :doctorEmail 
            AND NOT EXISTS (SELECT ma FROM MedicalAppointmentEntity ma 
                           WHERE ma.medicalAppointmentDateEntity = mad)
                           """)
    List<MedicalAppointmentDateEntity> findAvailableDatesByDoctorEmail(final @Param("doctorEmail") String doctorEmail);

    @Query("""
            SELECT mad FROM MedicalAppointmentDateEntity mad 
            WHERE mad.doctor.email = :doctorEmail 
                           """)
    List<MedicalAppointmentDateEntity> findAllDatesByDoctorEmail(final @Param("doctorEmail") String doctorEmail);


    @Query("""
            SELECT mad FROM MedicalAppointmentDateEntity mad 
            WHERE mad.doctor.email = :doctorEmail
            AND (mad.dateTime >= CURRENT_TIMESTAMP OR FUNCTION('DATE', mad.dateTime) = FUNCTION('DATE', CURRENT_TIMESTAMP))
                           """)
    List<MedicalAppointmentDateEntity> findAllFutureDatesByDoctorEmail(
            final @Param("doctorEmail") String doctorEmail);


    //TODO LOGIKA do przetestowania czy aktualny dzien wejdzie do history czy schedule
    @Query("""
            SELECT mad FROM MedicalAppointmentDateEntity mad 
            WHERE mad.doctor.email = :doctorEmail
            AND (mad.dateTime < CURRENT_TIMESTAMP OR FUNCTION('DATE', mad.dateTime) = FUNCTION('DATE', CURRENT_TIMESTAMP))
                           """)
    List<MedicalAppointmentDateEntity> findAllHistoryDatesByDoctorEmail(
            final @Param("doctorEmail") String doctorEmail);


}
