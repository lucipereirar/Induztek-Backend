package com.induztek.backend.controller;

import com.induztek.backend.model.Equipo;
import com.induztek.backend.service.EquipoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipos")
@CrossOrigin(origins = "*")
public class EquipoController {

    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    // GET /api/equipos -> 200 con la lista
    @GetMapping
    public List<Equipo> obtenerTodos() {
        return equipoService.obtenerTodos();
    }

    // GET /api/equipos/{id} -> 200, o 404 si no existe
    @GetMapping("/{id}")
    public Equipo obtenerPorId(@PathVariable Long id) {
        return equipoService.obtenerPorId(id);
    }

    // POST /api/equipos -> 201 con el equipo creado, 400 si faltan datos,
    // 409 si el código ya existe
    @PostMapping
    public ResponseEntity<Equipo> guardar(@Valid @RequestBody Equipo equipo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(equipoService.guardar(equipo));
    }

    // PUT /api/equipos/{id} -> 200 con el equipo editado, 400, 404 o 409
    @PutMapping("/{id}")
    public Equipo actualizar(@PathVariable Long id, @Valid @RequestBody Equipo equipo) {
        return equipoService.actualizar(id, equipo);
    }

    // DELETE /api/equipos/{id} -> 204 sin cuerpo, o 404 si no existe
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        equipoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
