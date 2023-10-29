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


### DO ZROBIENIA:
* Paginacja wyswietlanych wynikow
* rejestracja pacjenta bez konta (zakładanie konta)
* reset hasła
* Actuator metric (do mierzenia ilosci zapytan do serwera) i Actuator health (czy z apka ok)


## REST API
## 1. Konfiguracja wstępna do wystawienia zwracanych elementów z kontrolera PatientRestControl.class:
* Utworzono klasę do obsługi błedów (GlobalExceptionRestHandler.class), która skonfigurowana w odpowiedni sposób, pozwala na dwie obslugi błędów jednoczesnie w projekcie. Restowa konfiguracja jest priorytetowa.
* Utworzono dedykowanego usera z dostępem do api -> test.user w grupie REST_API w SecurityConfiguration.class
## 2. Tworzenie własnej dokumentacji API
### 2.1. Dodajemy biblioteke do wygenerowania api
```yaml
dependencies {
  implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0'
}
```
### 2.2 Tworzymy konfiguracje w klasie. Klasa sluzy do wystawienia Swaggera. Beany w tej klasie sluza do wygenerowania automatycznie dokumentacji naszego API zgodnie ze specyfikacja OpenAPI
```java
@Configuration
public class SpringDocConfiguration {

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/**")
                .packagesToScan(WebClinicApplication.class.getPackageName())
                .build();
    }

    @Bean
    public OpenAPI springDocOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Web clinic application")
                        .contact(contact())
                        .version("1.0")
                );
    }

    @Bean
    public Contact contact() {
        return new Contact()
                .name("Peter from Szczecin")
                .url("https://mojastrona.pl")
                .email("lewy007@o2.pl");
    }
}
```
### Po uruchomieniu aplikacji można wejść w przeglądarce internetowej pod endpoint: http://localhost:8087/web-clinic-application/swagger-ui/index.html
### Pod tym linkiem jest dokumentacja mojego API. Następnie można przejć pod link http://localhost:8087/web-clinic-application/v3/api-docs/default, jest to kontrakt. Jest to faktyczna dokumentacja, która trzyma się specyfikacji OpenApi.
### Controlery dostępne z poziomy MVC zostały skopiowane i przerobione pod specyfikację API. Teraz można sprawdzać metody GET lub POST poprzez swaggera na ww. endpoincie.