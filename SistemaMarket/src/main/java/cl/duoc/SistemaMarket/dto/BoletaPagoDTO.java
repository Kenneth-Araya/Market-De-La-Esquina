package cl.duoc.SistemaMarket.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BoletaPagoDTO {
    
    private String folio;
    private BigDecimal monto;
    private String estado;
}

