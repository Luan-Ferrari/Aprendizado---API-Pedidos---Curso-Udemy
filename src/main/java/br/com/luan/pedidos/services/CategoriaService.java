package br.com.luan.pedidos.services;

import br.com.luan.pedidos.domain.Categoria;
import br.com.luan.pedidos.repositories.CategoriaRepository;
import br.com.luan.pedidos.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public Categoria find(Integer id) {
        Optional<Categoria> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
    }

    public Categoria insert(Categoria obj) {
        obj.setId(null); //aqui o null serve para garantir que seja criado um novo objeto, se o Id vim preenchido ele atualiza e não cria um novo
        return repository.save(obj);
    }
}
