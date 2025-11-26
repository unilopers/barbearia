package br.com.unilopers.barbearia_api.controller;
import br.com.unilopers.barbearia_api.model.Produto;
import br.com.unilopers.barbearia_api.repository.ProdutoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    @Autowired
    private ProdutoView produtoView;
    @GetMapping
    public List <Produto> listar() {
        return produtoView.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return produtoView.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto criar(@RequestBody Produto produto) {
        return produtoView.save(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        if (!produtoView.existsById(id)) {
    return ResponseEntity.notFound().build();
    }

    produto.setId(id);
        return ResponseEntity.ok(produtoView.save(produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if(!produtoView.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        produtoView.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}