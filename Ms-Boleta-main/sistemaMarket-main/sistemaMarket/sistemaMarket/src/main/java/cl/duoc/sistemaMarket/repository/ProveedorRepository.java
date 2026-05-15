package cl.duoc.sistemaMarket.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.duoc.sistemaMarket.model.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long>{
    
    Optional<Proveedor> findByRut(String rutProveedor);
}
