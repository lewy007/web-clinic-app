package pl.szczecin.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczecin.business.dao.DoctorDAO;
import pl.szczecin.domain.Doctor;
import pl.szczecin.infrastructure.database.repository.jpa.DoctorJpaRepository;
import pl.szczecin.infrastructure.database.repository.mapper.DoctorEntityMapper;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DoctorRepository implements DoctorDAO {

    private final DoctorJpaRepository doctorJpaRepository;
    private final DoctorEntityMapper doctorEntityMapper;


    @Override
    public List<Doctor> findAvailableDoctors() {
        return doctorJpaRepository.findAll().stream()
                .map(doctorEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public Optional<Doctor> findDoctorByEmail(String email) {
        return doctorJpaRepository.findByEmail(email)
                .map(doctorEntityMapper::mapFromEntity);
    }

    @Override
    public Optional<Doctor> findDoctorBySurname(String surname) {
        return doctorJpaRepository.findBySurname(surname)
                .map(doctorEntityMapper::mapFromEntity);
    }
}
