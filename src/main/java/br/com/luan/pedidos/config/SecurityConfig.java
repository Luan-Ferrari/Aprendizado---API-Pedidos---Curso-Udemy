package br.com.luan.pedidos.config;

import br.com.luan.pedidos.security.JWTAuthenticationFilter;
import br.com.luan.pedidos.security.JWTAutorizationFilter;
import br.com.luan.pedidos.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //esse Environment precisa para liberar o acesso ao banco H2, mais o if no método configure
    @Autowired
    private Environment env;

    @Autowired
    private UserDetailsService userDetailsService; //injetar a interface que o Spring se vira encontrar a implementacao

    @Autowired
    private JWTUtil jwtUtil;

    //só de adiconar as dependcias do spring security e do jsontoken, todas as URLs já foram bloqueadas.
    //aqui nesta lista iremos informar as URLs que queremos que sejam liberadas, que não irão exigir autenticacao
    private static final String[] PUBLIC_MATCHERS = {
            "/h2-console/**"
    };

    //caminhos que serão somente leitura, ou seja, não permitirão alterações
    private static final String[] PUBLIC_MATCHERS_GET = {
            "/produtos/**",
            "/categorias/**",
            "/estados/**"
    };

    //caminhos que só são permitidos para o POST (por exemplo um usuario nao logado poder se cadastrar)
    private static final String[] PUBLIC_MATCHERS_POST = {
            "/clientes",
            "/auth/forgot/**"
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
                .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated();
        http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil)); //adicionamos o filtro que foi criado no package security (todas as classes)
        http.addFilter(new JWTAutorizationFilter(authenticationManager(), jwtUtil, userDetailsService)); //filtro de autorizacao
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //aqui asseguramos que o back end não irá criar sessão de usuário
    }

    //esse metodo é uma sobrecarga do de cima, ele é bem padrão, serve para mostrar onde buscar o usuario e onde esta o encriptador da senha
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    //aqui permitimos acesso aos endpoints por multiplas fontes usando as cofiguracoes basicas(applyPermitDefaultValues)
    //não sei direito o que isso significa, mas foi feito assim.
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    //método para criptografar a senha de cliente
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
