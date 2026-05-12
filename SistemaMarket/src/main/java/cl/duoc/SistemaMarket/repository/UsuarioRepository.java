package cl.duoc.SistemaMarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.duoc.SistemaMarket.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findById(Long id);

    void deleteByIdById(Long id);
}
