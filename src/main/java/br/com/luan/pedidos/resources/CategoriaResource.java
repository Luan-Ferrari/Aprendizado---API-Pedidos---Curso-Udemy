package br.com.luan.pedidos.resources;

import br.com.luan.pedidos.domain.Categoria;
import br.com.luan.pedidos.dto.CategoriaDTO;
import br.com.luan.pedidos.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaService service;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> findAll() {
        List<Categoria> list = service.findAll();
        List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    //para usar /categorias/page?linesPerPage=3&page=3&orderBy=id&direction=DESC
    //alem da lista, esse método tras diversas informações uteis ao front end no body da resposta, como: se é
    //a última pagina, quantos objetos existem no total no banco de dados, quanto estao sendo mostrados por pagina,
    //e muitos outros. Essas informações vem do objeto Page, que é tipo uma List, porem com mais recursos e com Java 8 compliance,
    //propria para esse objetivo de paginacoes.
    @GetMapping("/page")
    public ResponseEntity<Page<CategoriaDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page, //dessa forma, com o @RequestParam, o atributo se torna opcional, porque tem um valor padrao
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, //sugestao 24, pois é multiplo de 1,2,3 e 4, ficam facil de fazer layout responsivo depois
            @RequestParam(value = "direction", defaultValue = "ASC") String direction, //ASC ascendente ou DESC descendente
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy) {
        Page<Categoria> list = service.findPage(page, linesPerPage, direction, orderBy);
        Page<CategoriaDTO> listDto = list.map(obj -> new CategoriaDTO(obj)); //por ser Java 8 Compliance, a conversão entre listas do objeto Page exige menos coisa que uma List
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> find(@PathVariable Integer id) {
        Categoria obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDto) { //@RequestBody transforma o objeto Json em objeto Java
        Categoria obj = service.fromDTO(objDto);
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder //Isso é o padrão para criar uma URI que diz o caminho do novo recurso inserido e retornar quando usamos um POST
                .fromCurrentRequest() // Isso aqui pega a URI atual, no caso http://localhost:8080/categorias
                .path("/{id}") // Isso aqui diz para adicionar um valor de Id após a URI atual
                .buildAndExpand(obj.getId()) //Isso aqui atribui o valor do Id criado ao path aqui de cima.
                .toUri(); // Isso aqui converte tudo em uma URI.
        return ResponseEntity.created(uri).build(); // o .created define qual codigo HTTP vai ser retornado, neste caso 201
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto, @PathVariable Integer id) {
        Categoria obj = service.fromDTO(objDto);
        obj.setId(id);
        obj = service.update(obj);
        return ResponseEntity.noContent().build(); // o .noContent define qual codigo HTTP vai ser retornado, neste caso 204
    }

    @PreAuthorize("hasAnyRole('ADMIN')") //para isso funcionar precisa ter a anotacao no SecurityConfig também
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
