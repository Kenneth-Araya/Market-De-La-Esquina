package cl.duoc.SistemaMarket.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.duoc.SistemaMarket.dto.ProductoDTO;
import cl.duoc.SistemaMarket.dto.ProductoDTOMapper;
import cl.duoc.SistemaMarket.exception.RecursoNoEncontradoException;
import cl.duoc.SistemaMarket.model.Producto;
import cl.duoc.SistemaMarket.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    @Autowired
    private ProductoRepository repo;

    //Listar producto
    public List<ProductoDTO> listarProductos(){
        return repo.findAll()
        .stream()
        .map(ProductoDTOMapper::toDto)
        .toList();
    }

    //Guardar producto
    public ProductoDTO guardarProducto(ProductoDTO dto){
        Producto producto =ProductoDTOMapper.toEntity(dto);
        Producto guardado=repo.save(producto);
        return ProductoDTOMapper.toDto(guardado);
    }

    //Eliminar producto
    public void eliminarProducto(Long id){
        repo.deleteById(id);
    }

    //Buscar producto por id 
    public ProductoDTO buscarProducto(Long id){
        Optional<Producto> resultado = repo.findById(id);
        if (resultado.isPresent()) {
            return ProductoDTOMapper.toDto(resultado.get());
        }
        throw new RecursoNoEncontradoException("no existe el producto con Id: "+id);
    }

    //Actualizar producto
    public ProductoDTO actualizarProducto(Long id,ProductoDTO dto){
        Optional<Producto>resultado=repo.findById(id);
        if (resultado.isPresent()) {
            Producto producto=resultado.get();
            producto.setCodigoBarra(dto.getCodigoBarra());
            producto.setNombre(dto.getNombre());
            producto.setDescripcion(dto.getDescripcion());
            producto.setPrecioProducto(dto.getPrecioProducto());
            producto.setCategoriaId(dto.getCategoriaId());

            Producto actualizado=repo.save(producto);

            return ProductoDTOMapper.toDto(actualizado);
        }
        throw new RecursoNoEncontradoException("no existe el producto con Id: "+id);
    }
    
}
