package pl.szczecin.infrastructure.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.szczecin.WebClinicApplication;

// Klasa sluzy do wystawienia Swaggera
// Beany w tej klasie sluza do wygenerowania automatycznie dokumentacji naszego API zgodnie ze specyfikacja OpenAPI
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
                        .description("This is simple application for a small clinic.")
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
