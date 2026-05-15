package cl.duoc.sistemaMarket.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.duoc.sistemaMarket.dto.VentaDTO;
import cl.duoc.sistemaMarket.dto.VentaDTOMapper;
import cl.duoc.sistemaMarket.model.Venta;
import cl.duoc.sistemaMarket.repository.VentaRepository;

@Service
public class VentaService {
    
    @Autowired
    private VentaRepository ventaRepository;

    //Listar ventas
    public List<VentaDTO> listarTodos(){
        return ventaRepository.findAll()
        .stream()
        .map(VentaDTOMapper::toDto)
        .toList();
    }

    //Guardar ventas
    public VentaDTO guardarVenta(VentaDTO dto){
        
        Venta venta = VentaDTOMapper.toEntity(dto);
        Venta guardado = ventaRepository.save(venta);
        return VentaDTOMapper.toDto(guardado);
    }

    //Eliminar ventas
    public void eliminarPorId(Long idVenta){
        ventaRepository.deleteById(idVenta);
    }

    //Actualizar ventas
    public VentaDTO actualizarVenta(Long id, VentaDTO dto){

        Venta venta = ventaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));

        venta.setFechaVenta(dto.getFechaVentaDto());
        venta.setDescripcionVenta(dto.getDescripcionVentaDto());
        venta.setMetodoVenta(dto.getMetodoPagoVentaDto());
        venta.setTotalVenta(dto.getTotalVentaDto());

        Venta actualizado = ventaRepository.save(venta);
        return VentaDTOMapper.toDto(actualizado);
        
    }

    //buscar venta por id 
    public VentaDTO findById(Long id){
        Venta venta = ventaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
        return VentaDTOMapper.toDto(venta);
    }
}
