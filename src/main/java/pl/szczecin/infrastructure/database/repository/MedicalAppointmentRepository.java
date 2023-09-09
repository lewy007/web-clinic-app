package pl.szczecin.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczecin.business.dao.MedicalAppointmentDAO;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentEntity;
import pl.szczecin.infrastructure.database.repository.jpa.MedicalAppointmentJpaRepository;
import pl.szczecin.infrastructure.database.repository.mapper.MedicalAppointmentEntityMapper;

@Repository
@AllArgsConstructor
public class MedicalAppointmentRepository implements MedicalAppointmentDAO {

    private final MedicalAppointmentJpaRepository medicalAppointmentJpaRepository;
    private final MedicalAppointmentEntityMapper medicalAppointmentEntityMapper;


    @Override
    public void makeAppointment(MedicalAppointment medicalAppointment) {

        MedicalAppointmentEntity medicalAppointmentToSave =
                medicalAppointmentEntityMapper.mapToEntity(medicalAppointment);

        medicalAppointmentJpaRepository.saveAndFlush(medicalAppointmentToSave);

    }

    @Override
    public void cancelMedicalAppointment(Integer medicalAppointmentDateId) {

        MedicalAppointmentEntity medicalAppointmentEntityToDelete
                = medicalAppointmentJpaRepository.findByMedicalAppointmentDateId(medicalAppointmentDateId);

        medicalAppointmentJpaRepository.delete(medicalAppointmentEntityToDelete);
    }

}
