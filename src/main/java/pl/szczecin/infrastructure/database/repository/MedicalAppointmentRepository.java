package pl.szczecin.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczecin.business.dao.MedicalAppointmentDAO;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentEntity;
import pl.szczecin.infrastructure.database.repository.jpa.MedicalAppointmentJpaRepository;
import pl.szczecin.infrastructure.database.repository.mapper.MedicalAppointmentEntityMapper;

import java.util.List;

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
    public void makeAppointment(MedicalAppointment medicalAppointment) {

        MedicalAppointmentEntity medicalAppointmentToSave =
                medicalAppointmentEntityMapper.mapToEntity(medicalAppointment);

        medicalAppointmentJpaRepository.saveAndFlush(medicalAppointmentToSave);

    }

    @Override
    public void cancelMedicalAppointment(Integer medicalAppointmentDateId) {

        var medicalAppointmentEntityIdToDelete
                = medicalAppointmentJpaRepository
                .findByMedicalAppointmentDateId(medicalAppointmentDateId).getMedicalAppointmentId();

        System.out.println("To sie wyswietla przed usunieciem");
//        System.out.println("Usuwamy medicalAppointmentEntityToDelete: " + medicalAppointmentEntityToDelete);

        if (medicalAppointmentJpaRepository.existsById(medicalAppointmentEntityIdToDelete)) {
            System.out.println("wejscie w ifa do usuniecia");
            medicalAppointmentJpaRepository.deleteById(medicalAppointmentEntityIdToDelete);
        }

        System.out.println("To sie wyswietla po usunieciu");

    }

}
