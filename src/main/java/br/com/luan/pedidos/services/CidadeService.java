package br.com.luan.pedidos.services;

import br.com.luan.pedidos.domain.Cidade;
import br.com.luan.pedidos.repositories.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository repository;

    public List<Cidade> findByEstado(Integer estadoId) {
        return repository.findCidades(estadoId);
    }
}
