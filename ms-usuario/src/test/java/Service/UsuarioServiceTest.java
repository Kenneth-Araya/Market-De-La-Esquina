package Service;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import java.util.Optional;
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

    //test unitario listar todas los usuarios
    @Test
    void listarUsuarios_DeberiaRetornarListaDeDtos(){
        Usuario usuario = new Usuario();

        usuario.setId(1L);
        usuario.setNombreUsuario("Miles Morales");

        when(usuarioRepository.findAllByOrderByIdAsc()).thenReturn(List.of(usuario));

        List<UsuarioDTO>resultado=usuarioService.listarTodos();

        assertNotNull(resultado,"la lista no deberia ir null");
        assertEquals(1, resultado.size(),"la lista deberia tener minimo un usuario");
        assertEquals("Miles Morales", resultado.get(0).getNombreUsuarioDto(),"El nombre no coincide");

        verify(usuarioRepository, times(1)).findAllByOrderByIdAsc();
    }

    //test unitario guardar usuarios
    @Test
    void guardarUsuario_IllegalArgumentException(){
        UsuarioDTO usuariodto = new UsuarioDTO();
        usuariodto.setNombreUsuarioDto("Miles Morales");

        when(usuarioRepository.existsByNombreUsuario("Miles Morales")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,()->{
            usuarioService.guardarUsuario(usuariodto);
        },"IllegalArgumentException cuando la descripcion sea existente");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    //test unitario guardar usuarios con condicion
    @Test
    void guardarUsuarios_CuandoRutNoExista(){
        UsuarioDTO usuariodto = new UsuarioDTO();
        usuariodto.setNombreUsuarioDto("Homero simpson");
        usuariodto.setRutUsuarioDto("13.321.986-2");
    
        when(usuarioRepository.existsByNombreUsuario("Homero simpson")).thenReturn(false);

        Usuario usuario = new Usuario();
        usuariodto.setNombreUsuarioDto("Homero simpson");
        usuariodto.setRutUsuarioDto("13.321.986-2");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        
        UsuarioDTO resultado = usuarioService.guardarUsuario(usuariodto);

        assertNotNull(resultado,"El usuario guardado no deberia ser null");
        assertEquals("13.321.986-2", resultado.getCorreoUsuarioDto(),"El RUT deberia ser XX.XXX.XXX-X");

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    //test unitario buscar usuarios por id 
    @Test
    void buscarUsuario_CuandoIdExiste(){

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreUsuario("Pedro picapiedra");
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        UsuarioDTO resultado = usuarioService.findById(1);

        //then
        assertNotNull(resultado,"El nombre no deberia ser null");
        assertEquals("Pedro picapiedra", resultado.getNombreUsuarioDto(),"El nombre debe coincidir");

        verify(usuarioRepository, times(1)).findById(1L);
    }

    //test unitario eliminar usuarios con condicion
    @Test
    void eliminarUsuarioNoExistente(){
        when(usuarioRepository.existsByNombreUsuario("Peter Parker")).thenReturn(false);
        
        //When y then 
        assertThrows(IllegalArgumentException.class, ()->{
            usuarioService.eliminarPorId(98);
        },"IllegalArgumentException si el id no existe");

        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    //test unitario eliminar por id 
    @Test
    void eliminarUsuario_CuandoIdExiste(){
        when(usuarioRepository.existsById(1)).thenReturn(true);

        //when
        usuarioService.eliminarPorId(1);

        //then
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    //test unitario actualizar por id 
    @Test
    void actualizarUsuario_CuandoIdNoExista(){
        int idInexistente = 98;
        UsuarioDTO usuariodto = new UsuarioDTO();
        usuariodto.setNombreUsuarioDto("El pepe");

        when(usuarioRepository.findById(idInexistente)).thenReturn(Optional.empty());

        //when y then
        assertThrows(RecursoNoEncontradoException.class, ()->{
            usuarioService.actualizarUsuario(idInexistente, usuariodto);
        });
    }

    //test unitario actualizar por id 
    @Test
    void actualizarUsuario_CuandoIdExiste() {
    int id = 1;
    Usuario usuarioExistente = new Usuario();
    usuarioExistente.setId((long) id);
    usuarioExistente.setNombreUsuario("Nombre Original");

    UsuarioDTO dto = new UsuarioDTO();
    dto.setNombreUsuarioDto("Descripcion ocupada");

    when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
    when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);

    assertThrows(IllegalArgumentException.class,()->{
        usuarioService.actualizarUsuario(id, dto);
    },"IllegalArgumentException si la nueva descripcion ya es existente");
}

    //test unitario actualizar por id 
    @Test
    void actualizarUsuario_DeberiaRetornarUsuarioActualizado(){
        int id = 1;
        Usuario usuario = new Usuario();
        usuario.setId((long) id);
        usuario.setNombreUsuario("Nombre Original");

        UsuarioDTO usuariodto = new UsuarioDTO();
        usuariodto.setNombreUsuarioDto("Maggie simpson");
        usuariodto.setRutUsuarioDto("11.431.921-7");
        
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByNombreUsuario("Nombre Nuevo")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        //when
        UsuarioDTO resultado = usuarioService.actualizarUsuario(id, usuariodto);

        //then
        assertNotNull(resultado,"El resultado no deberia ser null");
        assertEquals("Nombre Nuevo", resultado.getNombreUsuarioDto(),"El nombre deberia actualizarse");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}