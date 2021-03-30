package br.com.luan.pedidos.services;

import br.com.luan.pedidos.domain.Categoria;
import br.com.luan.pedidos.dto.CategoriaDTO;
import br.com.luan.pedidos.repositories.CategoriaRepository;
import br.com.luan.pedidos.services.exceptions.DataIntegrityException;
import br.com.luan.pedidos.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public List<Categoria> findAll() {
        return repository.findAll();
    }

    //metodo para pesquisar com paginacao, ele recebe o numero da pagina(inicia com 0), quantas linhas por pagina, a direcao e os argumentos de pesquisa
    public Page<Categoria> findPage(Integer page, Integer linesPerPage, String direction, String orderBy) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return repository.findAll(pageRequest);
    }

    public Categoria find(Integer id) {
        Optional<Categoria> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
    }

    public Categoria insert(Categoria obj) {
        obj.setId(null); //aqui o null serve para garantir que seja criado um novo objeto, se o Id vim preenchido ele atualiza e não cria um novo
        return repository.save(obj);
    }

    public Categoria update(Categoria obj) {
        find(obj.getId()); //não é obrigatório, aqui chama o método find acima para ver se o Id existe, se não existir já retorna a exception de que não existe
        return repository.save(obj);
    }

    public void delete(Integer id) {
        find(id); // isso não é obrigatório, serve só para verificar se o id existe antes de dar o comando de excluir
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
        } //esse try catch é necessário porque da um erro de integridade se tentar excluir uma categoria que possui produtos
        //quando isso ocorre existem duas saidas, ou apaga os produtos também, ou retorna uma exception e aborta o delete.
    }

    //metodo auxiliar para converter o DTO recebido no método POST e PUT em uma Categoria do domain.
    //Precisa disso pois as validacoes estão sendo feitas nas classes DTO, então quando recebemos um
    //POST ou PUT devemos ter um objeto DTO para validar, mas na hora de persistir os dados precisamos
    //de um objeto do pacote Domain
    public Categoria fromDTO(CategoriaDTO objDto) {
        return new Categoria(objDto.getId(), objDto.getNome());
    }

}
