package cl.duoc.sistemaMarket.Service;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import cl.duoc.msVenta.dto.VentaDTO;
import cl.duoc.msVenta.exeptions.RecursoNoEncontradoException;
import cl.duoc.msVenta.model.Venta;
import cl.duoc.msVenta.repository.VentaRepository;
import cl.duoc.msVenta.service.VentaService;

@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaService ventaService;

    //test unitario listar todas las ventas
    @Test
    void listarVentas_DeberiaRetornarListaDeDtos(){
        Venta venta = new Venta();

        venta.setIdVenta(1L);
        venta.setDescripcionVenta("Compra bebidas y snacks");

        when(ventaRepository.findAllByOrderByIdVentaAsc()).thenReturn(List.of(venta));

        List<VentaDTO>resultado=ventaService.listarTodos();

        assertNotNull(resultado,"la lista no deberia ir null");
        assertEquals(1, resultado.size(),"la lista deberia tener minimo una venta");
        assertEquals("Compra bebidas y snacks", resultado.get(0).getDescripcionVentaDto(),"La descripcion no coincide");

        verify(ventaRepository, times(1)).findAllByOrderByIdVentaAsc();
    }

    //test unitario guardar las ventas
    @Test
    void guardarVenta_IllegalArgumentException(){
        VentaDTO ventadto = new VentaDTO();
        ventadto.setDescripcionVentaDto("Ventas de media docena de huevos");

        when(ventaRepository.existsByDescripcionVenta("Ventas de media docena de huevos")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,()->{
            ventaService.guardarVenta(ventadto);
        },"IllegalArgumentException cuando la descripcion sea existente");
        verify(ventaRepository, never()).save(any(Venta.class));
    }

    //test unitario guardar con condicion
    @Test
    void guardarVenta_CuandoLaDescripcionNoExista(){
        VentaDTO ventadto = new VentaDTO();
        ventadto.setDescripcionVentaDto("Ventas de media docena de huevos");
        ventadto.setMontoPagoVentaDto(6000.0);
    
        when(ventaRepository.existsByDescripcionVenta("Ventas de media docena de huevos")).thenReturn(false);

        Venta venta = new Venta();
        venta.setDescripcionVenta("Ventas de media docena de huevos");
        venta.setMontoVenta(6000.0);
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
        
        VentaDTO resultado = ventaService.guardarVenta(ventadto);

        assertNotNull(resultado,"La venta guardada no deberia ser null");
        assertEquals(6000.0, resultado.getMontoPagoVentaDto(),"El precio debería ser 6000.0");

        verify(ventaRepository, times(1)).save(any(Venta.class));
    }

    //test unitario buscar por id 
    @Test
    void buscarVenta_CuandoIdExiste(){

        Venta venta = new Venta();
        venta.setIdVenta(1L);
        venta.setDescripcionVenta("Ventas de media docena de huevos");
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        VentaDTO resultado = ventaService.findById(1L);

        //then
        assertNotNull(resultado,"La venta  no deberia ser null");
        assertEquals("Ventas de media docena de huevos", resultado.getDescripcionVentaDto(),"La descripcion deberia coincidir");

        verify(ventaRepository, times(1)).findById(1L);
    }

    //test unitario eliminar por id 
    @Test
    void eliminarVentaNoExistente(){
        when(ventaRepository.existsById(98L)).thenReturn(false);
        
        //When y then 
        assertThrows(IllegalArgumentException.class, ()->{
            ventaService.eliminarPorId(98L);
        },"IllegalArgumentException si el id no existe");

        verify(ventaRepository, never()).deleteById(anyLong());
    }

    //test unitario eliminar por id 
    @Test
    void eliminarVenta_CuandoIdExiste(){
        when(ventaRepository.existsById(1L)).thenReturn(true);

        //when
        ventaService.eliminarPorId(1L);

        //then
        verify(ventaRepository, times(1)).deleteById(1L);
    }

    //test unitario actualizar por id 
    @Test
    void actualizarVenta_CuandoIdNoExista(){
        Long idInexistente = 98L;
        VentaDTO ventadto = new VentaDTO();
        ventadto.setDescripcionVentaDto("descripcion de prueba");

        when(ventaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        //when y then
        assertThrows(RecursoNoEncontradoException.class, ()->{
            ventaService.actualizarVenta(idInexistente, ventadto);
        });
    }

    //test unitario actualizar por id 
    @Test
    void actualizarVenta_CuandoIdExiste(){
        Long id=1L;
        Venta ventaexistente = new Venta();
        ventaexistente.setIdVenta(id);
        ventaexistente.setDescripcionVenta("Descripcion Original");

        VentaDTO ventadto = new VentaDTO();
        ventadto.setDescripcionVentaDto("Descripcion Ocupada");

        when(ventaRepository.findById(id)).thenReturn(Optional.of(ventaexistente));
        when(ventaRepository.existsByDescripcionVenta("Descripcion Ocupada")).thenReturn(true);

        //when y then
        assertThrows(IllegalArgumentException.class,()->{
            ventaService.actualizarVenta(id, ventadto);
        },"IllegalArgumentException si la nueva descripcion ya es existente");

    }

    //test unitario actualizar por id 
    @Test
    void actualizarVentas_DeberiaRetornarVentaActualizada(){
        Long id=1L;
        Venta venta = new Venta();
        venta.setIdVenta(id);
        venta.setDescripcionVenta("Descripcion Original");

        VentaDTO ventadto = new VentaDTO();
        ventadto.setDescripcionVentaDto("Descripcion Nuevo");
        ventadto.setMontoPagoVentaDto(20000.0);
        
        when(ventaRepository.findById(id)).thenReturn(Optional.of(venta));
        when(ventaRepository.existsByDescripcionVenta("Descripcion Nuevo")).thenReturn(false);
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        //when
        VentaDTO resultado = ventaService.actualizarVenta(id, ventadto);

        //then
        assertNotNull(resultado,"El resultado no deberia ser null");
        assertEquals("Descripcion Nuevo", resultado.getDescripcionVentaDto(),"La Descripcion deberia actualizarse");
        verify(ventaRepository, times(1)).save(any(Venta.class));

    }


}

