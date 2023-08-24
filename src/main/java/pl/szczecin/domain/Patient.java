package pl.szczecin.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.With;

@With
@Value
@Builder
@EqualsAndHashCode(of = "email")
@ToString(of = {"patientId", "name", "surname", "phone", "email"})
public class Patient {

    Integer patientId;
    String name;
    String surname;
    String phone;
    String email;
    Address address;
}
