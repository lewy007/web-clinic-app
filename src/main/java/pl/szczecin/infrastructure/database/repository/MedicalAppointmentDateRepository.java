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
    public List<MedicalAppointmentDate> findAvailableDatesByDoctorEmail(String doctorEmail) {
        return medicalAppointmentDateJpaRepository.findAvailableDatesByDoctorEmail(doctorEmail).stream()
                .filter(appointmentDate -> appointmentDate
                        .getDateTime().isAfter(OffsetDateTime.now().plusHours(1)))
                .map(medicalAppointmentDateEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public List<MedicalAppointmentDate> findAllFutureDatesByDoctorEmail(String doctorEmail) {
        return medicalAppointmentDateJpaRepository.findAllFutureDatesByDoctorEmail(doctorEmail).stream()
                .map(medicalAppointmentDateEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public List<MedicalAppointmentDate> findAllHistoryDatesByDoctorEmail(String doctorEmail) {
        return medicalAppointmentDateJpaRepository.findAllHistoryDatesByDoctorEmail(doctorEmail).stream()
                .map(medicalAppointmentDateEntityMapper::mapFromEntity)
                .toList();
    }


    @Override
    public Optional<MedicalAppointmentDate> findMedicalAppointmentDateByDateAndDoctor(
            OffsetDateTime medicalAppointmentDate,
            String doctorEmail) {
        return medicalAppointmentDateJpaRepository.findByDateTimeAndDoctor(medicalAppointmentDate, doctorEmail)
                .map(medicalAppointmentDateEntityMapper::mapFromEntity);
    }

    @Override
    public MedicalAppointmentDate saveMedicalAppointmentDate(MedicalAppointmentDate medicalAppointmentDate) {
        MedicalAppointmentDateEntity toSave = medicalAppointmentDateEntityMapper.mapToEntity(medicalAppointmentDate);
        MedicalAppointmentDateEntity saved = medicalAppointmentDateJpaRepository.save(toSave);
        return medicalAppointmentDateEntityMapper.mapFromEntity(saved);
    }

}
