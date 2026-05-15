package cl.duoc.sistemaMarket.model;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "proveedor")
public class Proveedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = 9)
    private String rut;

    @Column(nullable = false, unique = true, length = 9)
    private String nombre;

    @Column(nullable = false, unique = true, length = 9)
    private String giro;

    @Column(nullable = false, unique = true, length = 9)
    private String direccion;

    @Column(nullable = false, unique = true, length = 9)
    private String telefono;

    @OneToMany(mappedBy = "proveedor")
    private List<Boleta> boletas;

    
}
