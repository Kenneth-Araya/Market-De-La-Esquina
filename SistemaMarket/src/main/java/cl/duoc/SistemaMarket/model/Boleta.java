package cl.duoc.SistemaMarket.model;

import java.time.LocalDate;
import cl.duoc.SistemaMarket.service.ProveedorService;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "boleta")

public class Boleta {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String folioBoleta;

    @Column(nullable = false, length = 100)
    private String glosaBoleta;

    @Column(nullable = false, length = 100)
    private String tipoBoleta;

    @Column(nullable = false, length = 100)
    private LocalDate fechaEmisionBoleta;

    @Column(nullable = false, length = 100)
    private int montoBrutoBoleta;

    @Column(nullable = false, length = 100)
    private int montoNetoBoleta;

    @Column(nullable = false, length = 100)
    private String estadoBoleta;

    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedorBoleta;
}