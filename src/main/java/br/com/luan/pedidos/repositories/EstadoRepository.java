package br.com.luan.pedidos.repositories;

import br.com.luan.pedidos.domain.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
}
