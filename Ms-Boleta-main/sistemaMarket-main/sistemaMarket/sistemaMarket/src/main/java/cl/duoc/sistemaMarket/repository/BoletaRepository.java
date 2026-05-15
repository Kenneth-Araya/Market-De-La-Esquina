package cl.duoc.sistemaMarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.duoc.sistemaMarket.model.Boleta;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {

    Boleta findByFolio(String folio);

    void deleteByFolio(String folio);
}
