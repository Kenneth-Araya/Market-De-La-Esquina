package cl.duoc.SistemaMarket.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.SistemaMarket.dto.ProductoDTO;
import cl.duoc.SistemaMarket.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api/v1/productos")
@RequiredArgsConstructor

public class ProductoController {

    private final ProductoService service;

    //Listar productos
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        List<ProductoDTO> productos = service.listarProductos();
        return ResponseEntity
        .ok(productos);
    }
   
    //Crear producto
    @PostMapping
    public ResponseEntity<ProductoDTO> guardarProducto(@Valid @RequestBody ProductoDTO productoDto) {
        ProductoDTO creado = service.guardarProducto(productoDto);
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(creado);
    }

    //Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id){
        service.eliminarProducto(id);
        return ResponseEntity
        .noContent()
        .build();
    }

    //Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable Long id,@Valid @RequestBody ProductoDTO dto) {
        ProductoDTO actualizado=service.actualizarProducto(id, dto);
        return ResponseEntity
        .ok(actualizado);
    }

    //Buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> listarProductoPorId(@PathVariable Long id) {
        ProductoDTO producto=service.buscarProducto(id);
        return ResponseEntity
        .ok(producto);
    }
}
