package cl.duoc.SistemaMarket.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import cl.duoc.SistemaMarket.client.BoletaClient;
import cl.duoc.SistemaMarket.dto.BoletaPagoDTO;
import cl.duoc.SistemaMarket.model.Pago;
import cl.duoc.SistemaMarket.repository.PagoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoletaPagoService {

    private final BoletaClient boletaClient;
    private final PagoRepository pagoRepository;

    public BoletaPagoDTO obtenerBoleta(String folio) {
        return boletaClient.obtenerBoleta(folio);
    }

    public void autorizarPago(String folio) {
        
        //Obtener boleta
        BoletaPagoDTO boleta = boletaClient.obtenerBoleta(folio);

        if (boleta == null) {
            throw new RuntimeException("Boleta no encontrada");
        }

        //Validar estado
        if (!"PENDIENTE".equals(boleta.getEstado())) {
            throw new RuntimeException("Boleta ya pagada");
        }

        //Registrar pago
        Pago pago = new Pago();
        pago.setCodigoTransaccionPago("TX-" + folio);
        pago.setMontoPago(boleta.getMonto().doubleValue());
        pago.setMetodoPago("EFECTIVO");
        pago.setEstadoPago("PAGADO");
        pago.setFechaPago(LocalDateTime.now());
        pago.setFechaCreacionPago(LocalDateTime.now());
        pago.setMonedaPago("CLP");
        pago.setDescripcionPago("Pago autorizado para boleta " + folio);

        pagoRepository.save(pago);

        //Actualizar boleta en otro MS
        boletaClient.marcarComoPagada(folio);
    }
}
