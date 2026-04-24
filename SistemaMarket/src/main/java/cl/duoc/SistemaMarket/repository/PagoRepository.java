package cl.duoc.SistemaMarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.duoc.SistemaMarket.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long>{
    
    Pago findBycodigoTransaccion(String codigoTransaccion);

    void dedeleteBycodigoTransaccion(String codigoTransaccion);
}
