package cl.duoc.SistemaMarket.dto;

import cl.duoc.SistemaMarket.model.Pago;

public class PagoDTOMapper {

    public static PagoDTO toDTO(Pago pago){
        if (pago == null) {
            return null;
        }
        PagoDTO pagoDTO = new PagoDTO();
        pagoDTO.setCodigoTransaccionPago(pago.getCodigoTransaccionPago());
        pagoDTO.setFechaCreacionPago(pago.getFechaCreacionPago());
        pagoDTO.setDescripcionPago(pago.getDescripcionPago());
        pagoDTO.setMontoPago(pago.getMontoPago());
        pagoDTO.setMetodoPago(pago.getMetodoPago());
        pagoDTO.setFechaPago(pago.getFechaPago());
        return pagoDTO;
    }

    public static Pago toModel(PagoDTO pagoDTO){
        if (pagoDTO == null) {
            return null;
        }
        Pago pago = new Pago();
        pago.setCodigoTransaccionPago(pagoDTO.getCodigoTransaccionPago());
        pago.setFechaCreacionPago(pagoDTO.getFechaCreacionPago());
        pago.setDescripcionPago(pagoDTO.getDescripcionPago());
        pago.setMontoPago(pagoDTO.getMontoPago());
        pago.setMetodoPago(pagoDTO.getMetodoPago());
        pago.setFechaPago(pagoDTO.getFechaPago());
        return pago;
    }
}
