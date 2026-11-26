package com.induztek.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

// Convierte las excepciones en respuestas JSON simples:
// { "error": "mensaje" }, con el código HTTP que corresponde.
@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(RecursoNoEncontradoException e) {
        return error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CodigoDuplicadoException.class)
    public ResponseEntity<Map<String, String>> duplicado(CodigoDuplicadoException e) {
        return error(HttpStatus.CONFLICT, e.getMessage());
    }

    // Falla de @Valid: junta los mensajes de todos los campos.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validacion(MethodArgumentNotValidException e) {
        String mensaje = e.getBindingResult().getFieldErrors().stream()
                .map(campo -> campo.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(" "));
        return error(HttpStatus.BAD_REQUEST, mensaje);
    }

    // JSON mal formado o vacío.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> jsonInvalido(HttpMessageNotReadableException e) {
        return error(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no es un JSON válido.");
    }

    private ResponseEntity<Map<String, String>> error(HttpStatus estado, String mensaje) {
        return ResponseEntity.status(estado).body(Map.of("error", mensaje));
    }
}
