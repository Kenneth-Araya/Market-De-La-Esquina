package cl.duoc.SistemaMarket.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoDTO {
    @NotNull(message = "El codigo transaccion es obligatorio")
    private String codigoTransaccionPago;

    @NotNull(message = "El monto es obligatorio")
    private Double montoPago;

    @NotNull(message = "El metodo de pago es obligatorio")
    private String metodoPago;

    @NotBlank(message = "La fecha de pago es obligatoria")
    private LocalDateTime fechaPago;

    @NotNull(message = "La fecha de creacion es obligatoria")
    private LocalDateTime fechaCreacionPago;

    @Min(value = 1, message = "La descripcion es obligatoria")
    private String descripcionPago;
}
