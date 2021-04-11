package br.com.luan.pedidos.resources.exceptions;

import br.com.luan.pedidos.services.exceptions.AuthorizationException;
import br.com.luan.pedidos.services.exceptions.DataIntegrityException;
import br.com.luan.pedidos.services.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    //AQUI COMO INSTANCIAMOS UM OBJETO DO TIPO ValidationError, PODEMOS PERCORRER A LISTA DE ERROS GERADA NA
    //RESPOSTA PADRÃO DA EXCEPTION MethodArgumentNotValidException E CRIAR UMA LISTA PARA RETORNAR NA NOSSA
    //RESPOSTA PERSONALIZADA
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
        ValidationError err = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de Validação", System.currentTimeMillis());
        for(FieldError x : e.getBindingResult().getFieldErrors()) {
            err.addError(x.getField(), x.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    //para tratar exceção de usuario só poder consultar a si mesmo quando o usuario que consulta nao é ele mesmo ou não é admin
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<StandardError> authorization(AuthorizationException e, HttpServletRequest request) {

        StandardError err = new StandardError(HttpStatus.FORBIDDEN.value(), e.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }
}
