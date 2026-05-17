package cl.duoc.ms_inventario.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.ms_inventario.dto.InventarioDTO;
import cl.duoc.ms_inventario.service.InventarioService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("api/v1/inventario")
@RequiredArgsConstructor
public class InventarioController {
    private final InventarioService service;

    // 1. Consultar estado y stock (Para ver si es apto para venta)
    @GetMapping("/{id}")
    public ResponseEntity<InventarioDTO> consultarStock(@PathVariable Long id) {
        return ResponseEntity.ok(service.validarObteniendoStock(id));
    }

    // 2. Descontar stock 
    @PostMapping("/{id}")
    public ResponseEntity<String> descontarStock(@PathVariable Long id, @RequestParam int cantidad) {
        service.descontarStock(id, cantidad);
        return ResponseEntity.ok("Stock actualizado: se descontaron"+ cantidad + " unidades ");
    }

    // 4. Reporte de stock bajo (Ejemplo: limite = 5)
    @GetMapping("/bajo/{limite}")
    public ResponseEntity<List<InventarioDTO>> listarBajoStock(@PathVariable int limite) {
        return ResponseEntity.ok(service.listarProductosBajoStock(limite));
    }
    
    

    
}
