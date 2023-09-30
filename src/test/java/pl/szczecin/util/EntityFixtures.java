package pl.szczecin.util;

import lombok.experimental.UtilityClass;
import pl.szczecin.api.dto.DoctorDTO;
import pl.szczecin.domain.Doctor;

@UtilityClass
public class EntityFixtures {


    public static Doctor someDoctor1() {
        return Doctor.builder()
                .name("Edyta")
                .surname("Kowalska")
                .email("edyta.kowalska@clinic.pl")
                .build();
    }

    public static Doctor someDoctor2() {
        return Doctor.builder()
                .name("Alina")
                .surname("Nowak")
                .email("alina.nowak@clinic.pl")
                .build();
    }

    public static Doctor someDoctor3() {
        return Doctor.builder()
                .name("Malwina")
                .surname("Malinowska")
                .email("malwina.malinowska@clinic.pl")
                .build();
    }
    public static DoctorDTO someDoctorDTO1() {
        return DoctorDTO.builder()
                .name("Edyta")
                .surname("Kowalska")
                .email("edyta.kowalska@clinic.pl")
                .build();
    }

    public static DoctorDTO someDoctorDTO2() {
        return DoctorDTO.builder()
                .name("Alina")
                .surname("Nowak")
                .email("alina.nowak@clinic.pl")
                .build();
    }

    public static DoctorDTO someDoctorDTO3() {
        return DoctorDTO.builder()
                .name("Malwina")
                .surname("Malinowska")
                .email("malwina.malinowska@clinic.pl")
                .build();
    }

}
