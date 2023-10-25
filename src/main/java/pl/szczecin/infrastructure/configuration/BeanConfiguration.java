package pl.szczecin.infrastructure.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    // nadpisalismy customowo sposob mapowania jsonow na obiekty w javie i odwrotnie

    // TODO po napisaniu aplikacji sprawdz zmiany parametrow w ponizszym beanie
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                // dotyczy pola w jsonie, ktorego nie ma odzwierciedlonego w klasie javowej
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // jezeli nastapi mapowanie obiektu javowego na jsona i wystapi pole nullowe
                // to ma sie nie pojawic w pliku json
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

}
