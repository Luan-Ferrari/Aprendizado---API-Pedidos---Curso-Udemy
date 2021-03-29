package br.com.luan.pedidos.resources;

import br.com.luan.pedidos.domain.Categoria;
import br.com.luan.pedidos.dto.CategoriaDTO;
import br.com.luan.pedidos.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> find(@PathVariable Integer id) {
        Categoria obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody Categoria obj) { //@RequestBody transforma o objeto Json em objeto Java
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder //Isso é o padrão para criar uma URI que diz o caminho do novo recurso inserido e retornar quando usamos um POST
                .fromCurrentRequest() // Isso aqui pega a URI atual, no caso http://localhost:8080/categorias
                .path("/{id}") // Isso aqui diz para adicionar um valor de Id após a URI atual
                .buildAndExpand(obj.getId()) //Isso aqui atribui o valor do Id criado ao path aqui de cima.
                .toUri(); // Isso aqui converte tudo em uma URI.
        return ResponseEntity.created(uri).build(); // o .created define qual codigo HTTP vai ser retornado, neste caso 201
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody Categoria obj, @PathVariable Integer id) {
        obj.setId(id);
        obj = service.update(obj);
        return ResponseEntity.noContent().build(); // o .noContent define qual codigo HTTP vai ser retornado, neste caso 204
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
