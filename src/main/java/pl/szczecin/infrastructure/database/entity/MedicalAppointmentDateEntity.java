package pl.szczecin.infrastructure.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;
}
