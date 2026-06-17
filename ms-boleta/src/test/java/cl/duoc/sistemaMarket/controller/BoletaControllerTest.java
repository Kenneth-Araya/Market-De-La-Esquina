package cl.duoc.sistemaMarket.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import cl.duoc.sistemaMarket.dto.BoletaDTO;
import cl.duoc.sistemaMarket.service.BoletaService;

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

@WebMvcTest(BoletaController.class)
public class BoletaControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoletaService boletaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void listarBoleta_DeberiaRetornar200() throws Exception{
        //given
        BoletaDTO boletadto = new BoletaDTO();
        boletadto.setFolio("FOL-Y3P84N");

        BoletaDTO boletadtos = new BoletaDTO();
        boletadtos.setFolio("FOL-Z7R15Q");

        List<BoletaDTO> listaSimulada = Arrays.asList(boletadto, boletadtos);

        when(boletaService.listarTodos()).thenReturn(listaSimulada);

        //when y then
        mockMvc.perform(get("/api/v1/boletas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("FOL-Y3P84N"))
                .andExpect(jsonPath("$[1].nombre").value("FOL-Z7R15Q"));  
    }

    @Test
    void guardarBoleta_201() throws Exception{
        //given
        BoletaDTO nuevaBoleta = new BoletaDTO();
        nuevaBoleta.setEstado("EMITIDA");
        nuevaBoleta.setFecha(LocalDate.of(2025, 6, 15));
        nuevaBoleta.setFolio("FOL-A00001");
        nuevaBoleta.setGlosa("Compra de artículos de oficina");
        nuevaBoleta.setMontoBruto(11900);
        nuevaBoleta.setMontoNeto(10000);
        nuevaBoleta.setTipo("BOLETA");

        when(boletaService.guardarBoleta(any(BoletaDTO.class))).thenReturn(nuevaBoleta);

        //when y then
        mockMvc.perform(post("/api/v1/boletas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevaBoleta)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.folio").value("FOL-A21646"));

    }

    @Test
    void eliminarBoleta_204() throws Exception {
        //given
        String folio = "FOL-000000";
        doNothing().when(boletaService).eliminarBoleta("FOL-000000");

        //when y then
        mockMvc.perform(delete("/api/v1/boletas/{folio}", folio))
                .andExpect(status().isNoContent());
    }

 @Test
 void actualizarBoleta_200() throws Exception{
    //given
    String folio = "FOL-000000";

    BoletaDTO dtoActualizado = new BoletaDTO();
    dtoActualizado.setEstado("PAGADA");
    dtoActualizado.setFecha(LocalDate.of(2025, 5, 28));
    dtoActualizado.setFolio("FOL-A00002");
    dtoActualizado.setGlosa("Servicio de mantención informática");
    dtoActualizado.setMontoBruto(59500);
    dtoActualizado.setMontoNeto(50000);
    dtoActualizado.setTipo("BOLETA ELECTRONICA");

    when(boletaService.actualizarBoleta(eq(folio), any(BoletaDTO.class))).thenReturn(true);

    //when y then
    mockMvc.perform(put("/api/v1/ventas/{folio}", folio)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dtoActualizado)))
            .andExpect(status().isOk());
}

    @Test
    void listarBoletaPorId_200() throws Exception{
        String folio = "FOL-000000";
        BoletaDTO boleta = new BoletaDTO();

        boleta.setEstado("EMITIDA");
        boleta.setFecha(LocalDate.of(2025, 4, 17));
        boleta.setFolio("FOL-B00015");
        boleta.setGlosa("Venta de accesorios para computador");
        boleta.setMontoBruto(35700);
        boleta.setMontoNeto(30000);
        boleta.setTipo("BOLETA ELECTRONICA");

        when(boletaService.guardarBoleta(any(BoletaDTO.class))).thenReturn(boleta);

        //when t then
        mockMvc.perform(get("/api/v1/boleta/{folio}", folio))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.folio").value("FOL-B00015"));

    }
}