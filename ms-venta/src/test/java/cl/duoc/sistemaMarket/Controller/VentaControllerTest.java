package cl.duoc.sistemaMarket.Controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import cl.duoc.msVenta.controller.VentaController;
import cl.duoc.msVenta.dto.VentaDTO;
import cl.duoc.msVenta.service.VentaService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(VentaController.class)
public class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VentaService ventaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void listarVentas_DeberiaRetornar200() throws Exception{
        //given
        VentaDTO ventadto = new VentaDTO();
        ventadto.setDescripcionVentaDto("Lacteos");

        VentaDTO ventadtos = new VentaDTO();
        ventadtos.setDescripcionVentaDto("Carbohidratos");

        List<VentaDTO> listaSimulada=Arrays.asList(ventadto, ventadtos);

        when(ventaService.listarTodos()).thenReturn(listaSimulada);

        //when y then
        mockMvc.perform(get("/api/v1/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Lacteos"))
                .andExpect(jsonPath("$[1].nombre").value("Carbohidratos"));  
    }

    @Test
    void guardarVenta_201() throws Exception{
        //given
        VentaDTO nuevaVenta = new VentaDTO();
        nuevaVenta.setDescripcionVentaDto("Lacteos");
        nuevaVenta.setMontoPagoVentaDto(15000.0);
        nuevaVenta.setCodigoTransaccionVentaDto("TXN-20260220-002");
        nuevaVenta.setEstadoPagoVentaDto("PENDIENTE");
        nuevaVenta.setTotalVentaDto(7000.0);
        nuevaVenta.setFechaVentaDto(LocalDateTime.of(20026, 06, 15, 3, 20));
        nuevaVenta.setMetodoPagoVentaDto("DEBITO");

        when(ventaService.guardarVenta(any(VentaDTO.class))).thenReturn(nuevaVenta);

        //when y then
        mockMvc.perform(post("/api/v1/ventas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevaVenta)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Lacteos"));

    }

    @Test
    void eliminarVenta_204() throws Exception {
        //given
        Long id=1L;
        doNothing().when(ventaService).eliminarPorId(id);

        //when y then
        mockMvc.perform(delete("/api/v1/ventas/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void actualizarVenta_200() throws Exception{
        //given
        Long id=1L;
        VentaDTO dtoActualizado = new VentaDTO();
        dtoActualizado.setDescripcionVentaDto("Carbohidratos");
        dtoActualizado.setMontoPagoVentaDto(10000.0);
        dtoActualizado.setCodigoTransaccionVentaDto("TXN-20260312-001");
        dtoActualizado.setEstadoPagoVentaDto("PAGADO");
        dtoActualizado.setTotalVentaDto(10000.0);
        dtoActualizado.setFechaVentaDto(LocalDateTime.of(20026, 06, 15, 3, 20));
        dtoActualizado.setMetodoPagoVentaDto("EFECTIVO");

        when(ventaService.actualizarVenta(eq(id), any(VentaDTO.class))).thenReturn(dtoActualizado);

        //when Y then
        mockMvc.perform(put("/api/v1/ventas/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Carbohidratos"));
    }

    @Test
    void listarVentaPorId_200() throws Exception{
        Long id=1L;
        VentaDTO venta = new VentaDTO();

        venta.setDescripcionVentaDto("Suplementos proteicos");
        venta.setMontoPagoVentaDto(25500.0);
        venta.setCodigoTransaccionVentaDto("TXN-20260616-002");
        venta.setEstadoPagoVentaDto("PAGADO");
        venta.setTotalVentaDto(25500.0);
        venta.setFechaVentaDto(LocalDateTime.of(2026, 6, 16, 10, 30));
        venta.setMetodoPagoVentaDto("TARJETA_DEBITO");

        when(ventaService.guardarVenta(any(VentaDTO.class))).thenReturn(venta);

        //when t then
        mockMvc.perform(get("/api/v1/ventas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Vitaminas y minerales"))
                .andExpect(jsonPath("$.codigoTransaccionVentaDto").value("TXN-20260616-004"));
    }
}