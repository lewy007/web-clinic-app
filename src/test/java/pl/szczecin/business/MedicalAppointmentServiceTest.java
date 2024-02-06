package pl.szczecin.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szczecin.business.dao.MedicalAppointmentDAO;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.util.EntityFixtures;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MedicalAppointmentServiceTest {

    @Mock
    private MedicalAppointmentDAO medicalAppointmentDAO;

    @InjectMocks
    private MedicalAppointmentService medicalAppointmentService;

    @Test
    @DisplayName("That method should return all medical appointments with given MADateID for Doctor")
    void testFindAllMedicalAppointmentByMADateID() {
        // given
        List<Integer> medicalAppointmentDateIds = Arrays.asList(1, 2, 3);
        List<MedicalAppointment> medicalAppointments = Arrays.asList(
                EntityFixtures.someMedicalAppointment1(),
                EntityFixtures.someMedicalAppointment2(),
                EntityFixtures.someMedicalAppointment3()
        );

        Mockito.when(medicalAppointmentDAO.findAllMedicalAppointmentByMADateID(medicalAppointmentDateIds))
                .thenReturn(medicalAppointments);

        // when
        List<MedicalAppointment> result = medicalAppointmentService.findAllMedicalAppointmentByMADateID(medicalAppointmentDateIds);

        // then
        Assertions.assertEquals(3, result.size()); // przykładowa asercja na liczbę zwróconych elementów
        // Możesz dodać więcej asercji w zależności od danych, które powinny być zwracane

        // Sprawdź czy wszystkie zwrócone obiekty mają niepusty identyfikator
        for (MedicalAppointment appointment : result) {
            Assertions.assertNotNull(appointment.getMedicalAppointmentDate().getMedicalAppointmentDateId());
        }

        // Możesz dodać więcej asercji w zależności od danych, które powinny być zwracane
    }
}
