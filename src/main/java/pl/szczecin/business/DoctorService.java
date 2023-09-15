package pl.szczecin.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.szczecin.business.dao.DoctorDAO;
import pl.szczecin.domain.Doctor;
import pl.szczecin.domain.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class DoctorService {

    private final DoctorDAO doctorDAO;

    public List<Doctor> findAvailableDoctors() {
        List<Doctor> availableDoctors = doctorDAO.findAvailableDoctors();
        log.info("Available doctors: [{}]", availableDoctors.size());
        return availableDoctors;
    }

    @Transactional
    public Doctor findDoctorByEmail(String email) {
        Optional<Doctor> doctor = doctorDAO.findDoctorByEmail(email);
        if (doctor.isEmpty()) {
            throw new NotFoundException("Could not find doctor by email: [%s]".formatted(email));
        }
        return doctor.get();
    }

    public Doctor findDoctorBySurname(String surname) {
        Optional<Doctor> doctor = doctorDAO.findDoctorBySurname(surname);
        if (doctor.isEmpty()) {
            throw new NotFoundException("Could not find doctor by surname: [%s]".formatted(surname));
        }
        return doctor.get();
    }

    // wyciagamy z security emaila zalogowanego doctora
    public String getLoggedInDoctorEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();  // Załóżmy, że email lekarza jest przechowywany jako username
        }
        return null;
    }
}
