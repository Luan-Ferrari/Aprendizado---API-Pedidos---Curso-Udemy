package br.com.luan.pedidos.config;

import br.com.luan.pedidos.services.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

//essa classe só vai rodar se for configura o application.properties como "test"
@Configuration
@Profile("test")
public class TestConfig {

    @Autowired
    private DBService dbService;

    @Bean
    public boolean instantiateDatebase() throws ParseException {
        dbService.instanciateTestDatabase();
        return true;
    }
}
