package cl.duoc.sistemaMarket.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import cl.duoc.sistemaMarket.dto.UsuarioDTO;
import cl.duoc.sistemaMarket.exeptions.RecursoNoEncontradoException;
import cl.duoc.sistemaMarket.model.Usuario;
import cl.duoc.sistemaMarket.repository.UsuarioRepository;
import cl.duoc.sistemaMarket.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario buildUsuario(int id, String nombre) {
        Usuario u = new Usuario();
        u.setId((long) id);
        u.setNombreUsuario(nombre);
        u.setRutUsuario("13.321.986-2");
        u.setCorreoUsuario("usuario@correo.com");
        u.setContactoUsuario("+56912345678");
        return u;
    }

    private UsuarioDTO buildUsuarioDTO(String nombre) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombreUsuarioDto(nombre);
        dto.setRutUsuarioDto("13.321.986-2");
        dto.setCorreoUsuarioDto("usuario@correo.com");
        dto.setContactoUsuarioDto("+56912345678");
        dto.setDireccionUsuarioDto("Calle Falsa 123");
        return dto;
    }

    @Test
    void listarTodos_DeberiaRetornarListaDeDtos() {
        Usuario usuario = buildUsuario(1, "Miles Morales");
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioDTO> resultado = usuarioService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Miles Morales", resultado.get(0).getNombreUsuarioDto());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void listarTodos_RepositorioVacio_DeberiaRetornarListaVacia() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        List<UsuarioDTO> resultado = usuarioService.listarTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void guardarUsuario_Exitoso() {
        UsuarioDTO dto = buildUsuarioDTO("Homero Simpson");
        Usuario guardado = buildUsuario(1, "Homero Simpson");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(guardado);

        UsuarioDTO resultado = usuarioService.guardarUsuario(dto);

        assertNotNull(resultado);
        assertEquals("Homero Simpson", resultado.getNombreUsuarioDto());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void guardarUsuario_NombreNulo_DeberiaLanzarIllegalArgumentException() {
        UsuarioDTO dto = buildUsuarioDTO(null);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.guardarUsuario(dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void guardarUsuario_NombreVacio_DeberiaLanzarIllegalArgumentException() {
        UsuarioDTO dto = buildUsuarioDTO("   ");

        assertThrows(IllegalArgumentException.class, () -> usuarioService.guardarUsuario(dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void guardarUsuario_DtoNulo_DeberiaLanzarIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> usuarioService.guardarUsuario(null));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void guardarUsuario_RutSinGuion_DeberiaLanzarIllegalArgumentException() {
        UsuarioDTO dto = buildUsuarioDTO("Pedro Parker");
        dto.setRutUsuarioDto("133219862");

        assertThrows(IllegalArgumentException.class, () -> usuarioService.guardarUsuario(dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void guardarUsuario_RutNulo_DeberiaLanzarIllegalArgumentException() {
        UsuarioDTO dto = buildUsuarioDTO("Pedro Parker");
        dto.setRutUsuarioDto(null);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.guardarUsuario(dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void guardarUsuario_CorreoSinArroba_DeberiaLanzarIllegalArgumentException() {
        UsuarioDTO dto = buildUsuarioDTO("Pedro Parker");
        dto.setCorreoUsuarioDto("correoSinArroba.com");

        assertThrows(IllegalArgumentException.class, () -> usuarioService.guardarUsuario(dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void guardarUsuario_CorreoNulo_DeberiaLanzarIllegalArgumentException() {
        UsuarioDTO dto = buildUsuarioDTO("Pedro Parker");
        dto.setCorreoUsuarioDto(null);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.guardarUsuario(dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void findById_IdExiste_DeberiaRetornarDTO() {
        Usuario usuario = buildUsuario(1, "Pedro Picapiedra");
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        UsuarioDTO resultado = usuarioService.findById(1);

        assertNotNull(resultado);
        assertEquals("Pedro Picapiedra", resultado.getNombreUsuarioDto());
        verify(usuarioRepository, times(1)).findById(1);
    }

    @Test
    void findById_IdNoExiste_DeberiaLanzarRecursoNoEncontradoException() {
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> usuarioService.findById(999));
        verify(usuarioRepository, times(1)).findById(999);
    }

    @Test
    void eliminarPorId_Exitoso_DeberiaLlamarDeleteById() {
        doNothing().when(usuarioRepository).deleteById(1);

        usuarioService.eliminarPorId(1);

        verify(usuarioRepository, times(1)).deleteById(1);
    }

    @Test
    void eliminarPorId_RepositorioLanzaExcepcion_DeberiaPropagarla() {
        doThrow(new RuntimeException("Error al eliminar"))
                .when(usuarioRepository).deleteById(999);

        assertThrows(RuntimeException.class, () -> usuarioService.eliminarPorId(999));
    }

    @Test
    void actualizarUsuario_Exitoso() {
        int id = 1;
        UsuarioDTO dto = buildUsuarioDTO("Nombre Actualizado");
        Usuario existente = buildUsuario(id, "Nombre Original");
        Usuario guardado = buildUsuario(id, "Nombre Actualizado");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(guardado);

        UsuarioDTO resultado = usuarioService.actualizarUsuario(id, dto);

        assertNotNull(resultado);
        assertEquals("Nombre Actualizado", resultado.getNombreUsuarioDto());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_IdNoExiste_DeberiaLanzarRuntimeException() {
        UsuarioDTO dto = buildUsuarioDTO("Nombre");
        when(usuarioRepository.findById(98)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.actualizarUsuario(98, dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void actualizarUsuario_RutSinGuion_DeberiaLanzarIllegalArgumentException() {
        UsuarioDTO dto = buildUsuarioDTO("Nombre");
        dto.setRutUsuarioDto("133219862");

        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.actualizarUsuario(1, dto));
        verify(usuarioRepository, never()).findById(anyInt());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void actualizarUsuario_RutNulo_DeberiaLanzarIllegalArgumentException() {
        UsuarioDTO dto = buildUsuarioDTO("Nombre");
        dto.setRutUsuarioDto(null);

        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.actualizarUsuario(1, dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void actualizarUsuario_CorreoSinArroba_DeberiaLanzarIllegalArgumentException() {
        UsuarioDTO dto = buildUsuarioDTO("Nombre");
        dto.setCorreoUsuarioDto("sinArrobacorreo.com");

        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.actualizarUsuario(1, dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void actualizarUsuario_CorreoNulo_DeberiaLanzarIllegalArgumentException() {
        UsuarioDTO dto = buildUsuarioDTO("Nombre");
        dto.setCorreoUsuarioDto(null);

        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.actualizarUsuario(1, dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void actualizarUsuario_DeberiaActualizarTodosLosCampos() {
        int id = 1;
        UsuarioDTO dto = buildUsuarioDTO("Nuevo Nombre");
        dto.setContactoUsuarioDto("+56987654321");

        Usuario existente = buildUsuario(id, "Nombre Original");
        Usuario guardado = buildUsuario(id, "Nuevo Nombre");
        guardado.setContactoUsuario("+56987654321");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(guardado);

        UsuarioDTO resultado = usuarioService.actualizarUsuario(id, dto);

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}