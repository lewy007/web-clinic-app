package pl.szczecin.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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


    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT
    )
    public Doctor findDoctorByEmail(String email) {
        return doctorDAO.findDoctorByEmail(email)
                .orElseThrow(() -> new NotFoundException("Could not find doctor by email: [%s]".formatted(email)));
    }


    public Doctor findDoctorBySurname(String surname) {
        return doctorDAO.findDoctorBySurname(surname)
                .orElseThrow(() -> new NotFoundException("Could not find doctor by surname: [%s]".formatted(surname)));
    }


    // wyciagamy z securityContext emaila zalogowanego pacjenta
    public String getLoggedInDoctorEmail() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(UserDetails.class::isInstance)
                .map(UserDetails.class::cast)
                .map(UserDetails::getUsername)
//                .orElseThrow(()-> new NotFoundException("Something went wrong because the email for logged-in doctor could not be found."));
                .orElse(Optional.empty().toString());
    }
}
