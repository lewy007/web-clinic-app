package pl.szczecin.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentEntity;

@Repository
public interface MedicalAppointmentJpaRepository extends JpaRepository<MedicalAppointmentEntity, Integer> {

//    Optional<MedicalAppointmentDateEntity> findByDate(String medicalAppointmentDate);

}
