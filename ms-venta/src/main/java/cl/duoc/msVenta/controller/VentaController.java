package cl.duoc.msVenta.controller;

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

import cl.duoc.msVenta.dto.BoletaVentaDTO;
import cl.duoc.msVenta.dto.VentaDTO;
import cl.duoc.msVenta.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    // Listar ventas
    @GetMapping
    public ResponseEntity<List<VentaDTO>> listarTodos() {
        List<VentaDTO> ventaDTOs = ventaService.listarTodos();
        return ResponseEntity
                .ok(ventaDTOs);
    }

    // Guardar Venta
    @PostMapping
    public ResponseEntity<VentaDTO> guardarVenta(@Valid @RequestBody VentaDTO ventaDTO) {
        VentaDTO creado = ventaService.guardarVenta(ventaDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creado);
    }

    // Eliminar venta
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarVenta(@PathVariable Long id) {
        ventaService.eliminarPorId(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    // Actualizar venta
    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> actualizarVenta(@PathVariable Long id, @Valid @RequestBody VentaDTO dto) {
        VentaDTO actualizado = ventaService.actualizarVenta(id, dto);
        return ResponseEntity
                .ok(actualizado);
    }

    // buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> findById(@PathVariable Long id) {
        return ResponseEntity
                .ok(ventaService.findById(id));
    }

    //====================================feign====================================

    //listar boletas
    @GetMapping("/boletas")
    public ResponseEntity<List<BoletaVentaDTO>> listarBoletas() {
        return ResponseEntity.ok(ventaService.listarBoletas());
    }

    //buscar una boleta por folio
    @GetMapping("/boletas/{folio}")
    public ResponseEntity<BoletaVentaDTO> obtenerBoleta(@PathVariable String folio) {
        return ResponseEntity.ok(ventaService.obtenerBoletaPorFolio(folio));
    }

    //Crear una boleta
    @PostMapping("/boletas")
    public ResponseEntity<BoletaVentaDTO> crearBoleta(@RequestBody BoletaVentaDTO boletaDTO, @PathVariable String folio) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ventaService.crearBoletaParaVenta(boletaDTO));
    }

    //Actualiza boleta (marcar PAGADA)
    @PutMapping("/boletas/{folio}")
    public ResponseEntity<BoletaVentaDTO> actualizarBoleta(@PathVariable String folio,
            @RequestBody BoletaVentaDTO boletaDTO) {
        return ResponseEntity.ok(ventaService.actualizarEstadoBoleta(folio, boletaDTO));
    }

    //Elimina boleta (venta anulada)
    @DeleteMapping("/boletas/{folio}")
    public ResponseEntity<Void> eliminarBoleta(@PathVariable String folio) {
        ventaService.eliminarBoletaDeVenta(folio);
        return ResponseEntity.noContent().build();
    }
}
