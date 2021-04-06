package br.com.luan.pedidos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //esse Environment precisa para liberar o acesso ao banco H2, mais o if no método configure
    @Autowired
    private Environment env;

    //só de adiconar as dependcias do spring security e do jsontoken, todas as URLs já foram bloqueadas.
    //aqui nesta lista iremos informar as URLs que queremos que sejam liberadas, que não irão exigir autenticacao
    private static final String[] PUBLIC_MATCHERS = {
            "/h2-console/**",
    };

    //caminhos que serão somente leitura, ou seja, não permitirão alterações
    private static final String[] PUBLIC_MATCHERS_GET = {
            "/produtos/**",
            "/categorias/**"
    };

    //aqui dizemos que as URLs da lista PUBLIC_MATCHERS serão liberadas e que para todo o resto precisa autenticar
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable();
        } //verifica se o profile é de teste e configura pra poder usar o banco H2, isso é uma coisa muito espeficida do framework Spring

        http.cors() //esse método cors serve para verificar se existe um CorsConfigurationSource (@Bean ali de baixo), e se existir, ele aplica a configuração do @Bean ali de baixo
            .and().csrf().disable(); //isso aqui desabilita a proteção de CSRF. Podemos fazer isso porque não vamos armazenar sessão de usuário.
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //aqui asseguramos que o back end não irá criar sessão de usuário
    }

    //aqui permitimos acesso aos endpoints por multiplas fontes usando as cofiguracoes basicas(applyPermitDefaultValues)
    //não sei direito o que isso significa, mas foi feito assim.
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }


}
