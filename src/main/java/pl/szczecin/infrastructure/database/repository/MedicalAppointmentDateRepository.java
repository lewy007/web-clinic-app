package pl.szczecin.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.szczecin.business.dao.MedicalAppointmentDateDAO;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentDateEntity;
import pl.szczecin.infrastructure.database.repository.jpa.MedicalAppointmentDateJpaRepository;
import pl.szczecin.infrastructure.database.repository.mapper.MedicalAppointmentDateEntityMapper;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class MedicalAppointmentDateRepository implements MedicalAppointmentDateDAO {

    private final MedicalAppointmentDateJpaRepository medicalAppointmentDateJpaRepository;
    private final MedicalAppointmentDateEntityMapper medicalAppointmentDateEntityMapper;

    @Override
    public List<MedicalAppointmentDate> findAvailableMedicalAppointmentDates() {
        return medicalAppointmentDateJpaRepository.findAll().stream()
                .map(medicalAppointmentDateEntityMapper::mapFromEntity)
                .toList();
    }


    @Override
    public List<MedicalAppointmentDate> findAvailableDatesForDoctor(String doctorPesel) {
        return medicalAppointmentDateJpaRepository.findByDoctorPesel(doctorPesel).stream()
                .map(medicalAppointmentDateEntityMapper::mapFromEntity)
                .toList();
    }


    @Override
    public Optional<MedicalAppointmentDate> findMedicalAppointmentDateByDate(
            OffsetDateTime medicalAppointmentDate) {
        return medicalAppointmentDateJpaRepository.findByDateTime(medicalAppointmentDate)
                .map(medicalAppointmentDateEntityMapper::mapFromEntity);
    }
    @Override
    public Optional<MedicalAppointmentDate> findMedicalAppointmentDateByDateAndDoctor(
            OffsetDateTime medicalAppointmentDate,
            String doctorSurname) {
        return medicalAppointmentDateJpaRepository.findByDateTimeAndDoctor(medicalAppointmentDate, doctorSurname)
                .map(medicalAppointmentDateEntityMapper::mapFromEntity);
    }

    @Override
    public MedicalAppointmentDate saveMedicalAppointmentDate(MedicalAppointmentDate medicalAppointmentDate) {
        MedicalAppointmentDateEntity toSave = medicalAppointmentDateEntityMapper.mapToEntity(medicalAppointmentDate);
        MedicalAppointmentDateEntity saved = medicalAppointmentDateJpaRepository.save(toSave);
        return medicalAppointmentDateEntityMapper.mapFromEntity(saved);
    }

}
