package pl.szczecin.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczecin.business.dao.PatientDAO;
import pl.szczecin.domain.Patient;
import pl.szczecin.infrastructure.database.entity.PatientEntity;
import pl.szczecin.infrastructure.database.repository.jpa.PatientJpaRepository;
import pl.szczecin.infrastructure.database.repository.mapper.PatientEntityMapper;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PatientRepository implements PatientDAO {

    private final PatientJpaRepository patientJpaRepository;
    private final PatientEntityMapper patientEntityMapper;


    @Override
    public Patient savePatient(Patient patient) {

        PatientEntity toSave = patientEntityMapper.mapToEntity(patient);
        PatientEntity saved = patientJpaRepository.save(toSave);
        return patientEntityMapper.mapFromEntity(saved);
    }

    @Override
    public List<Patient> findAvailablePatients() {
        return patientJpaRepository.findAll().stream()
                .map(patientEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public Optional<Patient> findPatientByEmail(String email) {
        return patientJpaRepository.findByEmail(email)
                .map(patientEntityMapper::mapFromEntity);
    }

}
