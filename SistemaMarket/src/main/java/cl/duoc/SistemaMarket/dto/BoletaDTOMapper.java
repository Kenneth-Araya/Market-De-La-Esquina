package cl.duoc.SistemaMarket.dto;

import org.springframework.stereotype.Component;
import cl.duoc.SistemaMarket.model.Boleta;
import cl.duoc.SistemaMarket.model.Proveedor;
import cl.duoc.SistemaMarket.service.ProveedorService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BoletaDTOMapper {
    
    private final ProveedorService proveedorService;

    public BoletaDTO toDTO(Boleta boleta){
        if (boleta == null) {
            return null;
        }

        BoletaDTO boletaDTO = new BoletaDTO();
        boletaDTO.setFolio(boleta.getFolioBoleta());
        boletaDTO.setRutProveedor(boleta.getProveedorBoleta().getRut());
        boletaDTO.setGlosa(boleta.getGlosaBoleta());
        boletaDTO.setTipo(boleta.getTipoBoleta());
        boletaDTO.setFecha(boleta.getFechaEmisionBoleta());
        boletaDTO.setMontoBruto(boleta.getMontoBrutoBoleta());
        boletaDTO.setMontoNeto(boleta.getMontoNetoBoleta());
        return boletaDTO;
    }

    public Boleta toModel(BoletaDTO boletaDTO){
        if (boletaDTO == null) {
            return null;
        }

        Proveedor proveedor = proveedorService.findByRutProveedor(boletaDTO.getRutProveedor());

        Boleta boleta = new Boleta();
        boleta.setFolioBoleta(boletaDTO.getFolio());
        boleta.setProveedorBoleta(proveedor);
        boleta.setGlosaBoleta(boletaDTO.getGlosa());
        boleta.setTipoBoleta(boletaDTO.getTipo());
        boleta.setFechaEmisionBoleta(boletaDTO.getFecha());
        boleta.setMontoBrutoBoleta(boletaDTO.getMontoBruto());
        boleta.setMontoNetoBoleta(boletaDTO.getMontoNeto());
        boleta.setEstadoBoleta("PENDIENTE");
        return boleta;
    }
}

