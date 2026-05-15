package cl.duoc.sistemaMarket.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import cl.duoc.sistemaMarket.dto.BoletaVentaDTO;

@FeignClient(name = "boletas-service", url = "http://localhost:8081")
public interface BoletaClient {

    @GetMapping("/boletas/{folio}")
    BoletaVentaDTO obtenerBoleta(@PathVariable String folio);

    @PutMapping("/boletas/{folio}/pagada")
    void marcarComoPagada(@PathVariable String folio);
}