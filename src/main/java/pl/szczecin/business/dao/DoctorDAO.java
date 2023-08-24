package pl.szczecin.business.dao;

import pl.szczecin.domain.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorDAO {

    List<Doctor> findAvailableDoctors();

    Optional<Doctor> findDoctorByPesel(String pesel);
}
