package cl.duoc.ms_inventario.controller;
import cl.duoc.ms_inventario.dto.InventarioDTO;
import cl.duoc.ms_inventario.service.InventarioService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest({InventarioController.class})
public class InventarioControllerTest {
   @Autowired
   private MockMvc mockMvc;
   @MockitoBean
   private InventarioService servicio;

   public InventarioControllerTest() {
   }

   @Test
   void consultarStock_CuandoIdExiste_DeberiaRetornar200YElDTO() throws Exception {
      Long idProducto = 1L;
      InventarioDTO dtoMock = new InventarioDTO();
      dtoMock.setIdProducto(idProducto);
      dtoMock.setStockActual(20);
      dtoMock.setEstadoProducto("DISPONIBLE");
      Mockito.when(this.servicio.validarObteniendoStock(idProducto)).thenReturn(dtoMock);
      this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inventario/{id}", new Object[]{idProducto})).andExpect(MockMvcResultMatchers.jsonPath("$.idProducto", new Object[0]).value(idProducto)).andExpect(MockMvcResultMatchers.jsonPath("$.stockActual", new Object[0]).value(20)).andExpect(MockMvcResultMatchers.jsonPath("$.estadoProducto", new Object[0]).value("DISPONIBLE"));
   }

   @Test
   void descontarStock_CuandoDatosSonValidos_DeberiaRetornar200YMensajeTexto() throws Exception {
      Long idProducto = 1L;
      int cantidad = 5;
      ((InventarioService)Mockito.doNothing().when(this.servicio)).descontarStock(idProducto, cantidad);
      String mensajeEsperado = "Stock actualizado: se descontaron " + cantidad + " unidades";
      this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/inventario/{id}/descontar", new Object[]{idProducto}).param("cantidad", new String[]{String.valueOf(cantidad)})).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(mensajeEsperado));
   }

   @Test
   void listarBajoStock_CuandoHayProductosBajos_DeberiaRetornar200YLista() throws Exception {
      int limite = 10;
      InventarioDTO dtoMock = new InventarioDTO();
      dtoMock.setIdProducto(1L);
      dtoMock.setStockActual(5);
      List<InventarioDTO> listaMock = Arrays.asList(dtoMock);
      Mockito.when(this.servicio.listarProductosBajoStock(limite)).thenReturn(listaMock);
      this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inventario/bajo/{limite}", new Object[]{limite})).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.size()", new Object[0]).value(1)).andExpect(MockMvcResultMatchers.jsonPath("$[0].stockActual", new Object[0]).value(5));
   }

   @Test
   void listarTodo_CuandoExistenProductos_DeberiaRetornar200YListaCompleta() throws Exception {
      InventarioDTO dto1 = new InventarioDTO();
      dto1.setIdProducto(1L);
      InventarioDTO dto2 = new InventarioDTO();
      dto2.setIdProducto(2L);
      List<InventarioDTO> listaMock = Arrays.asList(dto1, dto2);
      Mockito.when(this.servicio.listarTodo()).thenReturn(listaMock);
      this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inventario", new Object[0])).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.size()", new Object[0]).value(2));
   }

   @Test
   void agregarStock_CuandoDatosSonValidos_DeberiaRetornar200YMensajeTexto() throws Exception {
      Long idProducto = 1L;
      int cantidad = 15;
      ((InventarioService)Mockito.doNothing().when(this.servicio)).agregarStock(idProducto, cantidad);
      String mensajeEsperado = "Stock actualizado: se agregaron " + cantidad + " unidades";
      this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/inventario/{id}/agregar", new Object[]{idProducto}).param("cantidad", new String[]{String.valueOf(cantidad)})).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(mensajeEsperado));
   }
}