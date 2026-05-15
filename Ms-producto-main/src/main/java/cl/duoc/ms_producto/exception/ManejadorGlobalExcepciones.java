package cl.duoc.ms_producto.exception;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(ResourceNotFoundException.class)

   public ResponseEntity<ErrorDetalles> handleRecursoNoEncontrado(ResourceNotFoundException e, WebRequest request) {
        ErrorDetalles error=new ErrorDetalles(
            LocalDateTime.now(),
            e.getMessage(),
            request.getDescription(false),
            HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
