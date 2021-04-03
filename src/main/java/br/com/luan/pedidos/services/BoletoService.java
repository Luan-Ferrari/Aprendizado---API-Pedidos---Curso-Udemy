package br.com.luan.pedidos.services;

import br.com.luan.pedidos.domain.PagamentoComBoleto;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class BoletoService {

    //esse método vai setar a data de vencimento do boleto para 7 dias após a data do pedido.
    //na vida real, se usarmos um web service que cria um boleto para nós, toda essa implementação
    //seria substituida por uma chamada a esse webservice.
    public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(instanteDoPedido);
        cal.add(Calendar.DAY_OF_MONTH, 7);
        pagto.setDataVencimento(cal.getTime());
    }
}
