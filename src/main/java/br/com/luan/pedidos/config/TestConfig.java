package br.com.luan.pedidos.config;

import br.com.luan.pedidos.services.DBService;
import br.com.luan.pedidos.services.EmailService;
import br.com.luan.pedidos.services.MockEmailService;
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

    //esse Bean funciona assim: quando anotamos com @Bean, esse método se torna um componente do sistema
    //então, quando injetamos alguma coisa relacionada a emailService em outra classe, o Spring vem aqui
    // buscar esse método para usá-lo, neste caso, esse método foi criado na classe TestConfig, pois assim
    // ele só será chamado se estivermos no profile de test.

    @Bean
    public EmailService emailService() {
        return new MockEmailService();
    }
}
