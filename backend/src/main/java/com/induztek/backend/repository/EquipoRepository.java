package com.induztek.backend.repository;

import com.induztek.backend.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    boolean existsByCodigoIgnoreCase(String codigo);

    // Para editar: ¿hay OTRO equipo con este código?
    boolean existsByCodigoIgnoreCaseAndIdNot(String codigo, Long id);
}
