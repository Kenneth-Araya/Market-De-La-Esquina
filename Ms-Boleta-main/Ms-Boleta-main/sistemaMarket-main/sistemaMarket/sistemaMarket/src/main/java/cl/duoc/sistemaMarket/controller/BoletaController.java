package cl.duoc.sistemaMarket.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cl.duoc.sistemaMarket.dto.BoletaDTO;
import cl.duoc.sistemaMarket.service.BoletaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/boletas")
@RequiredArgsConstructor
public class BoletaController {

    private final BoletaService boletaService;

    //Listar todos
    @GetMapping()
    public ResponseEntity<List<BoletaDTO>> listarTodos() {
        List<BoletaDTO> boletaDTOs = boletaService.listarTodos();
        return ResponseEntity
        .ok(boletaDTOs);
    }

    //Crear boleta
    @PostMapping()
    public ResponseEntity<BoletaDTO> guardarBoleta(@Valid @RequestBody BoletaDTO boletaDTO) {
        boletaService.guardarBoleta(boletaDTO);
        return ResponseEntity
        .ok(boletaDTO);
    }

    //Eliminar boleta
    @DeleteMapping("/{folio}")
    public ResponseEntity<Void> eliminarBoleta (@PathVariable String folio) {
        boletaService.eliminarBoleta(folio);
        return ResponseEntity
        .noContent()
        .build();
    }
    
    //Actualizar boleta
    @PutMapping("/{folio}")
    public ResponseEntity<BoletaDTO> actualizaBoleta (@PathVariable String folio, @Valid @RequestBody BoletaDTO boletaDTO) {
        boletaService.actualizarBoleta(folio, boletaDTO);
        return ResponseEntity
        .ok(boletaDTO);
    }

    //Obtener boleta por folio
    @GetMapping("/{folio}")
    public ResponseEntity<BoletaDTO> obtenerBoletaPorFolio(@PathVariable String folio) {
        return ResponseEntity
        .ok(boletaService.obtenerBoletaPorFolio(folio));
    }
}

