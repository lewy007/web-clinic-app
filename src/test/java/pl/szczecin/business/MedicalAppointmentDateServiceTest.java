package pl.szczecin.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szczecin.business.dao.MedicalAppointmentDateDAO;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.util.EntityFixtures;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MedicalAppointmentDateServiceTest {

    @Mock
    private MedicalAppointmentDateDAO medicalAppointmentDateDAO;

    @InjectMocks
    private MedicalAppointmentDateService medicalAppointmentDateService;

    @Test
    @DisplayName("That method should return all history medical appointment dates for Doctor")
    void testGetAllHistoryDatesByDoctorEmail() {
        // given
        String doctorEmail = "example@clinic.com";
        List<MedicalAppointmentDate> medicalAppointmentDates = Arrays.asList(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate2()
        );
        Mockito.when(medicalAppointmentDateDAO.findAllHistoryDatesByDoctorEmail(doctorEmail))
                .thenReturn(medicalAppointmentDates);

        // when
        List<MedicalAppointmentDate> result = medicalAppointmentDateService.getAllHistoryDatesByDoctorEmail(doctorEmail);

        // then
        Assertions.assertEquals(2, result.size()); // przykładowa asercja na liczbę zwróconych elementów
        // Możesz dodać więcej asercji w zależności od danych, które powinny być zwracane


    }

}