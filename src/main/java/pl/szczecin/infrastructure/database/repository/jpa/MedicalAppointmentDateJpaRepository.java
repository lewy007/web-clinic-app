package pl.szczecin.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentDateEntity;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface MedicalAppointmentDateJpaRepository extends JpaRepository<MedicalAppointmentDateEntity, Integer> {


    @Query("""
            SELECT mad FROM MedicalAppointmentDateEntity mad
            WHERE mad.dateTime = :dateTime
            """)
    Optional<MedicalAppointmentDateEntity> findByDateTime(final @Param("dateTime") OffsetDateTime medicalAppointmentDate);
//    @Query("""
//            SELECT csr FROM CarServiceRequestEntity csr
//            WHERE csr.completedDateTime IS NULL
//            AND csr.car.vin = :vin
//            """)
//    Optional<MedicalAppointmentDateEntity> findByDateTime(String medicalAppointmentDate);
}
