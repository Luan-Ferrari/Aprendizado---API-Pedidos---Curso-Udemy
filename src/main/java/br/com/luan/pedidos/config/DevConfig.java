package br.com.luan.pedidos.config;

import br.com.luan.pedidos.services.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile("dev")
public class DevConfig {

    @Autowired
    private DBService dbService;

    //aqui é coletado o valor da propriedade abaixo, o objetivo é verificar se ela é create ou não. Caso seja create,
    //dentro do método instantiateDatebase logo abaixo, será rodado o dbService.instanciateTestDatabase para popular
    //o banco de dados. Caso tenha outro valor qualquer que não seja create, será retornado false e não sera populado
    //nada no banco de dados.
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String strategy;

    @Bean
    public boolean instantiateDatabase() throws ParseException{

        if (!"create".equals(strategy)) { //aqui usamos a negacao ! pois se a comparacao for verdadeira então o retorno será falso
            return false;
        }

        dbService.instanciateTestDatabase();
        return true;
    }
}
