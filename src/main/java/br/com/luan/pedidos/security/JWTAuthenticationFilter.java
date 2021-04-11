package br.com.luan.pedidos.security;

import br.com.luan.pedidos.dto.CredenciaisDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

//quando extende a classe UsernamePasswordAuthenticationFilter, o Spring já sabe que esta classe é para ser
//o filtro de autenticação, ou seja, é essa classe que vai ser chamada quando alguem tentar fazer login,
//o endpoint /login é reservado ao spring. Toda essa classe é padrao do Framework do Spring Security
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //authenticationManager e jwtUtil serão injetados pelo construtor, ou seja, toda vez que precisar instanciar
    //esta classe, devemos passar os dois atributos abaixo como argumentos.
    private AuthenticationManager authenticationManager;
    private JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   AuthenticationManager authenticationManager1,
                                   JWTUtil jwtUtil) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager1;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication (HttpServletRequest req,
                                                 HttpServletResponse res) throws AuthenticationException {
        try {
            CredenciaisDTO creds = new ObjectMapper()
                    .readValue(req.getInputStream(), CredenciaisDTO.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(),
                    creds.getSenha(),
                    new ArrayList<>());
            Authentication auth = authenticationManager.authenticate(authToken);
            return auth;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String username = ((UserSS) auth.getPrincipal()).getUsername();
        String token = jwtUtil.generateToken(username);
        res.addHeader("Authorization", "Bearer " + token);
    }

}
