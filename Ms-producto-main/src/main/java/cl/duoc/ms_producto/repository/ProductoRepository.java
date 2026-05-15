package cl.duoc.ms_producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.ms_producto.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>  {

}
