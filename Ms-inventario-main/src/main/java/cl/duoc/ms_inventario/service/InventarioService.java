package cl.duoc.ms_inventario.service;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.duoc.ms_inventario.dto.InventarioDTO;
import cl.duoc.ms_inventario.dto.InventarioMapper;
import cl.duoc.ms_inventario.model.Inventario;
import cl.duoc.ms_inventario.repository.InventarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class InventarioService {
    @Autowired
    private  InventarioRepository repo;

    @Autowired
    private  InventarioMapper mapper;

    public InventarioDTO validarObteniendoStock(Long idProducto){
        log.info("Iniciando validacion de stock para el producto: {}",idProducto);
        // 1. Buscamos la entidad en la base de datos
        Inventario inventario=repo.findById(idProducto)
                .orElseThrow(()-> new RuntimeException("El producto no existe en el inventario"));
        
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

        return dto;   
        }

    @Transactional
    public void descontarStock(Long idProducto, int cantidad){
        log.info("procesando salida de {} unidades para el producto {} ", cantidad, idProducto);

        Inventario inv=repo.findById(idProducto)
                .orElseThrow(()->new RuntimeException("NO SE PUEDE DESCONTAR: PRODUCTO NO ENCONTRADO"));
        if (inv.getStockActual()<cantidad){
            log.error("Falla en salida: Stock insuficiente para el producto {} ",idProducto);
            throw new RuntimeException("Stock insuficiente para realizar la venta");
        }
        inv.setStockActual(inv.getStockActual()-cantidad);
        repo.save(inv);
        log.info("Salida exitosa el nuevo stock es: {}",inv.getStockActual());

    }

    public List<InventarioDTO>listarVencidos(){
        log.info("generando reporte de los productos que ya estan vencidos");
        return repo.findAll().stream()
                .filter(i->i.getFechaVencimiento()!=null && i.getFechaVencimiento().isBefore(LocalDate.now()))
                .map(mapper::toDTO)
                .toList();
    }  
    
    public List<InventarioDTO>listarBajoStock(int limite){
        log.info("Buscando productos con un stock menor a {} ",limite);
        return repo.findAll().stream()
        .filter(i->i.getStockActual()<=limite)
        .map(mapper::toDTO)
        .toList();
        }
      
    
    /*POSTERIORMENTE CUANDO SE IMPLEMENTE EL MICROSERVICIO PROVEEDOR 
      SE AGREGARAN MAS METODOS RELEVANTES COMO AGREGAR STOCK */




}
