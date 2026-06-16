package cl.duoc.ms_inventario.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Arrays;
import cl.duoc.ms_inventario.clients.ProductoClient;
import cl.duoc.ms_inventario.dto.InventarioDTO;
import cl.duoc.ms_inventario.dto.InventarioMapper;
import cl.duoc.ms_inventario.dto.ProductoResponseDTO;
import cl.duoc.ms_inventario.exception.RecursoNoEncontradoException;
import cl.duoc.ms_inventario.model.Inventario;
import cl.duoc.ms_inventario.repository.InventarioRepository;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {

    @Mock
    private InventarioRepository repo;

    @Mock
    private InventarioMapper mapper;//LO AGREGAMOS PORQUE EL METODO LO USA

    @Mock
    private ProductoClient productoClient;//TAMBIEN LO AGREGAMOS PORQUE EL METODO LO USA

    @InjectMocks
    private InventarioService servicio;

    @Test
    void validarObteniendoStock_CuandoHayStock_DeberiaRetorarDisponible(){
        //GIVEN PREPARAMOS EL ESCENARIO
        Long idProducto=1L;

        Inventario inventario=new Inventario();
        inventario.setIdProducto(idProducto);
        inventario.setStockActual(10);//HAY STOCK

        InventarioDTO dto=new InventarioDTO();
        dto.setStockActual(10);

        ProductoResponseDTO productofeign= new ProductoResponseDTO();
        productofeign.setNombre("Pan Hallulla");

        when(repo.findByIdProducto(idProducto)).thenReturn(Optional.of(inventario));
        when(mapper.toDTO(inventario)).thenReturn(dto);
        when(productoClient.obtenerProductoPorId(idProducto)).thenReturn(productofeign);

        //WHEN EJECUTAMOS EL METODO
        InventarioDTO resultado=servicio.validarObteniendoStock(idProducto);

        //THEN VERIFICAMOS LOS RESULTADOS
        assertNotNull(resultado,"el DTO no deberia ser nulo");
        assertEquals("DISPONIBLE", resultado.getEstadoProducto(),"El estado debe ser DISPONIBLE porque hay stock mayor a cero");
        assertEquals("Pan Hallulla",resultado.getNombreProducto(),"Deberia haber seteado el nombre cruzando por Feign");

        verify(productoClient, times(1)).obtenerProductoPorId(idProducto);
    }

    @Test
    void validarObteniendoStock_CuandoNoHayStock_DeberiaRetorarSinStock(){
        //GIVEN PREPARAMOS UN ESCENARIO SIN STOCK
        Long idProducto=2L;

        Inventario inventario=new Inventario();
        inventario.setIdProducto(idProducto);
        inventario.setStockActual(0);

        InventarioDTO dto=new InventarioDTO();
        dto.setStockActual(0);

        ProductoResponseDTO productofeign=new ProductoResponseDTO();
        productofeign.setNombre("Coca-Cola");

        when(repo.findByIdProducto(idProducto)).thenReturn(Optional.of(inventario));
        when(mapper.toDTO(inventario)).thenReturn(dto);
        when(productoClient.obtenerProductoPorId(idProducto)).thenReturn(productofeign);

        //WHEN
        InventarioDTO resultado=servicio.validarObteniendoStock(idProducto);
        
        //THEN
        assertNotNull(resultado);
        assertEquals("SIN STOCK", resultado.getEstadoProducto(),"El estado debe ser SIN STOCK porque el valor es cero");
        assertEquals("Coca-Cola", resultado.getNombreProducto());
        
    }

    @Test
    void validarObteniendoStock_CuandoProductoNoExiste_DeberiaLanzarExcepcion() {
        // GIVEN: SIMULAMOS QUE LA BASE DE DATOS NO ENCUENTRA NADA
        Long idInexistente = 99L;
        when(repo.findByIdProducto(idInexistente)).thenReturn(Optional.empty());

        // WHEN y THEN: VERIFICAMOS QUE SE LANCE LA EXCEPCIÓN CORRECTA
        assertThrows(RecursoNoEncontradoException.class, () -> {
            servicio.validarObteniendoStock(idInexistente);
        }, "Debería lanzar RecursoNoEncontradoException si el id no existe en el inventario");

        // Verificamos que el código nunca llegó a llamar a Feign porque falló antes en la BD
        verify(productoClient, never()).obtenerProductoPorId(anyLong());
    }

    @Test
    void descontarStock_CuandoHayStockSuficiente_DeberiaDescontarYGuardar(){
        //GIVEN: HAY 19 UNIDADES EN BODEGA Y QUEREMOS SACAR 3
        Long idProducto=1L;
        int cantidadADescontar=3;

        Inventario inventario=new Inventario();
        inventario.setIdProducto(idProducto);
        inventario.setStockActual(10);

        when(repo.findByIdProducto(idProducto)).thenReturn(Optional.of(inventario));

        //WHEN EJECUTAMOS EL DESCUENTO
        servicio.descontarStock(idProducto, cantidadADescontar);

        //THEN VERIFICAMOS QUE LA MATEMATICA SE HIZO BIEN Y SE GUARDO
        assertEquals(7, inventario.getStockActual(),"El stock debio disminuir de 10 a 7");
        verify(repo, times(1)).save(inventario);// Confirmamos que se llamó al guardado en BD
    }

    @Test
    void descontarStock_CuandoStockEsInsuficiente_DeberiaLanzarRuntimeException() {
        // GIVEN: Hay 5 unidades, pero intentamos sacar 10
        Long idProducto = 1L;
        int cantidadADescontar = 10;
        
        Inventario inventario = new Inventario();
        inventario.setIdProducto(idProducto);
        inventario.setStockActual(5); 
        
        when(repo.findByIdProducto(idProducto)).thenReturn(Optional.of(inventario));
        
        // WHEN & THEN: Verificamos que salte la excepción exacta
        assertThrows(RuntimeException.class, () -> {
            servicio.descontarStock(idProducto, cantidadADescontar);
        }, "Debería lanzar RuntimeException por stock insuficiente");
        
        // Verificamos que NUNCA se intente guardar un stock negativo en la BD
        verify(repo, never()).save(any(Inventario.class));
    }

    @Test
    void descontarStock_CuandoProductoNoExiste_DeberiaLanzarExcepcion() {
        // GIVEN: El producto no existe en BD
        Long idProducto = 99L;
        when(repo.findByIdProducto(idProducto)).thenReturn(Optional.empty());
        
        // WHEN & THEN: Verificamos el orElseThrow
        assertThrows(RecursoNoEncontradoException.class, () -> {
            servicio.descontarStock(idProducto, 5);
        }, "Debería lanzar RecursoNoEncontradoException si el producto no existe");
        
        verify(repo, never()).save(any(Inventario.class));
    }

    @Test
    void listarProductosBajoStock_DeberiaFiltrarCorrectamenteYAsignarNombres() {
        // GIVEN: Creamos un límite de stock de 10
        int limite = 10;
        
        // Producto 1: Bajo stock (Debe pasar el filtro)
        Inventario i1 = new Inventario();
        i1.setIdProducto(1L);
        i1.setStockActual(5);
        
        // Producto 2: Alto stock (NO debe pasar el filtro)
        Inventario i2 = new Inventario();
        i2.setIdProducto(2L);
        i2.setStockActual(20);
        
        when(repo.findAll()).thenReturn(List.of(i1, i2));
        
        // Mapeamos solo el que pasará el filtro
        InventarioDTO dto1 = new InventarioDTO();
        dto1.setIdProducto(1L);
        dto1.setStockActual(5);
        when(mapper.toDTO(i1)).thenReturn(dto1);
        
        // Simulamos la respuesta exitosa de Feign
        ProductoResponseDTO productoFeign = new ProductoResponseDTO();
        productoFeign.setNombre("Fideos");
        when(productoClient.obtenerProductoPorId(1L)).thenReturn(productoFeign);

        // WHEN: Ejecutamos el método
        List<InventarioDTO> resultado = servicio.listarProductosBajoStock(limite);

        // THEN: Verificamos
        assertNotNull(resultado);
        assertEquals(1, resultado.size(), "Debería traer solo 1 producto (el de stock 5)");
        assertEquals("Fideos", resultado.get(0).getNombreProducto(), "El nombre cruzado por Feign debería asignarse");
        assertEquals(5, resultado.get(0).getStockActual());
    }

    @Test
    void listarProductosBajoStock_CuandoNoHayElementos_DeberiaRetornarListaVacia() {
        // GIVEN: La BD devuelve una lista vacía
        when(repo.findAll()).thenReturn(List.of());

        // WHEN
        List<InventarioDTO> resultado = servicio.listarProductosBajoStock(5);

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "La lista debería estar vacía");
        
        // Verificamos que jamás intentó llamar a Feign
        verify(productoClient, never()).obtenerProductoPorId(anyLong());
    }

    @Test
    void listarProductosBajoStock_CuandoFeignFalla_DeberiaAsignarNombreNoDisponible() {
        // GIVEN: Un producto con stock bajo, pero el otro microservicio está caído
        Inventario i1 = new Inventario();
        i1.setIdProducto(1L);
        i1.setStockActual(2);
        
        when(repo.findAll()).thenReturn(List.of(i1));
        
        InventarioDTO dto1 = new InventarioDTO();
        dto1.setIdProducto(1L);
        when(mapper.toDTO(i1)).thenReturn(dto1);
        
        // ¡Magia de Mockito! Simulamos que Feign explota (arroja una excepción)
        when(productoClient.obtenerProductoPorId(1L)).thenThrow(new RuntimeException("Microservicio ms-producto inalcanzable"));

        // WHEN
        List<InventarioDTO> resultado = servicio.listarProductosBajoStock(10);

        // THEN
        assertEquals(1, resultado.size());
        assertEquals("Nombre no disponible", resultado.get(0).getNombreProducto(), "Debería asignar el nombre por defecto del bloque catch");
    }

    //
    @Test
    void listarTodo_DeberiaRetornarListaCompletaConNombres() {
        // GIVEN: 2 productos en la base de datos
        Inventario i1 = new Inventario(); i1.setIdProducto(1L); i1.setStockActual(10);
        Inventario i2 = new Inventario(); i2.setIdProducto(2L); i2.setStockActual(20);
        
        when(repo.findAll()).thenReturn(Arrays.asList(i1, i2));
        
        InventarioDTO dto1 = new InventarioDTO(); dto1.setIdProducto(1L);
        InventarioDTO dto2 = new InventarioDTO(); dto2.setIdProducto(2L);
        
        when(mapper.toDTO(i1)).thenReturn(dto1);
        when(mapper.toDTO(i2)).thenReturn(dto2);
        
        ProductoResponseDTO p1 = new ProductoResponseDTO(); p1.setNombre("Teclado");
        ProductoResponseDTO p2 = new ProductoResponseDTO(); p2.setNombre("Mouse");
        
        when(productoClient.obtenerProductoPorId(1L)).thenReturn(p1);
        when(productoClient.obtenerProductoPorId(2L)).thenReturn(p2);

        // WHEN
        List<InventarioDTO> resultado = servicio.listarTodo();

        // THEN
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Teclado", resultado.get(0).getNombreProducto());
        assertEquals("Mouse", resultado.get(1).getNombreProducto());
        
        verify(productoClient, times(1)).obtenerProductoPorId(1L);
        verify(productoClient, times(1)).obtenerProductoPorId(2L);
    }

    @Test
    void listarTodo_CuandoNoHayInventario_DeberiaRetornarListaVacia() {
        // GIVEN
        when(repo.findAll()).thenReturn(Arrays.asList());

        // WHEN
        List<InventarioDTO> resultado = servicio.listarTodo();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(productoClient, never()).obtenerProductoPorId(anyLong());
    }


}
