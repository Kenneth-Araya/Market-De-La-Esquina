package cl.duoc.ms_producto.exception;

import java.time.LocalDateTime;

public record ErrorDetalles(
    LocalDateTime timestamp,
    String mensaje,
    String detalle,
    int codigo
){}

