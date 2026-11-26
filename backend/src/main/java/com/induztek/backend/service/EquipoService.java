package com.induztek.backend.service;

import com.induztek.backend.exception.CodigoDuplicadoException;
import com.induztek.backend.exception.RecursoNoEncontradoException;
import com.induztek.backend.model.Equipo;
import com.induztek.backend.repository.EquipoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipoService {

    private final EquipoRepository equipoRepository;

    public EquipoService(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    public List<Equipo> obtenerTodos() {
        return equipoRepository.findAll();
    }

    public Equipo obtenerPorId(Long id) {
        return equipoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un equipo con id " + id + "."));
    }

    @Transactional
    public Equipo guardar(Equipo equipo) {
        String codigo = equipo.getCodigo().trim();

        if (equipoRepository.existsByCodigoIgnoreCase(codigo)) {
            throw new CodigoDuplicadoException(
                    "Ya existe un equipo con el código " + codigo + ".");
        }

        // Se ignora un id que venga en el JSON: lo genera la base.
        equipo.setId(null);
        equipo.setCodigo(codigo);
        equipo.setTipo(equipo.getTipo().trim());
        return equipoRepository.save(equipo);
    }

    @Transactional
    public Equipo actualizar(Long id, Equipo datos) {
        Equipo existente = obtenerPorId(id);
        String codigo = datos.getCodigo().trim();

        if (equipoRepository.existsByCodigoIgnoreCaseAndIdNot(codigo, id)) {
            throw new CodigoDuplicadoException(
                    "Ya existe otro equipo con el código " + codigo + ".");
        }

        existente.setCodigo(codigo);
        existente.setTipo(datos.getTipo().trim());
        existente.setDescripcion(datos.getDescripcion());
        return equipoRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!equipoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "No existe un equipo con id " + id + ".");
        }
        equipoRepository.deleteById(id);
    }
}
