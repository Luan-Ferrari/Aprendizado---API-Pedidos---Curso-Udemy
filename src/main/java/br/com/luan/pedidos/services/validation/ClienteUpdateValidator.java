package br.com.luan.pedidos.services.validation;

import br.com.luan.pedidos.domain.Cliente;
import br.com.luan.pedidos.dto.ClienteDTO;
import br.com.luan.pedidos.repositories.ClienteRepository;
import br.com.luan.pedidos.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//VALIDAR REPETICAO DE EMAIL NA ATUALIZACAO DE CLIENTE.
//AQUI PRECISAMOS COLETAR A ID DO CLIENTE PARA VER SE O EMAIL ENCONTRADO NO BANCO NÃO É DO PROPRIO CLIENTE
// O ID DEVE SER COLETADO DA URI DA REQUISIÇÃO, POIS NO BODY NÃO É COLOCADO O ID, E NEM SE DEVE COLOCAR NESSE CASO
public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

    @Autowired
    private HttpServletRequest request; //objeto padrao do JavaWeb
    @Autowired
    private ClienteRepository repository;

    @Override
    public void initialize(ClienteUpdate ann) {
    }

    @Override
    public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context){

        //Esse Map abaixo é uma colegação do java.util, usamos ele porque ele faz um mapeamento entre
        //os atributos e seu valor, muito parecido com o arquivo json. Ali dentro vai ter a chave e o id
        //que foram passados na URI da requisição. O nome do método e do Enum é horrível, mas assim que
        //funciona. Aceita que dói menos!
        @SuppressWarnings("unchecked") //isso aqui é só pra nao ficar amarelo o casting abaixo.
        Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Integer uriId = Integer.parseInt(map.get("id"));
        //é feito, é horrível, mas é viável. Será que funciona para pegar argumento que vem depois do ? na URI?
        //o que ele fez foi buscar o 2 na uri http://localhost/aplicacao/cliente/2

        List<FieldMessage> list = new ArrayList<>();

        Cliente aux = repository.findByEmail(objDto.getEmail());
        if(aux != null && !aux.getId().equals(uriId)) { //aqui verifica se o Id é diferente para dar o erro
            list.add(new FieldMessage("email", "Email já cadastrado!"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    e.getMessage())
                    .addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
