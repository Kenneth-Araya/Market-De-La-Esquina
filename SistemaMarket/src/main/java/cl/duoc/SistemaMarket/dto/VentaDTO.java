package cl.duoc.SistemaMarket.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaDTO {

    @NotNull(message = "La fecha de venta es obligatoria")
    private LocalDateTime fechaVentaDto;

    @NotNull(message = "El total es obligatorio")
    @Positive(message = "El total debe ser mayor a 0")
    private Double totalVentaDto;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcionVentaDto;

    @NotNull(message = "El monto de pago es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double montoPagoVentaDto;

    @NotBlank(message = "El método de pago es obligatorio")
    @Pattern(regexp = "EFECTIVO|DEBITO|CREDITO", message = "El método debe ser EFECTIVO, DEBITO o CREDITO")
    private String metodoPagoVentaDto;

    @NotBlank(message = "El estado de pago es obligatorio")
    @Pattern(regexp = "PENDIENTE|COMPLETADO|ANULADO", message = "El estado debe ser PENDIENTE, COMPLETADO o ANULADO")
    private String estadoPagoVentaDto;

    @NotBlank(message = "El código de transacción es obligatorio")
    private String codigoTransaccionVentaDto;
}
