package controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import cl.duoc.sistemaMarket.controller.UsuarioController;
import cl.duoc.sistemaMarket.dto.UsuarioDTO;
import cl.duoc.sistemaMarket.service.UsuarioService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void listarUsuarios_DeberiaRetornar200() throws Exception{
        //given
        UsuarioDTO usuariodto = new UsuarioDTO();
        usuariodto.setNombreUsuarioDto("Luis Fernandez");

        UsuarioDTO usuariodtos = new UsuarioDTO();
        usuariodtos.setNombreUsuarioDto("Diego Castro");

        List<UsuarioDTO> listaSimulada = Arrays.asList(usuariodto, usuariodtos);

        when(usuarioService.listarTodos()).thenReturn(listaSimulada);

        //when y then
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Luis Fernandez"))
                .andExpect(jsonPath("$[1].nombre").value("Diego Castro"));  
    }

    @Test
    void guardarUsuarios_201() throws Exception{
        //given
        UsuarioDTO nuevoUsuario = new UsuarioDTO();
        nuevoUsuario.setNombreUsuarioDto("Matías Ignacio Rojas Pizarro");
        nuevoUsuario.setContactoUsuarioDto("+56 9 1234 5678");
        nuevoUsuario.setRutUsuarioDto("21.987.654-3");
        nuevoUsuario.setFechaUsuarioDto(LocalDate.of(2025, 6, 16));
        nuevoUsuario.setDireccionUsuarioDto("Pasaje Los Aromos 567, Depto 302, Providencia");
        nuevoUsuario.setCorreoUsuarioDto("matias.rojas@hotmail.com");
        when(usuarioService.guardarUsuario(any(UsuarioDTO.class))).thenReturn(nuevoUsuario);

        //when y then
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Ignacio Pizarro"));

    }

    @Test
    void eliminarUsuario_204() throws Exception {
        //given
        int id = 1;
        doNothing().when(usuarioService).eliminarPorId(99);

        //when y then
        mockMvc.perform(delete("/api/v1/usuarios/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void actualizarUsuario_200() throws Exception{
        //given
        int id=1;

        UsuarioDTO dtoActualizado = new UsuarioDTO();
        dtoActualizado.setNombreUsuarioDto("Matías Ignacio Rojas Pizarro");
        dtoActualizado.setContactoUsuarioDto("+56 9 1234 5678");
        dtoActualizado.setRutUsuarioDto("21.987.654-3");
        dtoActualizado.setFechaUsuarioDto(LocalDate.of(2025, 6, 16));
        dtoActualizado.setDireccionUsuarioDto("Pasaje Los Aromos 567, Depto 302, Providencia");
        dtoActualizado.setCorreoUsuarioDto("matias.rojas@hotmail.com");

        when(usuarioService.actualizarUsuario(eq(id), any(UsuarioDTO.class))).thenReturn(dtoActualizado);

        //when Y then
        mockMvc.perform(put("/api/v1/ventas/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Carbohidratos"));
    }

    @Test
    void listarVentaPorId_200() throws Exception{
        Long id=1L;
        UsuarioDTO usuario = new UsuarioDTO();

        usuario.setNombreUsuarioDto("Sebastián Andrés Fuentes Díaz");
        usuario.setContactoUsuarioDto("+56 9 1234 5678");
        usuario.setRutUsuarioDto("21.987.654-3");
        usuario.setFechaUsuarioDto(LocalDate.of(1998, 4, 12));
        usuario.setDireccionUsuarioDto("Pasaje Los Aromos 567, Depto 302, Providencia");
        usuario.setCorreoUsuarioDto("matias.rojas@hotmail.com");

        when(usuarioService.guardarUsuario(any(UsuarioDTO.class))).thenReturn(usuario);

        //when t then
        mockMvc.perform(get("/api/v1/usuario/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Andres Max de la cruz"))
                .andExpect(jsonPath("$.ContactoUsuarioDto").value("21.987.654-3"));
    }
}