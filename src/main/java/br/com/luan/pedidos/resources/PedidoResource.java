package br.com.luan.pedidos.resources;

import br.com.luan.pedidos.domain.Pedido;
import br.com.luan.pedidos.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/pedidos")
public class PedidoResource {

    @Autowired
    private PedidoService service;

    @GetMapping
    public ResponseEntity<Page<Pedido>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue="DESC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "instante") String orderBy) {
        Page<Pedido> list = service.findPage(page, linesPerPage, direction, orderBy);
        return ResponseEntity.ok().body(list);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Pedido> find(@PathVariable Integer id) {
        Pedido obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

    //em particular no pedido, não vamos criar um DTO para inserir um novo pedido, isso porque Pedido tem muitos
    //dados associados. Então seria necessário um DTO gigante ou então vários DTO uns associados aos outros.
    //Ele fez assim para ganhar tempo, mas acredito que o correto seria seguir o padrao que já vinha sendo feito
    //e fazer o DTO.

    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj) {
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }
}
