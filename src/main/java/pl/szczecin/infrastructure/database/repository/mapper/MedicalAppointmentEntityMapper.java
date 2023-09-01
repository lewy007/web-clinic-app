package pl.szczecin.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MedicalAppointmentEntityMapper {

    MedicalAppointment mapFromEntity(MedicalAppointmentEntity medicalAppointmentEntity);


    // metoda do przemapowania pacjentow z aktualnymi wizytami do PatientHistory.MedicalAppointment
    // doctor note prezkazuje jako nulla, bo nie jest potrzebny na etapie odwolywania wizyty
    default PatientHistory.MedicalAppointment mapFromMedicalAppointmentEntityToPatientHistoryMedicalAppointment(
            MedicalAppointmentEntity medicalAppointmentEntity
    ) {
        return PatientHistory.MedicalAppointment.builder()
                .dateTime(medicalAppointmentEntity.getMedicalAppointmentDateEntity().getDateTime())
                .doctorName(medicalAppointmentEntity.getMedicalAppointmentDateEntity().getDoctor().getName())
                .doctorSurname(medicalAppointmentEntity.getMedicalAppointmentDateEntity().getDoctor().getSurname())
                .build();
    }



    // nie bylo zaleznosci miedzy tymi dwoma klasami, MapStruct nie umial powiazac mapowania
    // z MedicalAppointmentDate do MedicalAppointmentDateEntity, poniewaz nie ma miedzy tymi klasami zaleznosci
    // sa dopiero na poziomie warstwy perystencji (miedzy encjamI)
    @Mapping(target = "medicalAppointmentDateEntity", source = "medicalAppointmentDate")
    MedicalAppointmentEntity mapToEntity(MedicalAppointment medicalAppointment);


}
