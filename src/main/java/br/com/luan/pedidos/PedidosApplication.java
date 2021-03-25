package br.com.luan.pedidos;

import br.com.luan.pedidos.domain.Categoria;
import br.com.luan.pedidos.domain.Cidade;
import br.com.luan.pedidos.domain.Estado;
import br.com.luan.pedidos.domain.Produto;
import br.com.luan.pedidos.repositories.CategoriaRepository;
import br.com.luan.pedidos.repositories.ProdutoRepository;
import br.com.luan.pedidos.repositories.exceptions.CidadeRepository;
import br.com.luan.pedidos.repositories.exceptions.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.Arrays;

//Todas as injeções de Repository, o implements CommandLineRunner e as instanciações de objetos feitos aqui
//são para testar os relacionamentos do banco de dados através do código, ao invés de ficar criando queries
//lá no mySql e incluir manualmente os dados na tabela.

@SpringBootApplication
public class PedidosApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private EstadoRepository estadoRepository;

	public static void main(String[] args) {
		SpringApplication.run(PedidosApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PedidosApplication.class);
	}

	@Override
	public void run(String... args) throws Exception {

		//instanciado as categorias
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");

		//instanciando os produtos
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);

		//instanciado os relacionamentos entre categorias e produtos, esses dados vao para a tablea PRODUTO_CATEGORIA
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));

		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));

		//para persistir no banco de dados as categorias e os produtos
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));

		//criando os estados
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");

		//criando as cidades
		Cidade c1 = new Cidade(null, "Uberlandia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);

		//informando aos objetos do tipo Estado quais Cidades pertencem a ele
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2, c3));

		//para persistir no banco de dados as categorias e os produtos
		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));


	}
}
