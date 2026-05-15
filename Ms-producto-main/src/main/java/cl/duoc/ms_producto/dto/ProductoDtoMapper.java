package cl.duoc.ms_producto.dto;

import cl.duoc.ms_producto.model.Producto;

public class ProductoDtoMapper {

    public static Producto toEntity(ProductoDto dto){
        if(dto==null){
            return null;
        }
        
        Producto producto=new Producto();
        producto.setCodigoBarra(dto.getCodigoBarra());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioProducto(dto.getPrecioProducto());
        producto.setCategoriaId(dto.getCategoriaId());
        return producto;

    }

    public static ProductoDto toDto(Producto prod){
        if(prod==null){
            return null;
        }

        ProductoDto dto=new ProductoDto();
        dto.setCodigoBarra(prod.getCodigoBarra());
        dto.setNombre(prod.getNombre());
        dto.setDescripcion(prod.getDescripcion());
        dto.setPrecioProducto(prod.getPrecioProducto());
        dto.setCategoriaId(prod.getCategoriaId());

        return dto;
    }


}
