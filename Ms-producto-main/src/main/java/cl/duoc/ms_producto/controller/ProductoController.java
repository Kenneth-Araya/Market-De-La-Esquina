package cl.duoc.ms_producto.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cl.duoc.ms_producto.dto.ProductoDto;
import cl.duoc.ms_producto.service.ProductoService;
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

    //LISTAR TODOS LOS PRODUCTOS 
    @GetMapping("")
    public ResponseEntity<List<ProductoDto>> listarProductos() {
        List<ProductoDto> productos=service.listarProductos();
        return ResponseEntity.ok(productos);
    }

    //BUSCAR PRODUCTO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> listarProductoPorId(@PathVariable Long id) {
        ProductoDto producto=service.buscarProducto(id);
        return ResponseEntity
               .ok(producto);
    }
    
    
    //CREAR UN NUEVO PRODUCTO
    @PostMapping("")
    public ResponseEntity<ProductoDto> guardarProducto(@Valid @RequestBody ProductoDto productoDto) {
        ProductoDto creado=service.guardarProducto(productoDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creado);
    }

    //ELIMINAR UN PRODUCTO POR ID 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id){
        service.eliminarProducto(id);
        return ResponseEntity
               .noContent()
               .build();
    }


    //ACTUALIZAR UN PRODUCTO EXISTENTE 
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDto> actualizarProducto(@PathVariable Long id,@Valid @RequestBody ProductoDto dto) {
        ProductoDto actualizado=service.actualizarProducto(id, dto);
        return ResponseEntity
               .ok(actualizado);
    }


}
