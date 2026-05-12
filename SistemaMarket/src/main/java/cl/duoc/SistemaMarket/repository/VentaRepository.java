package cl.duoc.SistemaMarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.duoc.SistemaMarket.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer>{
    
    Venta findById(Long idVenta);

    void deleteById(Long idVenta);
}
