package pl.szczecin.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.szczecin.business.dao.PatientDAO;
import pl.szczecin.domain.Address;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.domain.Patient;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.domain.exception.NotFoundException;
import pl.szczecin.infrastructure.security.UserEntity;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PatientService {

    private final PatientDAO patientDAO;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Patient savePatient(MedicalAppointmentRequest medicalAppointmentRequest) {

        Patient newPatient = buildPatient(medicalAppointmentRequest);

        Patient savedPatient = patientDAO.savePatient(newPatient);
        log.info("New patient: [{}]", savedPatient);
        return savedPatient;
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
        PatientHistory patientHistoryByEmail = patientDAO.findPatientHistoryByEmail(patientEmail);
        log.info("Patient history by email: [{}]", patientHistoryByEmail.getPatientEmail());
        return patientHistoryByEmail;

    }

    public PatientHistory findCurrentPatientAppointmentsByEmail(String patientEmail) {
        PatientHistory currentPatientAppointmentsByEmail = patientDAO.findCurrentPatientAppointmentsByEmail(patientEmail);
        log.info("Patient current appointments by email: [{}]", currentPatientAppointmentsByEmail.getPatientEmail());
        return currentPatientAppointmentsByEmail;
    }


    // wyciagamy z securityContext emaila zalogowanego pacjenta
    public String getLoggedInPatientEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return null;
    }


    private Patient buildPatient(MedicalAppointmentRequest inputData) {
        return Patient.builder()
                .name(inputData.getPatientName())
                .surname(inputData.getPatientSurname())
                .phone(inputData.getPatientPhone())
                .email(inputData.getPatientEmail())
                .address(Address.builder()
                        .country(inputData.getPatientAddressCountry())
                        .city(inputData.getPatientAddressCity())
                        .postalCode(inputData.getPatientAddressPostalCode())
                        .address(inputData.getPatientAddressStreet())
                        .build())
                .userEntity(UserEntity.builder()
                        .userName(inputData.getPatientName())
                        .email(inputData.getPatientEmail())
                        .password(passwordEncoder.encode(inputData.getPassword()))
                        .active(true)
                        .build())
                .build();
    }

}
