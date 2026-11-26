package com.induztek.backend.exception;

// Se lanza cuando ya existe un equipo con el mismo código. Responde 409.
public class CodigoDuplicadoException extends RuntimeException {

    public CodigoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
