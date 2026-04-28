package cl.duoc.SistemaMarket.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "boletas-service", url = "http://localhost:8081")
public interface BoletaClient {

    @GetMapping("/boletas/{folio}")
    //BoletaDTO obtenerBoleta(@PathVariable String folio);

    @PutMapping("/boletas/{folio}/pagada")
    void marcarComoPagada(@PathVariable String folio);
}