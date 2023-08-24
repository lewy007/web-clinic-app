package pl.szczecin.business.dao;

import pl.szczecin.domain.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientDAO {

    Patient savePatient(Patient patient);

    List<Patient> findAvailablePatients();

    Optional<Patient> findPatientByEmail(String email);
}
