package cl.duoc.ms_producto.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import cl.duoc.ms_producto.dto.ProductoDto;
import cl.duoc.ms_producto.dto.ProductoDtoMapper;
import cl.duoc.ms_producto.exception.ResourceNotFoundException;
import cl.duoc.ms_producto.model.Producto;
import cl.duoc.ms_producto.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository repo;

    //LISTAR PRODUCTOS
    public List<ProductoDto> listarProductos(){
        return repo.findAll()
        .stream()
        .map(ProductoDtoMapper::toDto)
        .toList();
    }

    //AGREGAR PRODUCTO
    public ProductoDto guardarProducto(ProductoDto dto){
        Producto entidad=ProductoDtoMapper.toEntity(dto);
        Producto guardado=repo.save(entidad);
        return ProductoDtoMapper.toDto(guardado);
    }

    //ELIMINAR PRODUCTO 
    public boolean eliminarProducto(Long id){
        if (repo.existsById(id)){
            repo.deleteById(id); 
            return true;    
        }
        return false;
    }

    //BUSCAR PRODUCTO POR ID
    public ProductoDto buscarProducto(Long id){
        Optional<Producto> resultado = repo.findById(id);
        if (resultado.isPresent()) {
            return ProductoDtoMapper.toDto(resultado.get());
        }
        throw new ResourceNotFoundException("no existe el producto con Id: "+id);
    }

    //ACTUALIZAR PRODUCTO
    public ProductoDto actualizarProducto(Long id,ProductoDto dto){
        Optional<Producto>resultado=repo.findById(id);
        if (resultado.isPresent()) {
            Producto producto=resultado.get();
            producto.setCodigoBarra(dto.getCodigoBarra());
            producto.setNombre(dto.getNombre());
            producto.setDescripcion(dto.getDescripcion());
            producto.setPrecioProducto(dto.getPrecioProducto());
            producto.setCategoriaId(dto.getCategoriaId());

            Producto actualizado=repo.save(producto);

            return ProductoDtoMapper.toDto(actualizado);
        }
        return null;
    }
    
}
