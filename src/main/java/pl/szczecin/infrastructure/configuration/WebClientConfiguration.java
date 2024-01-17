package pl.szczecin.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import pl.zajavka.infrastructure.nfz.ApiClient;
import pl.zajavka.infrastructure.nfz.api.SownikiApi;
import pl.zajavka.infrastructure.nfz.api.UmowyApi;

@Configuration
public class WebClientConfiguration {

    // Wykorzystujemy adnotację org.springframework.beans.factory.annotation.Value (to nie Lombok!),
    // żeby pobrać wartość z pliku application.yml i przypisać ją do String.
    @Value("${api.nfz.url}")
    private String nfzUrl;


    // Utworzenie Spring beana na podstawie wygenerowanej klasy ApiClient. Klasa ApiClient jest pod
    // spodem zależna od WebClient. Do WebClient przekazujemy ObjectMapper i ustawiamy endpoint, pod
    // którym mamy dostępne API w Internecie.
    @Bean
    public WebClient webClient(final ObjectMapper objectMapper) {

        // Object mapper do zamiany obiektow javowych na jsony i odwrotnie w komunikacji z zewnetrzbym api,
        // ten sam object mapper sluzy do zamiany obiektow w moim api skonfigurowany w BeanConfiguration
        final var exchangesStrategies = ExchangeStrategies
                .builder()
                .codecs(configurer -> {
                    configurer
                            .defaultCodecs()
                            .jackson2JsonEncoder(
                                    new Jackson2JsonEncoder(
                                            objectMapper,
                                            MediaType.APPLICATION_JSON
                                    )
                            );
                    configurer
                            .defaultCodecs()
                            .jackson2JsonDecoder(
                                    new Jackson2JsonDecoder(
                                            objectMapper,
                                            MediaType.APPLICATION_JSON
                                    )
                            );
                })
                .build();

        return WebClient.builder()
                .exchangeStrategies(exchangesStrategies)
                .build();

    }


    // Spinamy WebClienta z ApiClient stworzonym przez generator - klasa z nfz
    @Bean
    public ApiClient apiClient(final WebClient webClient) {
        ApiClient apiClient = new ApiClient(webClient);
        apiClient.setBasePath(nfzUrl);
        return apiClient;
    }


    // Tworzymy Spring bean na podstawie klasy UmowyApi. Klasa UmowyApi zawiera metody, które odpowiadają
    // mapowaniom endpointów z API. Dzięki temu będziemy mogli wstrzyknąć klasę UmowyApi do klasy
    // NfzClientImpl. (takie curle do komunukacji)
    @Bean
    public UmowyApi umowyApi(final ApiClient apiClient) {
        return new UmowyApi(apiClient);
    }

    @Bean
    public SownikiApi sownikiApi(ApiClient apiClient) {
        return new SownikiApi(apiClient);

    }

}
