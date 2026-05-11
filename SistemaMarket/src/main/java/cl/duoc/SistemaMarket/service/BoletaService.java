package cl.duoc.SistemaMarket.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import cl.duoc.SistemaMarket.dto.BoletaDTO;
import cl.duoc.SistemaMarket.dto.BoletaDTOMapper;
import cl.duoc.SistemaMarket.exception.RecursoNoEncontradoException;
import cl.duoc.SistemaMarket.model.Boleta;
import cl.duoc.SistemaMarket.repository.BoletaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoletaService {
    
    private final BoletaRepository boletaRepository;
    private final BoletaDTOMapper boletaDTOMapper;

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

    public BoletaDTO obtenerBoletaPorFolio(String folio){

        Boleta boleta = boletaRepository.findByFolioBoleta(folio);
        if (boleta == null || !boleta.getFolioBoleta().equals(folio)) {
            throw new RecursoNoEncontradoException("Numero de folio incorrecto");
        }
        return boletaDTOMapper.toDTO(boleta);
    }

    public boolean guardarBoleta(BoletaDTO boletaDTO){

        if (boletaDTO == null || boletaDTO.getFolio() == null || boletaDTO.getFolio().isEmpty()) {
            throw new IllegalArgumentException("La boleta o su número de folio no pueden ser nulos o vacíos");
        }

        Boleta boleta = boletaDTOMapper.toModel(boletaDTO);
        return boletaRepository.save(boleta) != null;
    }

    public boolean actualizarBoleta(String folio, BoletaDTO boletaDTOActualizada) {

        //solo existe el metodo save para poder actualizar
        Boleta boletaExistente = boletaRepository.findByFolioBoleta(folio);
        if (boletaExistente == null || !boletaExistente.getFolioBoleta().equals(folio)) {
            throw new RecursoNoEncontradoException("Número de folio incorrecto.");
        }

        Boleta boletaActualizada = boletaDTOMapper.toModel(boletaDTOActualizada);
        boletaActualizada.setId(boletaExistente.getId());
        return boletaRepository.save(boletaActualizada) != null;

    }

    public boolean eliminarBoleta(String folio) {

        Boleta boletaExistente = boletaRepository.findByFolioBoleta(folio);

        if (boletaExistente == null || !boletaExistente.getFolioBoleta().equals(folio)) {
            throw new RecursoNoEncontradoException("Número de folio incorrecto.");
        }

        boletaRepository.deleteByFolioBoleta(folio);
        return true;
    }
}

