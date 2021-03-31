package br.com.luan.pedidos.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

//ESSA CLASSE É UMA ESPECIALIZAÇÃO DA CLASSE StandadError, OU SEJA, POSSUI OS ATRIBUTOS QUE JÁ EXISTEM NELA
//MAIS UMA LISTA DE ERROS DE VALIDAÇÃO. ESSA LISTA É GERADA NA RESPOSTA PADRÃO DA MethodArgumentNotValidException,
//ENTÃO CRIAMOS ESSA CLASSE E USAMOS ELA NA CLASSE ResourceExceptionHandler.
public class ValidationError extends StandardError{
    private static final long serialVersionUID = 1L;

    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(Integer status, String msg, Long timeStamp) {
        super(status, msg, timeStamp);
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }
}
