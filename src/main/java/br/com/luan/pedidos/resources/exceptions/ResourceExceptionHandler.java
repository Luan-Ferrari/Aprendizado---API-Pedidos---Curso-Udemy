package br.com.luan.pedidos.resources.exceptions;

import br.com.luan.pedidos.services.exceptions.DataIntegrityException;
import br.com.luan.pedidos.services.exceptions.ObjectNotFoundException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

// SERVE PARA CAPTURAR TODAS AS EXCEPTIONS E TRATA-LÁS AQUI,
// POIS NÃO DEVEMOS TRATAR EXCEPTIONS COM TRY/CATCH NO CONTROLLER(RESOURCE)

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request){
        //ESSA ASSINATURA É PADRÃO DO @ControllerAdvice, TEM QUE SER ASSIM, NÃO É NADA INVENTADO AQUI.

        StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<StandardError> dataIntegrityException(DataIntegrityException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}
