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
            AND mad.doctor.surname = :doctorSurname
            """)
    Optional<MedicalAppointmentDateEntity> findByDateTimeAndDoctor(
            final @Param("dateTime") OffsetDateTime medicalAppointmentDate,
            final @Param("doctorSurname") String doctorSurname);


    @Query("""
            SELECT mad FROM MedicalAppointmentDateEntity mad 
            WHERE mad.status = true 
            AND mad.doctor.email = :doctorEmail 
            AND NOT EXISTS (SELECT ma FROM MedicalAppointmentEntity ma 
                           WHERE ma.medicalAppointmentDateEntity = mad)
                           """)
    List<MedicalAppointmentDateEntity> findAvailableByDoctorEmail(final @Param("doctorEmail") String doctorEmail);

    @Query("""
            SELECT mad FROM MedicalAppointmentDateEntity mad 
            WHERE mad.doctor.email = :doctorEmail 
                           """)
    List<MedicalAppointmentDateEntity> findAllByDoctorEmail(final @Param("doctorEmail") String doctorEmail);


//    @Query("""
//            SELECT
//                mad.dateTime,
//                mad.status
//            FROM
//                MedicalAppointmentDateEntity mad
//            LEFT JOIN FETCH
//                MedicalAppointment mad.medicalAppointmentDateId medicalAppointmentDateId
//            WHERE
//                mad.dateTime = :dateTime
//                mad.status = true
//                AND (ma.medicalAppointmentId IS NULL OR ma.medicalAppointmentDateId IS NULL)
//            ORDER BY mad.dateTime
//            """)
//    Optional<MedicalAppointmentDateEntity> findByDateTimeAndStatus(
//            final @Param("dateTime") OffsetDateTime dateTime);


}
