package pl.szczecin.infrastructure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity(debug = true) // na potrzeby developmentu mozna wlaczyc logi security. Nigdy na produkcje
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    // Bean okreslajacy w jaki sposob haslo ma byc haszowane
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // manager uwierzytelniania (authentication manager)
    @SuppressWarnings("removal")
    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity httpSecurity,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    // sprawdzenie czy kazdy request spelnia reguly bezpieczenstwa, dodatkowo dodajemy reguly do ktorych endpointow
    // uzytkownik uwierzytelniony moze sie dostac, a do ktorych nie
    @SuppressWarnings("removal")
    @Bean
    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "true", matchIfMissing = true)
    public SecurityFilterChain securityEnabled(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/login", "/registration", "/error", "/images/oh_no.png").permitAll()
                .requestMatchers("/patient/**").hasAnyAuthority("PATIENT")
                .requestMatchers("/doctor/**").hasAnyAuthority("DOCTOR")
                .requestMatchers("/api/**", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**")
                .hasAnyAuthority("REST_API")
                .requestMatchers("/", "/images/**").hasAnyAuthority("DOCTOR", "PATIENT", "REST_API")
//                .requestMatchers(HttpMethod.DELETE).hasAnyAuthority("ADMIN")
                .and()
                // zamiast formLogin jest httpBasic (takie zwykle okienko co wyskakuje do logowania jak wejedziemy w obszar chroniony)
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)  // sesja ma byc wyczyszczona na etapie wylogowania
                .deleteCookies("JSESSIONID")
                .permitAll();

        return httpSecurity.build();
    }

    @SuppressWarnings("removal")
    @Bean
    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "false")
    public SecurityFilterChain securityDisabled(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .anyRequest()
                .permitAll();

        return httpSecurity.build();
    }
}

// TODO csrf wylaczone na potrzeby sprawdzenia komuniakcji z zewnetrznym API
//  oraz rejestracja uzykownika metoda POST zwraca error csrf, po wylaczeniu dodanie usera działa.
//  Nie zaleca sie wylaczania csrf, ze wzgledu na ataki csrf,
//  pozniej w projekcie trzeba to wlaczyc
