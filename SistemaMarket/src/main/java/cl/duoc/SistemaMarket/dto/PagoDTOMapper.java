package cl.duoc.SistemaMarket.dto;

import cl.duoc.SistemaMarket.model.Pago;

public class PagoDTOMapper {

    public static PagoDTO toDTO(Pago pago){
        if (pago == null) {
            return null;
        }

        PagoDTO pagoDTO = new PagoDTO();
        pagoDTO.setCodigoTransaccion(pago.getCodigoTransaccion());
        pagoDTO.setFechaCreacion(pago.getFechaCreacion());
        pagoDTO.setDescripcion(pago.getDescripcion());
        pagoDTO.setMonto(pago.getMonto());
        pagoDTO.setMetodoPago(pago.getMetodoPago());
        pagoDTO.setFechaPago(pago.getFechaPago());
        return pagoDTO;
    }

    public static Pago toModel(PagoDTO pagoDTO){
        if (pagoDTO == null) {
            return null;
        }

        Pago pago = new Pago();
        pago.setCodigoTransaccion(pagoDTO.getCodigoTransaccion());
        pago.setFechaCreacion(pagoDTO.getFechaCreacion());
        pago.setDescripcion(pagoDTO.getDescripcion());
        pago.setMonto(pagoDTO.getMonto());
        pago.setMetodoPago(pagoDTO.getMetodoPago());
        pago.setFechaPago(pagoDTO.getFechaPago());
        return pago;
    }
}
