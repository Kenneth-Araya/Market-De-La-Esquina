package cl.duoc.ms_inventario.service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cl.duoc.ms_inventario.clients.ProductoClient;
import cl.duoc.ms_inventario.dto.InventarioDTO;
import cl.duoc.ms_inventario.dto.InventarioMapper;
import cl.duoc.ms_inventario.dto.ProductoResponseDTO;
import cl.duoc.ms_inventario.exception.RecursoNoEncontradoException;
import cl.duoc.ms_inventario.model.Inventario;
import cl.duoc.ms_inventario.repository.InventarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class InventarioService {
    private final InventarioRepository repo;
    private final InventarioMapper mapper;
    private final ProductoClient productoClient;

    public InventarioDTO validarObteniendoStock(Long idProducto){
        log.info("Iniciando validacion de stock para el producto: {}",idProducto);
        // 1. Buscamos la entidad en la base de datos
        Inventario inventario=repo.findByIdProducto(idProducto)
                .orElseThrow(()-> new RecursoNoEncontradoException("El producto no existe en el inventario"));
        
        // 2. Usamos el Mapper para convertir la Entidad a DTO
        InventarioDTO dto=mapper.toDTO(inventario);

        // 3. Aplicamos la Lógica de Negocio (Estado del producto)
        if (inventario.getFechaVencimiento() != null && inventario.getFechaVencimiento().isBefore(LocalDate.now())) {
            dto.setEstadoProducto("BLOQUEADO - VENCIDO");
            log.warn("Producto ID {} está vencido. Fecha: {}", idProducto, inventario.getFechaVencimiento());
        }else if(inventario.getStockActual()<=0){
            dto.setEstadoProducto("SIN STOCK");
        }else{
            dto.setEstadoProducto("DISPONIBLE");
        }

        try{
            log.info("Cruzando puente Feign hacia ms-producto para el id: {}",idProducto);
            ProductoResponseDTO productoDto=productoClient.obtenerProductoPorId(idProducto);
            if (productoDto!=null) {
                log.info("Feign funciono correctamente Seteando nombre del producto en el DTO");
                dto.setNombreProducto(productoDto.getNombre());
            }
        }catch (Exception e){
            log.error("No se pudo conectar con ms-producto a través de Feign: {}", e.getMessage());
        }

        return dto;

        }

    @Transactional
    public void descontarStock(Long idProducto, int cantidad){
        log.info("procesando salida de {} unidades para el producto {} ", cantidad, idProducto);

        Inventario inv=repo.findByIdProducto(idProducto)
                .orElseThrow(()->new RecursoNoEncontradoException("NO SE PUEDE DESCONTAR: PRODUCTO NO ENCONTRADO"));
        if (inv.getStockActual()<cantidad){
            log.error("Falla en salida: Stock insuficiente para el producto {} ",idProducto);
            throw new RuntimeException("Stock insuficiente para realizar la venta");
        }
        inv.setStockActual(inv.getStockActual()-cantidad);
        repo.save(inv);
        log.info("Salida exitosa el nuevo stock es: {}",inv.getStockActual());

    }

    
    public List<InventarioDTO>listarProductosBajoStock(int limite){
        log.info("Buscando productos con un stock menor a {} ",limite);
        // 1. Traemos todo y filtramos en memoria por el límite dinámico
        List<InventarioDTO> listaDtos = repo.findAll().stream()
                .filter(i -> i.getStockActual() <= limite)
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        listaDtos.forEach(dto -> {
            try {
                ProductoResponseDTO productoDto = productoClient.obtenerProductoPorId(dto.getIdProducto());
                if (productoDto != null) {
                    dto.setNombreProducto(productoDto.getNombre());
                }
            } catch (Exception e) {
                log.error("Error Feign al traer nombre para el producto ID {}: {}", dto.getIdProducto(), e.getMessage());
                dto.setNombreProducto("Nombre no disponible");
            }
        });

        return listaDtos;
    }
        
      
    
    /*POSTERIORMENTE CUANDO SE IMPLEMENTE EL MICROSERVICIO PROVEEDOR 
      SE AGREGARAN MAS METODOS RELEVANTES COMO AGREGAR STOCK */




}
