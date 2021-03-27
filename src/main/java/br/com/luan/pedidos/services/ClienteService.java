package br.com.luan.pedidos.services;

import br.com.luan.pedidos.domain.Cliente;
import br.com.luan.pedidos.repositories.ClienteRepository;
import br.com.luan.pedidos.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository repository;

    public Cliente find(Integer id) {
        Optional<Cliente> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não Encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
    }
}
