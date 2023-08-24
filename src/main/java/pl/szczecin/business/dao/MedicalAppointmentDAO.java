package pl.szczecin.business.dao;

import pl.szczecin.domain.MedicalAppointment;

public interface MedicalAppointmentDAO {
    void makeAppointment(MedicalAppointment medicalAppointment);
}
