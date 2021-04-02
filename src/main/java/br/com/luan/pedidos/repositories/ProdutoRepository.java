package br.com.luan.pedidos.repositories;

import br.com.luan.pedidos.domain.Categoria;
import br.com.luan.pedidos.domain.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    //Como o Repositori do Spring Data é uma interface, seria necessário colocar a assinatura do método aqui e depois
    //criar uma classe para fazer a implementação, porem, usando a anotação @Query, podemos escrever a consulta JPQL
    //entre parenteses que o Spring irá implementar o método por conta propria. ISSO FACILITA MUITO A VIDA.
    //As anotações @Param colocamos na frente dos atributos que serão usados na consulta JPQL, neste caso "nome" e "categoria"
    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
    Page<Produto> search(@Param("nome") String nome, @Param("categorias")List<Categoria> categorias, Pageable pageRequest);
}

//Outra forma de montar a mesma consulta acima (método search) usando o padrão de nomes do Spring Data. Assim:
//Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
//OBS: Containing é o equivalente ao LIKE.
//fazendo a assinatura desta forma, não precisa da anotação @Query, nem das anotacoes @Param, pois somente pela
//assinatura do método o Spring Data vai montar a consulta toda sozinho.
//Se usar a anotação @Query, ela se sobrepõe ao nome do método.