package cl.duoc.sistemaMarket.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.duoc.sistemaMarket.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    
    Optional<Venta> findByIdVenta(Long idVenta);

    void deleteByIdVenta(Long idVenta);
}