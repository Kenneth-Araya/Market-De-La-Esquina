package cl.duoc.sistemaMarket.dto;

import cl.duoc.sistemaMarket.model.Venta;

public class VentaDTOMapper {

    public static Venta toEntity(VentaDTO ventaDTO){
        if (ventaDTO == null) return null;
        Venta venta = new Venta();
        venta.setFechaVenta(ventaDTO.getFechaVentaDto());
        venta.setTotalVenta(ventaDTO.getTotalVentaDto());
        venta.setDescripcionVenta(ventaDTO.getDescripcionVentaDto());
        venta.setMontoVenta(ventaDTO.getMontoPagoVentaDto());
        venta.setMetodoVenta(ventaDTO.getMetodoPagoVentaDto());
        venta.setEstadoVenta(ventaDTO.getEstadoPagoVentaDto());
        venta.setCodigoTransaccionVenta(ventaDTO.getCodigoTransaccionVentaDto());
        return venta;
    }

    public static VentaDTO toDto(Venta venta){
        if (venta == null) {
            return null;
        }
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setFechaVentaDto(venta.getFechaVenta());
        ventaDTO.setTotalVentaDto(venta.getTotalVenta());
        ventaDTO.setDescripcionVentaDto(venta.getDescripcionVenta());
        ventaDTO.setMontoPagoVentaDto(venta.getMontoVenta());
        ventaDTO.setMetodoPagoVentaDto(venta.getMetodoVenta());
        ventaDTO.setEstadoPagoVentaDto(venta.getEstadoVenta());
        ventaDTO.setCodigoTransaccionVentaDto(venta.getCodigoTransaccionVenta());
        return ventaDTO;
    }
}
