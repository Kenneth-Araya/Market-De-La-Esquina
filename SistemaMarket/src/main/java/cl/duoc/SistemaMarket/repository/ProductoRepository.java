package cl.duoc.SistemaMarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.duoc.SistemaMarket.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>  {
    Producto findByIdProducto(Long id);

    void deleteByIdByIdProducto(Long id);
}

