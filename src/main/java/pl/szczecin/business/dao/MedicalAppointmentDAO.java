package pl.szczecin.business.dao;

import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentRequest;

import java.util.List;
import java.util.Optional;

public interface MedicalAppointmentDAO {
    List<MedicalAppointment> findAllMedicalAppointment();

    List<MedicalAppointment> findAllMedicalAppointmentByMADateID(List<Integer> allMedicalAppointmentDateIdForDoctor);

    MedicalAppointment makeAppointment(MedicalAppointment medicalAppointment);

    MedicalAppointment cancelMedicalAppointment(Integer medicalAppointmentDate);

    Optional<MedicalAppointment> addNoteToMedicalAppointment(MedicalAppointmentRequest request);
}
