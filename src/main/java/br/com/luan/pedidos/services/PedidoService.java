package br.com.luan.pedidos.services;

import br.com.luan.pedidos.domain.ItemPedido;
import br.com.luan.pedidos.domain.PagamentoComBoleto;
import br.com.luan.pedidos.domain.Pedido;
import br.com.luan.pedidos.domain.enums.EstadoPagamento;
import br.com.luan.pedidos.repositories.ItemPedidoRepository;
import br.com.luan.pedidos.repositories.PagamentoRepository;
import br.com.luan.pedidos.repositories.PedidoRepository;
import br.com.luan.pedidos.repositories.ProdutoRepository;
import br.com.luan.pedidos.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;
    @Autowired
    private BoletoService boletoService;
    @Autowired
    private PagamentoRepository pagamentoRepository;
    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;


    public Pedido find(Integer id) {
        Optional<Pedido> obj = repository.findById(id);
        System.out.println("Cheguei aqui");
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não Encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
    }

    @Transactional
    public Pedido insert(Pedido obj) {
        obj.setId(null);
        obj.setInstante(new Date());

        obj.getPagamento().setEstadoPagto(EstadoPagamento.PENDENTE);
        obj.getPagamento().setPedido(obj);
        if (obj.getPagamento() instanceof PagamentoComBoleto) {
            PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
            boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
        }

        obj = repository.save(obj);
        pagamentoRepository.save(obj.getPagamento());

        for (ItemPedido ip : obj.getItens()) {
            ip.setDesconto(0.0);
            ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco());
            System.out.println(ip.getPreco());
            ip.setPedido(obj);
        }
        itemPedidoRepository.saveAll(obj.getItens());
        return obj;
    }
}
