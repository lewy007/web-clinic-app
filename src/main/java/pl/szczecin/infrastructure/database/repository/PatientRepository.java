package pl.szczecin.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import pl.szczecin.business.dao.PatientDAO;
import pl.szczecin.domain.Patient;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.infrastructure.database.entity.PatientEntity;
import pl.szczecin.infrastructure.database.repository.jpa.PatientJpaRepository;
import pl.szczecin.infrastructure.database.repository.mapper.MedicalAppointmentEntityMapper;
import pl.szczecin.infrastructure.database.repository.mapper.PatientEntityMapper;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PatientRepository implements PatientDAO {

    private final PatientJpaRepository patientJpaRepository;
    private final PatientEntityMapper patientEntityMapper;
    private final MedicalAppointmentEntityMapper medicalAppointmentEntityMapper;


    @Override
    public Patient savePatient(Patient patient) {

        PatientEntity toSave = patientEntityMapper.mapToEntity(patient);
        PatientEntity saved = patientJpaRepository.saveAndFlush(toSave);
        return patientEntityMapper.mapFromEntity(saved);
    }

    @Override
    public List<Patient> findAvailablePatients(int pageNumber, int pageSize) {
        Sort sort = Sort.by("surname").ascending()
                .and(Sort.by("name")).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return patientJpaRepository.findAll(pageable).stream()
                .map(patientEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public Optional<Patient> findPatientByEmail(String email) {
        return patientJpaRepository.findOptionalByEmail(email)
                .map(patientEntityMapper::mapFromEntity);
    }

    @Override
    public PatientHistory findPatientHistoryByEmail(String patientEmail) {

        PatientEntity entity = patientJpaRepository.findByEmail(patientEmail);

        OffsetDateTime oneDayAgo = OffsetDateTime.now().minusDays(1);

        // Poniższe zapytanie pobiera tylko te spotkania, które już się odbyły (aktualizacja co jeden dzien)
        List<PatientHistory.MedicalAppointment> pastAppointments = entity.getMedicalAppointmentDetails().stream()
                .filter(appointment -> appointment.getMedicalAppointmentDateEntity().getDateTime()
                        .isBefore(oneDayAgo))
                .map(medicalAppointmentEntityMapper::mapFromMedicalAppointmentEntityToPatientHistoryMedicalAppointment)
                .toList();

        // Tworzymy nową instancję PatientHistory i ustawiamy w niej tylko odbyte spotkania
        PatientHistory patientHistoryAll = patientEntityMapper.mapFromEntity(entity, patientEmail);

        return patientHistoryAll.withMedicalAppointments(pastAppointments);
    }


    @Override
    public PatientHistory findPatientScheduleByEmail(String patientEmail) {

        PatientEntity entity = patientJpaRepository.findByEmail(patientEmail);

        OffsetDateTime oneDayAgo = OffsetDateTime.now().minusDays(1);

        // Poniższe zapytanie pobiera tylko te spotkania, które są zaplanowane dziś i dalej
        // (aktualizacja co jeden dzien)
        List<PatientHistory.MedicalAppointment> futureAppointments = entity.getMedicalAppointmentDetails().stream()
                .filter(appointment -> appointment.getMedicalAppointmentDateEntity().getDateTime()
                        .isAfter(oneDayAgo))
                .map(medicalAppointmentEntityMapper::mapFromMedicalAppointmentEntityToPatientHistoryMedicalAppointment)
                .toList();

        // Tworzymy nową instancję PatientHistory i ustawiamy w niej tylko przyszłe spotkania
        PatientHistory patientHistoryAll = patientEntityMapper.mapFromEntity(entity, patientEmail);

        return patientHistoryAll.withMedicalAppointments(futureAppointments);

    }

    @Override
    public PatientHistory findPatientAppointmentsToCancelByEmail(String patientEmail) {

        PatientEntity entity = patientJpaRepository.findByEmail(patientEmail);

        // Poniższe zapytanie pobiera tylko te spotkania, które odbywają się za minimum 24 godziny od teraz,
        // bo tak zakłada logika odwoływnaia wizyt
        List<PatientHistory.MedicalAppointment> futureAppointments = entity.getMedicalAppointmentDetails().stream()
                .filter(appointment -> {
                    OffsetDateTime appointmentDateTime = appointment.getMedicalAppointmentDateEntity().getDateTime();
                    OffsetDateTime now = OffsetDateTime.now().plusHours(24);
                    return appointmentDateTime.isAfter(now);
                })
                .map(medicalAppointmentEntityMapper::mapFromMedicalAppointmentEntityToPatientHistoryMedicalAppointment)
                .toList();

        // Tworzymy nową instancję PatientHistory i ustawiamy w niej tylko przyszłe spotkania
        PatientHistory patientHistoryAll = patientEntityMapper.mapFromEntity(entity, patientEmail);

        return patientHistoryAll.withMedicalAppointments(futureAppointments);

    }

}
