package pl.szczecin.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczecin.business.dao.MedicalAppointmentDAO;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentEntity;
import pl.szczecin.infrastructure.database.repository.jpa.MedicalAppointmentJpaRepository;
import pl.szczecin.infrastructure.database.repository.mapper.MedicalAppointmentEntityMapper;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class MedicalAppointmentRepository implements MedicalAppointmentDAO {

    private final MedicalAppointmentJpaRepository medicalAppointmentJpaRepository;
    private final MedicalAppointmentEntityMapper medicalAppointmentEntityMapper;


    @Override
    public List<MedicalAppointment> findAllMedicalAppointment() {
        return medicalAppointmentJpaRepository.findAll().stream()
                .map(medicalAppointmentEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public List<MedicalAppointment> findAllMedicalAppointmentByMADateID(
            List<Integer> allMedicalAppointmentDateIdForDoctor
    ) {
        return medicalAppointmentJpaRepository
                .findAllMedicalAppointmentByMADateID(allMedicalAppointmentDateIdForDoctor).stream()
                .map(medicalAppointmentEntityMapper::mapFromEntity)
                .toList();
    }


    @Override
    public MedicalAppointment makeAppointment(MedicalAppointment medicalAppointment) {

        MedicalAppointmentEntity medicalAppointmentToSave =
                medicalAppointmentEntityMapper.mapToEntity(medicalAppointment);

        MedicalAppointmentEntity saved = medicalAppointmentJpaRepository.saveAndFlush(medicalAppointmentToSave);

        return medicalAppointmentEntityMapper.mapFromEntity(saved);

    }

    @Override
    public MedicalAppointment cancelMedicalAppointment(Integer medicalAppointmentDateId) {

        // przypisanie w dwóch krokach, aby zwrocic MedicalAppointment do testow
        var medicalAppointmentEntityToDelete
                = medicalAppointmentJpaRepository
                .findByMedicalAppointmentDateId(medicalAppointmentDateId);

        var medicalAppointmentEntityIdToDelete
                = medicalAppointmentJpaRepository
                .findByMedicalAppointmentDateId(medicalAppointmentDateId).getMedicalAppointmentId();

        if (medicalAppointmentJpaRepository.existsById(medicalAppointmentEntityIdToDelete)) {
            medicalAppointmentJpaRepository.deleteById(medicalAppointmentEntityIdToDelete);
        }

        return medicalAppointmentEntityMapper.mapFromEntity(medicalAppointmentEntityToDelete);
    }

    @Override
    public Optional<MedicalAppointment> addNoteToMedicalAppointment(MedicalAppointmentRequest request) {

        OffsetDateTime medicalAppointmentDate = request.getMedicalAppointmentDate();
        String patientName = request.getPatientName();
        String patientSurname = request.getPatientSurname();
        String doctorEmail = request.getDoctorEmail();
        String doctorNote = request.getDoctorNote();

        // Sprawdzenie, czy rekord istnieje
        MedicalAppointmentEntity requestMedicalAppointment =
                medicalAppointmentJpaRepository.findByDataFromRequest(
                        medicalAppointmentDate,
                        patientName,
                        patientSurname,
                        doctorEmail);

        // Jeśli rekord istnieje, aktulizacja notatki lekarza
        if (requestMedicalAppointment != null) {
            requestMedicalAppointment.setDoctorNote(doctorNote);

            // Zapisz zaktualizowany rekord
            MedicalAppointmentEntity saved = medicalAppointmentJpaRepository.saveAndFlush(requestMedicalAppointment);

            return Optional.of(medicalAppointmentEntityMapper.mapFromEntity(saved));

        } else
            return Optional.empty();
    }
}
