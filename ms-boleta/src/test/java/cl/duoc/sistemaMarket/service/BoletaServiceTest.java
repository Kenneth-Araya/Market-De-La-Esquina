package cl.duoc.sistemaMarket.service;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import cl.duoc.sistemaMarket.dto.BoletaDTO;
import cl.duoc.sistemaMarket.dto.BoletaDTOMapper;
import cl.duoc.sistemaMarket.exeptions.RecursoNoEncontradoException;
import cl.duoc.sistemaMarket.model.Boleta;
import cl.duoc.sistemaMarket.repository.BoletaRepository;

@ExtendWith(MockitoExtension.class)
public class BoletaServiceTest{

    @Mock
    private BoletaRepository boletaRepository;

    @Mock
    private BoletaDTOMapper boletaDTOMapper;

    @InjectMocks
    private BoletaService boletaService;

    //test unitario listar usuarios
    @Test
    void listarTodos_CuandoExistenBoletas() {
        // Given
        Boleta boleta = new Boleta();
        boleta.setFolioBoleta("FOL-001");

        BoletaDTO boletaDTO = new BoletaDTO();
        boletaDTO.setFolio("FOL-001");

        when(boletaRepository.findAll()).thenReturn(List.of(boleta));
        when(boletaDTOMapper.toDTO(boleta)).thenReturn(boletaDTO);

        // When
        List<BoletaDTO> resultado = boletaService.listarTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(boletaRepository, times(1)).findAll();
    }

    //test unitario listar usuarios con condicion
    @Test
    void listarTodos_CuandoNoExistenBoletas() {
        // Given
        when(boletaRepository.findAll()).thenReturn(List.of());

        // When
        List<BoletaDTO> resultado = boletaService.listarTodos();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(boletaRepository, times(1)).findAll();
    }

    //test unitario obteber usuarios
    @Test
    void obtenerBoletaPorFolio_CuandoFolioExiste() {
        // Given
        String folio = "FOL-MN784P";
        Boleta boleta = new Boleta();
        boleta.setFolioBoleta(folio);

        BoletaDTO boletaDTO = new BoletaDTO();
        boletaDTO.setFolio(folio);

        when(boletaRepository.findByFolioBoleta(folio)).thenReturn(boleta);
        when(boletaDTOMapper.toDTO(boleta)).thenReturn(boletaDTO);

        // When
        BoletaDTO resultado = boletaService.obtenerBoletaPorFolio(folio);

        // Then
        assertNotNull(resultado, "La boleta no debería ser null");
        assertEquals(folio, resultado.getFolio(), "El folio debería coincidir");
        verify(boletaRepository, times(1)).findByFolioBoleta(folio);
    }

    //test unitario obteber usuarios con condicion
    @Test
    void obtenerBoletaPorFolio_CuandoFolioNoExiste() {
        // Given
        String folio = "FOL-INEXISTENTE";
        when(boletaRepository.findByFolioBoleta(folio)).thenReturn(null);

        // When & Then
        assertThrows(RecursoNoEncontradoException.class, () -> {
            boletaService.obtenerBoletaPorFolio(folio);
        }, "RecursoNoEncontradoException si el folio no existe");

        verify(boletaDTOMapper, never()).toDTO(any());
    }

    //test unitario guardar usuarios
    @Test
    void guardarBoleta_CuandoDatosValidos() {
        // Given
        BoletaDTO boletaDTO = new BoletaDTO();
        boletaDTO.setFolio("FOL-MN784P");
        boletaDTO.setFecha(LocalDate.now());

        Boleta boleta = new Boleta();
        boleta.setFolioBoleta("FOL-MN784P");

        when(boletaDTOMapper.toModel(boletaDTO)).thenReturn(boleta);
        when(boletaRepository.save(boleta)).thenReturn(boleta);
        when(boletaDTOMapper.toDTO(boleta)).thenReturn(boletaDTO);

        // When
        BoletaDTO resultado = boletaService.guardarBoleta(boletaDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("FOL-MN784P", resultado.getFolio());
        verify(boletaRepository, times(1)).save(any(Boleta.class));
    }

    //test unitario guardar usuarios con condicion
    @Test
    void guardarBoleta_CuandoFolioEsNulo() {
        // Given
        BoletaDTO boletaDTO = new BoletaDTO();
        boletaDTO.setFolio(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            boletaService.guardarBoleta(boletaDTO);
        }, "IllegalArgumentException si el folio es nulo");

        verify(boletaRepository, never()).save(any(Boleta.class));
    }

