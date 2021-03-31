package br.com.luan.pedidos.resources.exceptions;

import java.io.Serializable;

//METODO AUXILIAR PARA GERAR ERROS DE VALIDAÇÃO, É USADO PARA GERAR UMA LISTA COM O CAMPO E A MESSAGE DE ERRO
//GERADAS NA VALIDACAO DOS ATRIBUTOS INSERIDOS. ESSA LISTA É GERADA DENTRO DA CLASSE ValidationError.
public class FieldMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fieldName;
    private String message;

    public FieldMessage() {
    }

    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
