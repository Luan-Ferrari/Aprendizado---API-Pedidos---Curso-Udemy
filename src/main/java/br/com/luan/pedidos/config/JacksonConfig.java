package br.com.luan.pedidos.config;

import br.com.luan.pedidos.domain.PagamentoComBoleto;
import br.com.luan.pedidos.domain.PagamentoComCartao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {
    // https://stackoverflow.com/questions/41452598/overcome-can-not-construct-instance-ofinterfaceclass-without-hinting-the-pare

    @Bean //esse codigo é padrao, uma exigencia do Jackson
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder(){
            public void configure (ObjectMapper objectMapper) {
                objectMapper.registerSubtypes(PagamentoComBoleto.class);
                objectMapper.registerSubtypes(PagamentoComCartao.class); //isso aqui é a unica coisa que altera de um projeto para outro
                super.configure(objectMapper);
            }
        };
        return builder;
    }

}