    @Test
    void guardarBoleta_CuandoFechaEsFutura() {
        // Given
        BoletaDTO boletaDTO = new BoletaDTO();
        boletaDTO.setFolio("FOL-MN784P");
        boletaDTO.setFecha(LocalDate.now().plusDays(5));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            boletaService.guardarBoleta(boletaDTO);
        }, "IllegalArgumentException si la fecha es futura");

        verify(boletaRepository, never()).save(any(Boleta.class));
    }

    //test unitario actualizar usuarios con condicion
    @Test
    void actualizarBoleta_CuandoFolioNoExiste() {
        // Given
        String folioInexistente = "FOL-INEXISTENTE";
        BoletaDTO boletaDTO = new BoletaDTO();
        boletaDTO.setFolio(folioInexistente);

        when(boletaRepository.findByFolioBoleta(folioInexistente)).thenReturn(null);

        // When & Then
        assertThrows(RecursoNoEncontradoException.class, () -> {
            boletaService.actualizarBoleta(folioInexistente, boletaDTO);
        }, "RecursoNoEncontradoException si el folio no existe");

        verify(boletaRepository, never()).save(any(Boleta.class));
    }

    //test unitario guardar usuarios
    @Test
    void actualizarBoleta_CuandoDatosValidos() {
        // Given
        String folio = "FOL-MN784P";
        Boleta boletaExistente = new Boleta();
        boletaExistente.setId(1L);
        boletaExistente.setFolioBoleta(folio);

        BoletaDTO boletaDTO = new BoletaDTO();
        boletaDTO.setFolio(folio);
        boletaDTO.setFecha(LocalDate.now());

        Boleta boletaActualizada = new Boleta();
        boletaActualizada.setFolioBoleta(folio);

        when(boletaRepository.findByFolioBoleta(folio)).thenReturn(boletaExistente);
        when(boletaDTOMapper.toModel(boletaDTO)).thenReturn(boletaActualizada);
        when(boletaRepository.save(any(Boleta.class))).thenReturn(boletaActualizada);

        // When
        boolean resultado = boletaService.actualizarBoleta(folio, boletaDTO);

        // Then
        assertTrue(resultado, "Debería retornar true al actualizar correctamente");
        verify(boletaRepository, times(1)).save(any(Boleta.class));
    }

    //test unitario eliminar usuarios
    @Test
    void eliminarBoleta_CuandoFolioExiste() {
        // Given
        String folio = "FOL-MN784P";
        Boleta boletaExistente = new Boleta();
        boletaExistente.setFolioBoleta(folio);

        when(boletaRepository.findByFolioBoleta(folio)).thenReturn(boletaExistente);

        // When
        boolean resultado = boletaService.eliminarBoleta(folio);

        // Then
        assertTrue(resultado);
        verify(boletaRepository, times(1)).deleteByFolioBoleta(folio);
    }

    //test unitario eliminar usuarios con condicion
    @Test
    void eliminarBoleta_CuandoFolioNoExiste() {
        // Given
        String folio = "FOL-INEXISTENTE";
        when(boletaRepository.findByFolioBoleta(folio)).thenReturn(null);

        // When & Then
        assertThrows(RecursoNoEncontradoException.class, () -> {
            boletaService.eliminarBoleta(folio);
        }, "RecursoNoEncontradoException si el folio no existe");

        verify(boletaRepository, never()).deleteByFolioBoleta(anyString());
    }
}

