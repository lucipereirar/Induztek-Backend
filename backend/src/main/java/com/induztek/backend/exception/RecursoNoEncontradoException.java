package com.induztek.backend.exception;

// Se lanza cuando se pide un registro que no existe. Responde 404.
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
