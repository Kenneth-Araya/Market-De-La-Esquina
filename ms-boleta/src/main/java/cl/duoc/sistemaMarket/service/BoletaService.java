package cl.duoc.sistemaMarket.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import cl.duoc.sistemaMarket.dto.BoletaDTO;
import cl.duoc.sistemaMarket.dto.BoletaDTOMapper;
import cl.duoc.sistemaMarket.exeptions.RecursoNoEncontradoException;
import cl.duoc.sistemaMarket.model.Boleta;
import cl.duoc.sistemaMarket.repository.BoletaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoletaService {
    
    private final BoletaRepository boletaRepository;
    private final BoletaDTOMapper boletaDTOMapper;

    //Listar todas las boletas
    public List<BoletaDTO> listarTodos(){
        List<Boleta> boletas = boletaRepository.findAll();
        List<BoletaDTO> boletaDTOs = new ArrayList<>();

        if (!boletas.isEmpty()) {
            for (Boleta boleta : boletas) {
                boletaDTOs.add(boletaDTOMapper.toDTO(boleta));
            }
        }
        return boletaDTOs;
    }

    //Listar obtener las boletas por folio
    public BoletaDTO obtenerBoletaPorFolio(String folio){

        Boleta boleta = boletaRepository.findByFolioBoleta(folio);
        if (boleta == null || !boleta.getFolioBoleta().equals(folio)) {
            throw new RecursoNoEncontradoException("Numero de folio incorrecto");
        }
        return boletaDTOMapper.toDTO(boleta);
    }

    //guardar boleta 
    public boolean guardarBoleta(BoletaDTO boletaDTO){

        //Regla de negocio
        if (boletaDTO == null || boletaDTO.getFolio() == null || boletaDTO.getFolio().isEmpty()) {
            throw new IllegalArgumentException("La boleta o su número de folio no pueden ser nulos o vacíos");
        }

        //Regla de negocio
        if (boletaDTO == null || boletaDTO.getFolio() == null || boletaDTO.getFolio().isEmpty()) {
            throw new IllegalArgumentException("No se puede generar una boleta sin productos");
        }

        //Regla de negocio
        if (boletaDTO.getFecha() != null && boletaDTO.getFecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de emision no puede ser una fecha futura");
        }

        Boleta boleta = boletaDTOMapper.toModel(boletaDTO);
        return boletaRepository.save(boleta) != null;
    }

    //Actualizar boleta 
    public boolean actualizarBoleta(String folio, BoletaDTO boletaDTO) {

        //solo existe el metodo save para poder actualizar
        Boleta boletaExistente = boletaRepository.findByFolioBoleta(folio);
        if (boletaExistente == null || !boletaExistente.getFolioBoleta().equals(folio)) {
            throw new RecursoNoEncontradoException("Número de folio incorrecto.");
        }

        // Regla de negocio
        if (boletaDTO.getProductos() == null || boletaDTO.getProductos().isEmpty()) {
            throw new  IllegalStateException("No se puede actualizar boleta sin productos");
        }

        // Regla de negocio
        if (boletaDTO.getFecha() != null && boletaDTO.getFecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de emision no puede ser una fecha futura");
        }

        Boleta boletaActualizada = boletaDTOMapper.toModel(boletaDTO);
        boletaActualizada.setId(boletaExistente.getId());
        return boletaRepository.save(boletaActualizada) != null;

    }

    //Eliminar boleta
    public boolean eliminarBoleta(String folio) {

        Boleta boletaExistente = boletaRepository.findByFolioBoleta(folio);

        if (boletaExistente == null || !boletaExistente.getFolioBoleta().equals(folio)) {
            throw new RecursoNoEncontradoException("Número de folio incorrecto.");
        }

        boletaRepository.deleteByFolioBoleta(folio);
        return true;
    }
}
