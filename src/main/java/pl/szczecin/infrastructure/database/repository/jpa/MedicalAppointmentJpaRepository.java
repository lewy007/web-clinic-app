package pl.szczecin.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentEntity;

@Repository
public interface MedicalAppointmentJpaRepository extends JpaRepository<MedicalAppointmentEntity, Integer> {

    @Query("""
            SELECT ma FROM MedicalAppointmentEntity ma
            WHERE ma.medicalAppointmentDateEntity.medicalAppointmentDateId = :medicalAppointmentDateId
                       """)
    MedicalAppointmentEntity findByMedicalAppointmentDateId(
            final @Param("medicalAppointmentDateId") Integer medicalAppointmentDateId);


//    @Query("""
//            DELETE FROM MedicalAppointmentEntity ma
//            WHERE ma.patient.email = :patient
//            AND ma.medicalAppointmentDateEntity.dateTime = :medicalAppointmentDate
//            """)
//    void deleteByPatientEmailAndDateTime(
//            final @Param("patientEmail") String patientEmail,
//            final @Param("medicalAppointmentDate") OffsetDateTime medicalAppointmentDate
//    );

//    Optional<MedicalAppointmentDateEntity> findByDate(String medicalAppointmentDate);

}
