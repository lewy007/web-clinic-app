package pl.szczecin.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szczecin.infrastructure.database.entity.DoctorEntity;

import java.util.Optional;

@Repository
public interface DoctorJpaRepository extends JpaRepository<DoctorEntity, Integer> {

    Optional<DoctorEntity> findByPesel(String pesel);

    Optional<DoctorEntity> findBySurname(String surname);
}
