package cl.duoc.sistemaMarket.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cl.duoc.sistemaMarket.dto.UsuarioDTO;
import cl.duoc.sistemaMarket.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private UsuarioDTO buildUsuarioDTO(String nombre) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombreUsuarioDto(nombre);
        dto.setContactoUsuarioDto("+56912345678");
        dto.setRutUsuarioDto("21.987.654-3");
        dto.setFechaUsuarioDto(LocalDate.of(1998, 4, 12));
        dto.setDireccionUsuarioDto("Pasaje Los Aromos 567, Depto 302, Providencia");
        dto.setCorreoUsuarioDto("usuario@hotmail.com");
        return dto;
    }

    @Test
    void listarUsuarios_DeberiaRetornar200() throws Exception {
        UsuarioDTO u1 = buildUsuarioDTO("Luis Fernandez");
        UsuarioDTO u2 = buildUsuarioDTO("Diego Castro");
        when(usuarioService.listarTodos()).thenReturn(Arrays.asList(u1, u2));
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nombreUsuarioDto").value("Luis Fernandez"))
                .andExpect(jsonPath("$[1].nombreUsuarioDto").value("Diego Castro"));
    }

    @Test
    void listarUsuarios_ListaVacia_DeberiaRetornar200() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void listarUsuarios_ErrorServicio_DeberiaRetornar500() throws Exception {
        when(usuarioService.listarTodos())
                .thenThrow(new RuntimeException("Error de base de datos"));
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void guardarUsuario_DeberiaRetornar201() throws Exception {
        UsuarioDTO nuevo = buildUsuarioDTO("Matías Rojas");
        when(usuarioService.guardarUsuario(any(UsuarioDTO.class))).thenReturn(nuevo);
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreUsuarioDto").value("Matías Rojas"));
    }

    @Test
    void guardarUsuario_ErrorServicio_DeberiaRetornar500() throws Exception {
        UsuarioDTO dto = buildUsuarioDTO("Matías Rojas");
        when(usuarioService.guardarUsuario(any(UsuarioDTO.class)))
                .thenThrow(new RuntimeException("Error al guardar"));
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void guardarUsuario_DeberiaRetornarTodosLosCampos() throws Exception {
        UsuarioDTO dto = buildUsuarioDTO("Ana González");
        when(usuarioService.guardarUsuario(any(UsuarioDTO.class))).thenReturn(dto);
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreUsuarioDto").value("Ana González"))
                .andExpect(jsonPath("$.contactoUsuarioDto").value("+56912345678"))
                .andExpect(jsonPath("$.rutUsuarioDto").value("21.987.654-3"))
                .andExpect(jsonPath("$.correoUsuarioDto").value("usuario@hotmail.com"));
    }

    @Test
    void eliminarUsuario_DeberiaRetornar204() throws Exception {
        doNothing().when(usuarioService).eliminarPorId(1);
        mockMvc.perform(delete("/api/v1/usuarios/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarUsuario_ErrorServicio_DeberiaRetornar500() throws Exception {
        doThrow(new RuntimeException("Error al eliminar"))
                .when(usuarioService).eliminarPorId(999);
        mockMvc.perform(delete("/api/v1/usuarios/{id}", 999))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void actualizarUsuario_DeberiaRetornar200() throws Exception {
        int id = 1;
        UsuarioDTO actualizado = buildUsuarioDTO("Matías Rojas Actualizado");
        when(usuarioService.actualizarUsuario(eq(id), any(UsuarioDTO.class))).thenReturn(actualizado);
        mockMvc.perform(put("/api/v1/usuarios/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreUsuarioDto").value("Matías Rojas Actualizado"));
    }

    @Test
    void actualizarUsuario_ErrorServicio_DeberiaRetornar500() throws Exception {
        int id = 999;
        UsuarioDTO dto = buildUsuarioDTO("X");
        when(usuarioService.actualizarUsuario(eq(id), any(UsuarioDTO.class)))
                .thenThrow(new RuntimeException("Usuario no encontrado"));
        mockMvc.perform(put("/api/v1/usuarios/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findById_DeberiaRetornar200() throws Exception {
        int id = 1;
        UsuarioDTO usuario = buildUsuarioDTO("Sebastián Fuentes");
        when(usuarioService.findById(id)).thenReturn(usuario);
        mockMvc.perform(get("/api/v1/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreUsuarioDto").value("Sebastián Fuentes"))
                .andExpect(jsonPath("$.rutUsuarioDto").value("21.987.654-3"));
    }

    @Test
    void findById_ErrorServicio_DeberiaRetornar500() throws Exception {
        int id = 999;
        when(usuarioService.findById(id))
                .thenThrow(new RuntimeException("Usuario no encontrado"));
        mockMvc.perform(get("/api/v1/usuarios/{id}", id))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findById_DeberiaRetornarTodosLosCampos() throws Exception {
        int id = 1;
        UsuarioDTO usuario = buildUsuarioDTO("Carlos López");
        when(usuarioService.findById(id)).thenReturn(usuario);

        mockMvc.perform(get("/api/v1/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreUsuarioDto").value("Carlos López"))
                .andExpect(jsonPath("$.contactoUsuarioDto").value("+56912345678"))
                .andExpect(jsonPath("$.correoUsuarioDto").value("usuario@hotmail.com"))
                .andExpect(jsonPath("$.direccionUsuarioDto").value("Pasaje Los Aromos 567, Depto 302, Providencia"));
    }
}