package cl.duoc.sistemaMarket.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cl.duoc.sistemaMarket.dto.UsuarioDTO;
import cl.duoc.sistemaMarket.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    //Listar usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos(){
        List<UsuarioDTO> usuarioDTOs = usuarioService.listarTodos();
        return ResponseEntity
        .ok(usuarioDTOs);
    }

    //Crear usuarios
    @PostMapping
    public ResponseEntity<UsuarioDTO> guardarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO){
        UsuarioDTO creado = usuarioService.guardarUsuario(usuarioDTO);
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(creado);
    }

    //Eliminar usuarios
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable int id){
        usuarioService.eliminarPorId(id);
        return ResponseEntity
        .noContent()
        .build();
    }

    //Actualizar usuarios
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable int id,@Valid @RequestBody UsuarioDTO dto){
        UsuarioDTO actualizado = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity
        .ok(actualizado);
    }

    //Buscar usuarios por id
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable int id){
        return ResponseEntity
        .ok(usuarioService.findById(id));
    }
}
