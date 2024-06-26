package pl.szczecin.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.szczecin.business.dao.PatientDAO;
import pl.szczecin.domain.Address;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.domain.Patient;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.domain.exception.NotFoundException;
import pl.szczecin.infrastructure.security.UserEntity;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PatientService {

    private final PatientDAO patientDAO;
    private final PasswordEncoder passwordEncoder;

    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT
    )
    public Patient savePatient(MedicalAppointmentRequest medicalAppointmentRequest) {

        Patient newPatient = buildPatient(medicalAppointmentRequest);

        Patient savedPatient = patientDAO.savePatient(newPatient);
        log.info("New patient: [{}]", savedPatient);
        return savedPatient;
    }

    public List<Patient> findAvailablePatients(int pageNumber, int pageSize) {
        List<Patient> availablePatients = patientDAO.findAvailablePatients(pageNumber, pageSize);
        log.info("Available patients: [{}]", availablePatients.size());
        return availablePatients;
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT
    )
    public Patient findPatientByEmail(String email) {
        return patientDAO.findPatientByEmail(email)
                .orElseThrow(() -> new NotFoundException("Could not find patient by email: [%s]".formatted(email)));
    }

    public PatientHistory findPatientHistoryByEmail(String patientEmail) {
        PatientHistory patientHistoryByEmail = patientDAO.findPatientHistoryByEmail(patientEmail);
        log.info("Patient history by email: [{}]", patientHistoryByEmail.getPatientEmail());
        return patientHistoryByEmail;

    }

    public PatientHistory findPatientScheduleByEmail(String patientEmail) {
        PatientHistory patientScheduleByEmail = patientDAO.findPatientScheduleByEmail(patientEmail);
        log.info("Patient schedule by email: [{}]", patientScheduleByEmail.getPatientEmail());
        return patientScheduleByEmail;
    }

    public PatientHistory findPatientAppointmentsToCancelByEmail(String patientEmail) {
        PatientHistory patientScheduleByEmail = patientDAO.findPatientAppointmentsToCancelByEmail(patientEmail);
        log.info("Patient appointments to cancel by email: [{}]", patientScheduleByEmail.getPatientEmail());
        return patientScheduleByEmail;
    }


    // wyciagamy z securityContext emaila zalogowanego pacjenta
    public String getLoggedInPatientEmail() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(UserDetails.class::isInstance)
                .map(UserDetails.class::cast)
                .map(UserDetails::getUsername)
                .orElseThrow(() -> new NotFoundException(
                        "Something went wrong because the email for logged-in patient could not be found."));
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
                        .userName(inputData.getPatientName().toLowerCase()
                                + "."
                                + inputData.getPatientSurname().toLowerCase())
                        .email(inputData.getPatientEmail())
                        .password(passwordEncoder.encode(inputData.getPassword()))
                        .active(true)
                        .build())
                .build();
    }
}
