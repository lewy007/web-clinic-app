package pl.szczecin.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentEntity;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface MedicalAppointmentJpaRepository extends JpaRepository<MedicalAppointmentEntity, Integer> {

    @Query("""
            SELECT ma FROM MedicalAppointmentEntity ma
            WHERE ma.medicalAppointmentDateEntity.medicalAppointmentDateId = :medicalAppointmentDateId
                       """)
    MedicalAppointmentEntity findByMedicalAppointmentDateId(
            final @Param("medicalAppointmentDateId") Integer medicalAppointmentDateId);


    // zwracamy tylko te wpisy gdzie w tabeli Medical Appointment klucz obcy medical appointment date id
    // posiada wartosc z listy parametrow metody - lista id wyciagnieta z innej metody
    // na podstawie zapytania do bazy danych o wszystkie daty na podstawie meila doctora
    @Query("""
            SELECT ma FROM MedicalAppointmentEntity ma
            WHERE ma.medicalAppointmentDateEntity.medicalAppointmentDateId IN :dateIds
            ORDER BY ma.medicalAppointmentDateEntity.dateTime ASC
            """)
    List<MedicalAppointmentEntity> findAllMedicalAppointmentByMADateID(
            @Param("dateIds") List<Integer> dateIds
    );


    @Query("""
            SELECT ma FROM MedicalAppointmentEntity ma
            WHERE ma.medicalAppointmentDateEntity.dateTime = :medicalAppointmentDate
            AND ma.patient.name = :patientName
            AND ma.patient.surname = :patientSurname
            AND ma.medicalAppointmentDateEntity.doctor.email = :doctorEmail
            """)
    MedicalAppointmentEntity findByDataFromRequest(
            @Param("medicalAppointmentDate") OffsetDateTime medicalAppointmentDate,
            @Param("patientName") String patientName,
            @Param("patientSurname") String patientSurname,
            @Param("doctorEmail") String doctorEmail
    );

}
