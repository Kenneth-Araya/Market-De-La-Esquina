package cl.duoc.SistemaMarket.service;

import org.springframework.stereotype.Service;
import cl.duoc.SistemaMarket.model.Pago;
import cl.duoc.SistemaMarket.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
//Falta unir vboleta con pago

@Service
@RequiredArgsConstructor
public class BoletaPagoService {

    private final BoletaClient boletaClient;
    private final PagoRepository pagoRepository;

    public BoletaDTO obtenerBoleta(String folio) {
        return boletaClient.obtenerBoleta(folio);
    }

    public void autorizarPago(String folio) {
        //Obtener boleta
        BoletaDTO boleta = boletaClient.obtenerBoleta(folio);

        if (boleta == null) {
            throw new RuntimeException("Boleta no encontrada");
        }

        //Validar estado
        if (!"PENDIENTE".equals(boleta.getEstado())) {
            throw new RuntimeException("Boleta ya pagada");
        }

        //Registrar pago
        Pago pago = new Pago();
        pago.setFolioBoleta(folio);
        pago.setMonto(boleta.getMonto());

        pagoRepository.save(pago);

        //Actualizar boleta en otro MS
        boletaClient.marcarComoPagada(folio);
    }
}
