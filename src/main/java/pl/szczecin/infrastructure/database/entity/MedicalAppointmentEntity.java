package pl.szczecin.infrastructure.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(of = "medicalAppointmentId")
@ToString(of = {"medicalAppointmentId", "doctorNote"})
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medicalAppointment")
public class MedicalAppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicalAppointment_id")
    private Integer medicalAppointmentId;

    @Column(name = "doctor_note")
    private String doctorNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medical_appointment_date_id")
    private MedicalAppointmentDateEntity medicalAppointmentDateEntity;

}
