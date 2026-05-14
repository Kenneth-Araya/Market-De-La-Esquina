package cl.duoc.SistemaMarket.service;

import java.math.BigDecimal;
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

    public void autorizarVentaConMonto(String folio, BigDecimal montoPagado) {
        
        //Obtener boleta
        BoletaPagoDTO boleta = boletaClient.obtenerBoleta(folio);

        if (boleta == null) {
            throw new RuntimeException("Boleta no encontrada");
        }

        // Regla de negocio
        if (!"PENDIENTE".equals(boleta.getEstado())) {
            throw new IllegalStateException(
                "La boleta con folio" + folio + "Ya fue pagada. no se puede procesar pago nuevamente");
        }

        // Regla de negocio
        if (montoPagado == null || montoPagado.compareTo(boleta.getMonto()) != 0) {
            throw new IllegalArgumentException(
                "El monto pagado (" + montoPagado + "CLP) no coincidecon el total de la boleta (" + boleta.getMonto() + "CLP). El pago debe ser por el monto exacto");
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

        //Actualizar venta
        boletaClient.marcarComoPagada(folio);
    }
}
