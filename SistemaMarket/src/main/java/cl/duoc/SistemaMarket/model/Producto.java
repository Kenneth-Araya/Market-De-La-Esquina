package cl.duoc.SistemaMarket.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name="producto")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Producto {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 13, unique=true)
    private String codigoBarraProducto;

    @Column(nullable = false, length = 50)
    private String nombreProducto;

    @Column(nullable = false, length = 100)
    private String descripcionProducto;

    @Column(nullable = false, length = 100)
    private double precioProductoProducto;

    @Column(nullable = false, length = 20, unique = true)
    private Long categoriaIdProducto;

}

