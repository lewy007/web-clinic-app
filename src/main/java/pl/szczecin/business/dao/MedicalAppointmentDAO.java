package pl.szczecin.business.dao;

import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentRequest;

import java.util.List;

public interface MedicalAppointmentDAO {
    List<MedicalAppointment> findAllMedicalAppointment();

    List<MedicalAppointment> findAllMedicalAppointmentByMADateID(List<Integer> allMedicalAppointmentDateIdForDoctor);

    void makeAppointment(MedicalAppointment medicalAppointment);

    void cancelMedicalAppointment(Integer medicalAppointmentDate);

    void addNoteToMedicalAppointment(MedicalAppointmentRequest request);
}
