package cl.duoc.SistemaMarket.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import cl.duoc.SistemaMarket.dto.PagoDTO;
import cl.duoc.SistemaMarket.dto.PagoDTOMapper;
import cl.duoc.SistemaMarket.model.Pago;
import cl.duoc.SistemaMarket.repository.PagoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PagoService {
    
    private final PagoRepository pagoRepository;

    public List<PagoDTO> obtenerTodosLosPagos(){
        
        List<Pago> pagos = pagoRepository.findAll();
        List<PagoDTO> pagoDTOs = new ArrayList<>();

        if (!pagos.isEmpty()) {
            for (Pago pago : pagos) {
                pagoDTOs.add(PagoDTOMapper.toDTO(pago));
            }
        }
        return pagoDTOs;
    }
}
