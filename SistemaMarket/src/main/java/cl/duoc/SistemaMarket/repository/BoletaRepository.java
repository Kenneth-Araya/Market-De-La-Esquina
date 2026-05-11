package cl.duoc.SistemaMarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.duoc.SistemaMarket.model.Boleta;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {

    Boleta findByFolioBoleta(String folio);

    void deleteByFolioBoleta(String folio);
}
