package br.com.luan.pedidos.repositories;

import br.com.luan.pedidos.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    //Aqui aplicamos o SpringJPA, onde só precisamos criar a assinatura do método, o resto ele faz sozinho
    //Transaction readOnly =true serve para deixar a transação mais rapida, pois é somente leitura.
    @Transactional(readOnly = true)
    Cliente findByEmail(String email);
}
