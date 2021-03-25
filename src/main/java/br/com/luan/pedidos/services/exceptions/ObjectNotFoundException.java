package br.com.luan.pedidos.services.exceptions;

public class ObjectNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(String msg) {
        super(msg);
    }

    public ObjectNotFoundException(String msg, Throwable cause) {
        super(msg, cause); //esse construtor recebe alem da msg a causa de outra exceção que ocorreu antes.
    }
}
