package cl.duoc.SistemaMarket.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    @Column(nullable = false, length = 100)
    private Double monto;

    @Column(nullable = false, length = 100)
    private String metodoPago;

    @Column(nullable = false, length = 100)
    private String estado;

    @Column(nullable = false, length = 100)
    private String codigoTransaccion;

    @Column(nullable = false, length = 100)
    private LocalDateTime fechaPago;

    @Column(nullable = false, length = 100)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false, length = 100)
    private String moneda;

    @Column(nullable = false, length = 100)
    private String descripcion;
    
}
