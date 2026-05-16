package cl.duoc.sistemaMarket.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.duoc.sistemaMarket.dto.UsuarioDTO;
import cl.duoc.sistemaMarket.dto.UsuarioDTOMapper;
import cl.duoc.sistemaMarket.model.Usuario;
import cl.duoc.sistemaMarket.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    //Listar usuarios
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll()
        .stream()
        .map(UsuarioDTOMapper::toDto)
        .toList();
    }

    //Guardar usuarios
    public UsuarioDTO guardarUsuario(UsuarioDTO dto) {

        Usuario usuario = UsuarioDTOMapper.toEntity(dto);
        Usuario guardado = usuarioRepository.save(usuario);
        return UsuarioDTOMapper.toDto(guardado);
    }

    //Eliminar usuarios
    public void eliminarPorId(int id) {
        usuarioRepository.deleteById(id);
    }

    //Buscar usuarios por id 
    public UsuarioDTO findById(int id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return UsuarioDTOMapper.toDto(usuario);
    }

    //Actualizar usuarios
    public UsuarioDTO actualizarUsuario(int id, UsuarioDTO dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombreUsuario(dto.getNombreUsuarioDto());
        usuario.setRutUsuario(dto.getRutUsuarioDto());
        usuario.setContactoUsuario(dto.getContactoUsuarioDto());
        usuario.setCorreoUsuario(dto.getCorreoUsuarioDto());

        Usuario actualizado = usuarioRepository.save(usuario);
        return UsuarioDTOMapper.toDto(actualizado);
    }
}
