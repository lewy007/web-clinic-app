## WEB CLINIC APPLICATION
### Aplikacja sluzy do rejestrowania sie na wizyty lekarskie. Skierowana jest do dwoch grup: pacjentow i lekarzy.
### Dostep do aplikacji pod linkiem -> http://localhost:8087/web-clinic-application/
### PACJENT:
* umowia sie na wizyte (maksymalnie 1h przed zaplanowana wizyta);
* posiada dostep do historii swoich wizyt, zarowno odbytych jak i zaplanowanych (sortowanie za pomoca Comparable zaimplementowanego w klasie MedicalAppointmentDTO w PatientHistoryDTO) - mozna podzielic na historie i zaplanowane wizyty;
* wyswietlana historia zawiera rowniez kolumne z notatka doktora po wizycie(przyszle wizyty maja puste pola);
* odwoluje wizyte (maskymalnie 24 h przed zaplanowana data wizyty);
### DOKTOR:
* posiada dostep do wszytskich wizyt (sortowania)
* posiada grafik dostepnosci (wolne terminy);
* posiada dostep do historii pacjenta;
* moze dodac notatke dla pacjenta po wizycie.


## DO ZROBIENIA:
### Sortowanie wyswietlanych wynikow plus paginacja
### Logika dla lekarza!