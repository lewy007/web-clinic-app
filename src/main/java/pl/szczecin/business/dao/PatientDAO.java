package pl.szczecin.business.dao;

import pl.szczecin.domain.Patient;
import pl.szczecin.domain.PatientHistory;

import java.util.List;
import java.util.Optional;

public interface PatientDAO {

    Patient savePatient(Patient patient);

    List<Patient> findAvailablePatients();

    Optional<Patient> findPatientByEmail(String email);

    PatientHistory findPatientHistoryByEmail(String patientEmail);

    PatientHistory findPatientScheduleByEmail(String patientEmail);

    PatientHistory findPatientAppointmentsToCancelByEmail(String patientEmail);
}
