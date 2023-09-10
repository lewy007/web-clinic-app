package pl.szczecin.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.szczecin.domain.MedicalAppointmentDate;

import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "medicalAppointmentDateId")
@ToString(of = {"medicalAppointmentDateId", "dateTime"})
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medicalAppointmentDate")
public class MedicalAppointmentDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicalAppointmentDate_id")
    private Integer medicalAppointmentDateId;

    @Column(name = "date_time")
    private OffsetDateTime dateTime;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;

    @OneToOne(mappedBy = "medicalAppointmentDateEntity", fetch = FetchType.EAGER)
    private MedicalAppointmentEntity medicalAppointmentEntity;
}
