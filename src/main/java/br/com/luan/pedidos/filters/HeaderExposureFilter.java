package br.com.luan.pedidos.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HeaderExposureFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    //Esse filtro vai interceptar todas as requisições, e irá expor o header Location, que é o header que retorna o
    //URL do que criamos no banco de dados através do método POST
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse)servletResponse; //Esse cast é necessário pois o objeto do tipo
                                                                        //ServletResponse não possui o método addHeader
        res.addHeader("access-control-expose-headers", "location");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
