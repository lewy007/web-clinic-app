package pl.szczecin.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.szczecin.business.dao.PatientDAO;
import pl.szczecin.domain.Patient;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.domain.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PatientService {

    private final PatientDAO patientDAO;

    @Transactional
    public Patient savePatient(Patient patient) {
        return patientDAO.savePatient(patient);
    }

    @Transactional
    public Patient findPatientByEmail(String email) {
        Optional<Patient> patient = patientDAO.findPatientByEmail(email);
        if (patient.isEmpty()) {
            throw new NotFoundException("Could not find patient by email: [%s]".formatted(email));
        }
        return patient.get();
    }

    public PatientHistory findPatientHistoryByEmail(String patientEmail) {
        return patientDAO.findPatientHistoryByEmail(patientEmail);

    }

    public PatientHistory findCurrentPatientAppointmentsByEmail(String patientEmail) {
        return patientDAO.findCurrentPatientAppointmentsByEmail(patientEmail);
    }

    // wyciagamy z securityContext emaila zalogowanego pacjenta

    public String getLoggedInPatientEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return null;
    }


    // TODO nie wykorzystywana metoda - do weryfikacji
    @Transactional
    public List<Patient> findAvailablePatients() {
        List<Patient> availablePatients = patientDAO.findAvailablePatients();
        log.info("Available patients: [{}]", availablePatients.size());
        return availablePatients;

    }
}
