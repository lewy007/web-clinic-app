package pl.szczecin.business.dao;

import pl.szczecin.domain.MedicalAppointment;

import java.util.List;

public interface MedicalAppointmentDAO {
    List<MedicalAppointment> findAllMedicalAppointment();

    List<MedicalAppointment> findAllMedicalAppointmentByMADateID(List<Integer> allMedicalAppointmentDateIdForDoctor);

    void makeAppointment(MedicalAppointment medicalAppointment);

    void cancelMedicalAppointment(Integer medicalAppointmentDate);
}
