package com.induztek.backend.controller;

import com.induztek.backend.model.Equipo;
import com.induztek.backend.repository.EquipoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Verifica el CRUD de equipos de punta a punta (controlador, servicio,
// repositorio y base H2 en memoria).
@SpringBootTest
@AutoConfigureMockMvc
class EquipoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EquipoRepository repositorio;

    private Equipo existente;

    @BeforeEach
    void preparar() {
        repositorio.deleteAll();
        existente = repositorio.save(
                new Equipo("TR-001", "TRANSFORMADOR", "Subestación norte"));
    }

    @Test
    void listaLosEquipos() throws Exception {
        mvc.perform(get("/api/equipos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].codigo").value("TR-001"));
    }

    @Test
    void obtienePorId() throws Exception {
        mvc.perform(get("/api/equipos/" + existente.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("TRANSFORMADOR"));
    }

    @Test
    void idInexistenteResponde404() throws Exception {
        mvc.perform(get("/api/equipos/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No existe un equipo con id 9999."));
    }

    @Test
    void creaUnEquipo() throws Exception {
        mvc.perform(post("/api/equipos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\" RP-010 \",\"tipo\":\"RELE_PROTECCION\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.codigo").value("RP-010"));

        assertEquals(2, repositorio.count());
    }

    @Test
    void crearSinCodigoResponde400() throws Exception {
        mvc.perform(post("/api/equipos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"\",\"tipo\":\"CABLE\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El código del equipo es obligatorio."));
    }

    @Test
    void jsonInvalidoResponde400() throws Exception {
        mvc.perform(post("/api/equipos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{esto no es json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void codigoRepetidoResponde409() throws Exception {
        mvc.perform(post("/api/equipos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"tr-001\",\"tipo\":\"CABLE\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void editaUnEquipo() throws Exception {
        mvc.perform(put("/api/equipos/" + existente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"TR-001B\",\"tipo\":\"TRANSFORMADOR\",\"descripcion\":\"Cambiado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("TR-001B"));

        Equipo guardado = repositorio.findById(existente.getId()).orElseThrow();
        assertEquals("TR-001B", guardado.getCodigo());
        assertEquals("Cambiado", guardado.getDescripcion());
    }

    @Test
    void editarConservandoSuPropioCodigoFunciona() throws Exception {
        mvc.perform(put("/api/equipos/" + existente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"TR-001\",\"tipo\":\"CELDA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("CELDA"));
    }

    @Test
    void editarConCodigoDeOtroResponde409() throws Exception {
        repositorio.save(new Equipo("CB-002", "CABLE", null));

        mvc.perform(put("/api/equipos/" + existente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"CB-002\",\"tipo\":\"TRANSFORMADOR\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void editarInexistenteResponde404() throws Exception {
        mvc.perform(put("/api/equipos/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"X-1\",\"tipo\":\"CABLE\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminaUnEquipo() throws Exception {
        mvc.perform(delete("/api/equipos/" + existente.getId()))
                .andExpect(status().isNoContent());

        assertFalse(repositorio.existsById(existente.getId()));
    }

    @Test
    void eliminarInexistenteResponde404() throws Exception {
        mvc.perform(delete("/api/equipos/9999"))
                .andExpect(status().isNotFound());
    }
}
