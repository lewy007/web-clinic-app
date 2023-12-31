package pl.szczecin.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.With;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "email")
@ToString(of = {"doctorId", "name", "surname", "email"})
public class Doctor {

    Integer doctorId;
    String name;
    String surname;
    String email;
    Set<MedicalAppointmentDate> appointmentsDate;

    // dodajemy getter z palca, zeby uchronic sie przed NullPointerException
    public Set<MedicalAppointmentDate> getAppointmentsDate() {
        return Objects.isNull(appointmentsDate) ? new HashSet<>() : appointmentsDate;
    }

}
