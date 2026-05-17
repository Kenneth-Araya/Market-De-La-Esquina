package cl.duoc.msVenta.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.duoc.msVenta.dto.VentaDTO;
import cl.duoc.msVenta.dto.VentaDTOMapper;
import cl.duoc.msVenta.model.Venta;
import cl.duoc.msVenta.repository.VentaRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VentaService {
    
    @Autowired
    private VentaRepository ventaRepository;

    //Listar ventas
    public List<VentaDTO> listarTodos(){
        log.info("Iniciando consulta para listar todas las ventas");

        List<Venta> ventas = ventaRepository.findAll();
        List<VentaDTO> ventaDTOs = new ArrayList<>();

        if (!ventas.isEmpty()) {
            for (Venta venta : ventas) {
                ventaDTOs.add(VentaDTOMapper.toDto(venta));
            }
            log.info("Se encontraron {} venta(s)", ventas.size());
        } else{
            log.warn("No se encontraron ventas en el sistema");
        }
    }

    //Guardar ventas
    public VentaDTO guardarVenta(VentaDTO ventaDTO){
        
        log.info("Iniciando proceso de guardado de venta");

        //regla de negocio: debe tener producto
        if (ventaDTo.getProductos() == null || ventaDTO.getProductos().isEmpty()) {
            log.error("Validación fallida: la venta con codigo transaccion venta {} no tiene productos asosciados", ventaDTO.getCodigoTransaccionVentaDto());
            throw new IllegalArgumentException("No se puede generar venta sin producto");
        }

        //regla de negocio: no puede ser nula 
        if (ventaDTO == null || ventaDTO.getCodigoTransaccionVentaDto() == null || ventaDTO.getCodigoTransaccionVentaDto().isEmpty()) {
            log.error("Validación fallida: la venta o codigo de transaccion son nulos o vacíos");
            throw IllegalArgumentException("La venta o su codigo de transaccion no pueden ser nulos o vacios");
        }

        Venta venta = VentaDTOMapper.toEntity(ventaDTO);
        Venta guardado = ventaRepository.save(venta);

        if (guardado) {
            log.info("Venta con codigo de transaccion {} guarda exitosamente", ventaDTO.getCodigoTransaccionVentaDto());
        }else{
            log.warn("No se pudo guardar la venta con codigo de transaccion {}", ventaDTO.getCodigoTransaccionVentaDto());
        }

        return VentaDTOMapper.toDto(guardado);
    }

    //Eliminar ventas
    public boolean eliminarPorId(Long idVenta){
        log.info("Iniciando proceso de eliminación para la venta con id: {}", idVenta);
        
        Venta ventaExistente = ventaRepository.findByIdVenta(idVenta);

        if (ventaExistente == null || !ventaExistente.getIdVenta().equals(idVenta)) {
            log.error("No se encontró venta con id {} para eliminar", idVenta);
            throw new RecursoNoEncontradoException("id incorrecto.");
        }

        ventaRepository.deleteByIdVenta(idVenta);
        log.info("Venta con id {} eliminada exitosamente", idVenta);
        return true;

    }

    //Actualizar ventas
    public VentaDTO actualizarVenta(Long id, VentaDTO ventaDTO){
        log.info("Iniciando proceso de actualización para venta con codigo de transaccion");

        Venta ventaExistente = ventaRepository.findByIdVenta(id);

        //regla de negocio: debe tener producto
        if (ventaDTo.getProductos() == null || ventaDTO.getProductos().isEmpty()) {
            log.error("Validación fallida: la venta con codigo transaccion venta {} no tiene productos asosciados", ventaDTO.getCodigoTransaccionVentaDto());
            throw new IllegalArgumentException("No se puede generar venta sin producto");
        }

        //regla de negocio: no puede ser nula 
        if (ventaDTO == null || ventaDTO.getCodigoTransaccionVentaDto() == null || ventaDTO.getCodigoTransaccionVentaDto().isEmpty()) {
            log.error("Validación fallida: la venta o codigo de transaccion son nulos o vacíos");
            throw IllegalArgumentException("La venta o su codigo de transaccion no pueden ser nulos o vacios");
        }

        Venta ventaActualizada = VentaDTOMapper.toEntity(ventaDTO);
        ventaActualizada.setIdVenta(ventaExistente.getIdVenta());
        boolean actualizado = ventaRepository.save(ventaActualizada) != null;

        if (guardado) {
            log.info("Venta con codigo de transaccion {} guarda exitosamente", ventaDTO.getCodigoTransaccionVentaDto());
        }else{
            log.warn("No se pudo guardar la venta con codigo de transaccion {}", ventaDTO.getCodigoTransaccionVentaDto());
        }

        return actualizado;
        
    }

    //buscar venta por id 
    public VentaDTO findById(Long id){
        Venta venta = ventaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
        return VentaDTOMapper.toDto(venta);
    }
}
