package pl.szczecin.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MedicalAppointmentEntityMapper {


    // zwracany medicalAppointment mial wartosc nullowa medicalAppointmentDateId, wiec
    // trzeba powiazac mapowanie z medicalAppointmentDateEntity na medicalAppointmentDate
    // oraz zablokowac pozostale mapowania bo dostajemy stack overflow error
    @Mapping(target = "medicalAppointmentDate", source = "medicalAppointmentDateEntity")
    @Mapping(target = "patient.address", ignore = true)
    @Mapping(target = "doctor.appointmentsDate", ignore = true)
    @Mapping(target = "medicalAppointmentDate.doctor", ignore = true)
    MedicalAppointment mapFromEntity(MedicalAppointmentEntity medicalAppointmentEntity);


    // metoda do przemapowania pacjentow z aktualnymi wizytami do PatientHistory.MedicalAppointment
    // doctor note przekazuje jako nulla, bo nie jest potrzebny na etapie odwolywania wizyty
    default PatientHistory.MedicalAppointment mapFromMedicalAppointmentEntityToPatientHistoryMedicalAppointment(
            MedicalAppointmentEntity medicalAppointmentEntity
    ) {
        return PatientHistory.MedicalAppointment.builder()
                .dateTime(medicalAppointmentEntity.getMedicalAppointmentDateEntity().getDateTime())
                .doctorName(medicalAppointmentEntity.getMedicalAppointmentDateEntity().getDoctor().getName())
                .doctorSurname(medicalAppointmentEntity.getMedicalAppointmentDateEntity().getDoctor().getSurname())
                .doctorEmail(medicalAppointmentEntity.getMedicalAppointmentDateEntity().getDoctor().getEmail())
                .doctorNote(medicalAppointmentEntity.getDoctorNote())
                .build();
    }


    // nie bylo zaleznosci miedzy tymi dwoma klasami, MapStruct nie umial powiazac mapowania
    // z MedicalAppointmentDate do MedicalAppointmentDateEntity, poniewaz nie ma miedzy tymi klasami zaleznosci
    // sa dopiero na poziomie warstwy perystencji (miedzy encjamI)
    @Mapping(target = "medicalAppointmentDateEntity", source = "medicalAppointmentDate")
    MedicalAppointmentEntity mapToEntity(MedicalAppointment medicalAppointment);


}
