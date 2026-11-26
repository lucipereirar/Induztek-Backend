package com.induztek.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código interno del equipo (por ejemplo "TR-001"). Es único,
    // igual que en la app.
    @NotBlank(message = "El código del equipo es obligatorio.")
    @Size(max = 50, message = "El código no puede superar 50 caracteres.")
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @NotBlank(message = "El tipo de equipo es obligatorio.")
    @Column(nullable = false)
    private String tipo;

    @Size(max = 255, message = "La descripción no puede superar 255 caracteres.")
    private String descripcion;

    public Equipo() {
    }

    public Equipo(String codigo, String tipo, String descripcion) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
