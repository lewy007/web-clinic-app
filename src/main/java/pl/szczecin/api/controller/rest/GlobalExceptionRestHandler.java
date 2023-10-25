package pl.szczecin.api.controller.rest;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.szczecin.api.dto.ExceptionMessage;
import pl.szczecin.domain.exception.NotFoundException;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestControllerAdvice(annotations = RestController.class) // dwie obslugi bledow dzialaja jednoczesnie w projekcie
@Order(Ordered.HIGHEST_PRECEDENCE) // nasza obsluga bledow jest priorytetowa, najwazniejszy jest nasz Advice
public class GlobalExceptionRestHandler extends ResponseEntityExceptionHandler {

    private static final Map<Class<?>, HttpStatus> EXCEPTION_STATUS = Map.of(
            ConstraintViolation.class, HttpStatus.BAD_REQUEST,
            DataIntegrityViolationException.class, HttpStatus.BAD_REQUEST,
            EntityNotFoundException.class, HttpStatus.NOT_FOUND,
            NotFoundException.class, HttpStatus.NOT_FOUND
    );

    // wcinamy sie w srodek obslugiwania bledow przez springa i dopisujemy swoja logike, ale jej nie nadpisujemy,
    // czyli nastepuje przekierowanie ze springa do naszej metody, zostaje uruchomiona nasza ligika i z powrorem przekazujmey
    // obsluge do frameworka
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex,
            @Nullable Object body,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request) {

        // nadano uniklany identyfikator dla bledu, ktory przekazano do logow i do body
        final String errorId = UUID.randomUUID().toString();
        log.error("Exception: ID={}, HttpStatus={}", errorId, statusCode, ex);

        return super.handleExceptionInternal(
                ex,
                ExceptionMessage.of(errorId), // nasze body, ktore zwraca tylko errorId (nadpisuje oryginal)
                headers,
                statusCode,
                request);
    }


    // kazdy wyjatek, ktory nie zostanie nigdzie obsluzony ma trafic do tej metody i zostac obsluzony
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception exception) {
        return doHandle(exception, getHttpStatusFromException(exception.getClass()));
    }

    // do logow idzie caly zapis exception, natomiast do klienta jest zwracany tylko errorId, po ktorym moze
    // zglosic do developera identyfikator i my sprawdzamy pozniej w logach co sie stalo
    private ResponseEntity<?> doHandle(Exception exception, HttpStatus status) {
        final String errorId = UUID.randomUUID().toString();
        log.error("Exception: ID={}, HttpStatus={}", errorId, status, exception);

        // zwracamy status, typ oraz body w postaci identyfikatora bledu bez exception (klient tego nie potrzebuje)
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ExceptionMessage.of(errorId));
    }

    private HttpStatus getHttpStatusFromException(final Class<?> exceptionClass) {

        // zwracamy klucz z mapy z przypisana wartoscia statusu, a jesli go nie ma to zwracana jest 500ka
        return EXCEPTION_STATUS.getOrDefault(
                exceptionClass,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
