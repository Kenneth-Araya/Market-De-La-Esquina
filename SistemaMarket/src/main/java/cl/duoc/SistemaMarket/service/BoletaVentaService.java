package cl.duoc.SistemaMarket.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import cl.duoc.SistemaMarket.client.BoletaClient;
import cl.duoc.SistemaMarket.dto.BoletaPagoDTO;
import cl.duoc.SistemaMarket.model.Venta;
import cl.duoc.SistemaMarket.repository.VentaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoletaVentaService {

    private final BoletaClient boletaClient;
    private final VentaRepository VentaRepository;

    public BoletaPagoDTO obtenerBoleta(String folio) {
        return boletaClient.obtenerBoleta(folio);
    }

    public void autorizarVenta(String folio) {
        
        //Obtener boleta
        BoletaPagoDTO boleta = boletaClient.obtenerBoleta(folio);

        if (boleta == null) {
            throw new RuntimeException("Boleta no encontrada");
        }

        //Validar estado
        if (!"PENDIENTE".equals(boleta.getEstado())) {
            throw new RuntimeException("Boleta ya pagada");
        }

        //Registrar venta
        Venta venta = new Venta();
        venta.setFechaVenta(LocalDateTime.now());
        venta.setTotalVenta(999.999);
        venta.setDescripcionVenta("Pago autorizado para boleta " + folio);
        venta.setMontoVenta(boleta.getMonto().doubleValue());
        venta.setMetodoVenta("DEBITO");
        venta.setEstadoVenta("PAGADO");
        venta.setCodigoTransaccionVenta("TX-" + folio);

        VentaRepository.save(venta);

        //Actualizar boleta
        boletaClient.marcarComoPagada(folio);
    }
}
