package pl.szczecin.domain;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.With;

import java.time.OffsetDateTime;
import java.util.List;

@With
@Value
@Builder
@ToString(of = "patientEmail")
public class PatientHistory {

    String patientEmail;
    List<MedicalAppointment> medicalAppointments;


    public static final class MedicalAppointment {
        private final String doctorNote;
        private final OffsetDateTime dateTime;
        private final String doctorName;
        private final String doctorSurname;

        // dopisałem ale nie działa
        private final String doctorEmail;

        MedicalAppointment(String doctorNote, OffsetDateTime dateTime, String doctorName, String doctorSurname, String doctorEmail) {
            this.doctorNote = doctorNote;
            this.dateTime = dateTime;
            this.doctorName = doctorName;
            this.doctorSurname = doctorSurname;
            this.doctorEmail = doctorEmail;
        }

        public static MedicalAppointmentBuilder builder() {
            return new MedicalAppointmentBuilder();
        }

        public String getDoctorNote() {
            return this.doctorNote;
        }

        public OffsetDateTime getDateTime() {
            return this.dateTime;
        }

        public String getDoctorName() {
            return this.doctorName;
        }

        public String getDoctorSurname() {
            return this.doctorSurname;
        }

        public String getDoctorEmail() {
            return this.doctorEmail;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof MedicalAppointment)) return false;
            final MedicalAppointment other = (MedicalAppointment) o;
            final Object this$doctorNote = this.getDoctorNote();
            final Object other$doctorNote = other.getDoctorNote();
            if (this$doctorNote == null ? other$doctorNote != null : !this$doctorNote.equals(other$doctorNote))
                return false;
            final Object this$dateTime = this.getDateTime();
            final Object other$dateTime = other.getDateTime();
            if (this$dateTime == null ? other$dateTime != null : !this$dateTime.equals(other$dateTime)) return false;
            final Object this$doctorName = this.getDoctorName();
            final Object other$doctorName = other.getDoctorName();
            if (this$doctorName == null ? other$doctorName != null : !this$doctorName.equals(other$doctorName))
                return false;
            final Object this$doctorSurname = this.getDoctorSurname();
            final Object other$doctorSurname = other.getDoctorSurname();
            if (this$doctorSurname == null ? other$doctorSurname != null : !this$doctorSurname.equals(other$doctorSurname))
                return false;
            final Object this$doctorEmail = this.getDoctorEmail();
            final Object other$doctorEmail = other.getDoctorEmail();
            if (this$doctorEmail == null ? other$doctorEmail != null : !this$doctorEmail.equals(other$doctorEmail))
                return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $doctorNote = this.getDoctorNote();
            result = result * PRIME + ($doctorNote == null ? 43 : $doctorNote.hashCode());
            final Object $dateTime = this.getDateTime();
            result = result * PRIME + ($dateTime == null ? 43 : $dateTime.hashCode());
            final Object $doctorName = this.getDoctorName();
            result = result * PRIME + ($doctorName == null ? 43 : $doctorName.hashCode());
            final Object $doctorSurname = this.getDoctorSurname();
            result = result * PRIME + ($doctorSurname == null ? 43 : $doctorSurname.hashCode());
            final Object $doctorEmail = this.getDoctorEmail();
            result = result * PRIME + ($doctorEmail == null ? 43 : $doctorEmail.hashCode());
            return result;
        }

        public String toString() {
            return "PatientHistory.MedicalAppointment(doctorNote=" + this.getDoctorNote() + ")";
        }

        public static class MedicalAppointmentBuilder {
            private String doctorNote;
            private OffsetDateTime dateTime;
            private String doctorName;
            private String doctorSurname;
            private String doctorEmail;

            MedicalAppointmentBuilder() {
            }

            public MedicalAppointmentBuilder doctorNote(String doctorNote) {
                this.doctorNote = doctorNote;
                return this;
            }

            public MedicalAppointmentBuilder dateTime(OffsetDateTime dateTime) {
                this.dateTime = dateTime;
                return this;
            }

            public MedicalAppointmentBuilder doctorName(String doctorName) {
                this.doctorName = doctorName;
                return this;
            }

            public MedicalAppointmentBuilder doctorSurname(String doctorSurname) {
                this.doctorSurname = doctorSurname;
                return this;
            }

            public MedicalAppointmentBuilder doctorEmail(String doctorEmail) {
                this.doctorEmail = doctorEmail;
                return this;
            }

            public MedicalAppointment build() {
                return new MedicalAppointment(this.doctorNote, this.dateTime, this.doctorName, this.doctorSurname, this.doctorEmail);
            }

            public String toString() {
                return "PatientHistory.MedicalAppointment.MedicalAppointmentBuilder(doctorNote=" + this.doctorNote + ", dateTime=" + this.dateTime + ", doctorName=" + this.doctorName + ", doctorSurname=" + this.doctorSurname + ", doctorEmail=" + this.doctorEmail + ")";
            }
        }
    }

}
