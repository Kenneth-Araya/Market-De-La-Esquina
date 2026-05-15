package cl.duoc.sistemaMarket.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BoletaVentaDTO {
    private String folio;
    private BigDecimal monto;
    private String estado;
}
