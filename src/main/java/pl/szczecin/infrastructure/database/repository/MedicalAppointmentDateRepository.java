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
    public List<MedicalAppointmentDate> findAvailableDatesForDoctor(String doctorEmail) {
        return medicalAppointmentDateJpaRepository.findAvailableByDoctorEmail(doctorEmail).stream()
                .filter(appointmentDate -> appointmentDate
                        .getDateTime().isAfter(OffsetDateTime.now().plusHours(1)))
                .map(medicalAppointmentDateEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public List<MedicalAppointmentDate> findAllDatesForDoctor(String doctorEmail) {
        return medicalAppointmentDateJpaRepository.findAllByDoctorEmail(doctorEmail).stream()
                .map(medicalAppointmentDateEntityMapper::mapFromEntity)
                .toList();
    }


    @Override
    public List<MedicalAppointmentDate> findMedicalAppointmentDateByDate(
            OffsetDateTime medicalAppointmentDate) {
        return medicalAppointmentDateJpaRepository.findByDateTime(medicalAppointmentDate).stream()
                .map(medicalAppointmentDateEntityMapper::mapFromEntity)
                .toList();
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
