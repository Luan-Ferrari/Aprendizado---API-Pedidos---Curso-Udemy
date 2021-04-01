package br.com.luan.pedidos.services.validation;

import br.com.luan.pedidos.domain.enums.TipoCliente;
import br.com.luan.pedidos.dto.ClienteNewDTO;
import br.com.luan.pedidos.resources.exceptions.FieldMessage;
import br.com.luan.pedidos.services.validation.utils.BR;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

//TODA A IMPLEMENTAÇAO DESSA CLASSE TAMBÉM É PADRÃO PARA USAR NO BEAN VALIDATION
//NO MATERIAL DE APOIO TEM UM CHECKLIST QUE DEVE SER SEGUIDO PARA CRIAR ESSAS VALIDACOES PERSONALIZADAS
public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

    @Override
    public void initialize(ClienteInsert ann){
    }

    @Override
    public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        //ABAIXO DAQUI VAI A LÓGICA DA VALIDAÇÃO
        if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCodigo()) && !BR.isValidSsn(objDto.getCpfOuCnpj())) {
            list.add(new FieldMessage("cpfOuCnpj", "CPF Inválido"));
        }
        if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCodigo()) && !BR.isValidTfn(objDto.getCpfOuCnpj())) {
            list.add(new FieldMessage("cpfOuCnpj", "CNPJ Inválido"));
        }
        //AQUI TERMINA A LÓGICA DA VALIDAÇÃO

        for (FieldMessage e: list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    e.getMessage())
                    .addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
//EXPLICAÇÃO DE COMO ISSO FUNCIONA
//A LÓGICA DE PROGRAMAÇÃO DEVE ESTAR DENTRO DO MÉTODO isValid, POIS É ESSE MÉTODO DA INTERFACE ConstraintValidator
//QUE VERIFICA SE OBJETO ClienteNewDTO vai ser valido ou não. Ele retorna true se for valido e false caso não
//seja valido. Esse retorno é percebido pelo @Valid, anotação usada nos métodos POST e PUT
//lá do ClienteResource (controller). Caso queira colocar alguma programação de inicialização também, usamos o método
//initialize. A Lógica é iniciar uma lista vazia do tipo FieldMessage (classe que criamos dentro do pacote .resources.exceptions
//E IREMOS ADIONANDO A ESTA LISTA NOVAS INSTANCIAS DE FieldMessage passando o tipo e a mensagem que queremos.
//O RETORNO VAI VERIFICA SER A LISTA ESTÁ VAZIA OU NÃO. O for QUE EXISTE ALI É PARA CONVERTER A LISTA DE FieldMessage
//QUE NÓS CRIAMOS PARA UMA LISTA DE ERROS QUE O FRAMEWORK USA. ISSO É NECESSÁRIO PORQUE A LISTA QUE CRIAMOS NÃO É A
//QUE O FRAMEWORK USA. MAS ISSO TUDO SEGUE UM PADRÃO. OS COMANDOS context FAZ ESSA CONVERSÃO. DEPOIS ESSA LISTA
//DE ERROS SERÁ TRATADA EM ResourceExceptionHandler. LÁ JÁ EXISTE O TRATAMENTO PARA MethodArgumentNotValidException, que
//usa um objeto do tipo ValidationError para isso.

