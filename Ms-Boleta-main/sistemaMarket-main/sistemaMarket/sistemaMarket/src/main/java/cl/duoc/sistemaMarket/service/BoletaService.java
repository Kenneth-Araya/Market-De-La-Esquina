package cl.duoc.sistemaMarket.service;

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

    public List<BoletaDTO> listarTodos(){
        
        List<Boleta> boletas = boletaRepository.findAll();
        List<BoletaDTO> boletaDTOs = new ArrayList<>();

        if (!boletas.isEmpty()) {
            for (Boleta boleta : boletas) {
                boletaDTOs.add(BoletaDTOMapper.toDTO(boleta));
            }
        }
        return boletaDTOs;
    }

    public BoletaDTO obtenerBoletaPorFolio(String folio){

        Boleta boleta = boletaRepository.findByFolio(folio);
        if (boleta == null || !boleta.getFolio().equals(folio)) {
            throw new RecursoNoEncontradoException("Numero de folio incorrecto");
        }
        return BoletaDTOMapper.toDTO(boleta);
    }

    public boolean guardarBoleta(BoletaDTO boletaDTO){

        if (boletaDTO == null || boletaDTO.getFolio() == null || boletaDTO.getFolio().isEmpty()) {
            throw new IllegalArgumentException("La boleta o su número de folio no pueden ser nulos o vacíos");
        }

        Boleta boleta = BoletaDTOMapper.toModel(boletaDTO);
        return boletaRepository.save(boleta) != null;
    }

    public boolean actualizarBoleta(String folio, BoletaDTO boletaDTOActualizada) {

        //solo existe el metodo save para poder actualizar
        Boleta boletaExistente = boletaRepository.findByFolio(folio);
        if (boletaExistente == null || !boletaExistente.getFolio().equals(folio)) {
            throw new RecursoNoEncontradoException("Número de folio incorrecto.");
        }

        Boleta boletaActualizada = BoletaDTOMapper.toModel(boletaDTOActualizada);
        boletaActualizada.setId(boletaExistente.getId());
        return boletaRepository.save(boletaActualizada) != null;

    }

    public boolean eliminarBoleta(String folio) {

        Boleta boletaExistente = boletaRepository.findByFolio(folio);

        if (boletaExistente == null || !boletaExistente.getFolio().equals(folio)) {
            throw new RecursoNoEncontradoException("Número de folio incorrecto.");
        }

        boletaRepository.deleteByFolio(folio);
        return true;
    }
}
