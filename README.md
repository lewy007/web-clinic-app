## WEB CLINIC APPLICATION
### Aplikacja dla przychodni internetowej przeznaczona do rejestrowania się na wizyty lekarskie. Skierowana jest do dwóch grup: pacjentów i lekarzy.Każda z grup ma dostęp do innego obszaru aplikacji.
### Dostep do aplikacji pod linkiem -> http://localhost:8087/web-clinic-application/
### PACJENT:
* umawia się na wizytę (maksymalnie 1h przed zaplanowaną wizytą);
* posiada dostęp do historii swoich wizyt, zarówno odbytych jak i zaplanowanych (sortowanie za pomocą interfejsu Comparable zaimplementowanego w klasie MedicalAppointmentDTO w PatientHistoryDTO) - można podzielić na historię i zaplanowane wizyty;
* wyświetlana historia zawiera również kolumnę z notatką doktora po wizycie (przyszłe wizyty mają puste pola - null);
* odwołuje wizytę (maskymalnie 24 h przed zaplanowaną datą wizyty);
### DOKTOR:
* posiada dostęp do wszystkich wizyt pacjenta (sortowanie po dacie);
* posiada grafik dostępności na potencjalne wizyty (wolne terminy);
* posiada dostęp do historii pacjenta;
* może dodać notatkę dla pacjenta po wizycie lub zaktulizować już dodaną - w historii pacjenta.


## DO ZROBIENIA:
* Paginacja wyswietlanych wynikow
* rejestracja pacjenta bez konta (zakładanie konta)
* reset hasła