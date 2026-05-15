package cl.duoc.sistemaMarket.service;

import org.springframework.stereotype.Service;

import cl.duoc.sistemaMarket.exeptions.RecursoNoEncontradoException;
import cl.duoc.sistemaMarket.model.Proveedor;
import cl.duoc.sistemaMarket.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProveedorService {
    
    private final ProveedorRepository proveedorRepository;

    public Proveedor findByRutProveedor(String rutProveedor){
        if (rutProveedor == null || rutProveedor.isEmpty()) {
            throw new IllegalArgumentException("El RUT del proveedor no puede ser nulo o vacío");
        }
        return proveedorRepository.findByRut(rutProveedor).orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado"));
    }
}
