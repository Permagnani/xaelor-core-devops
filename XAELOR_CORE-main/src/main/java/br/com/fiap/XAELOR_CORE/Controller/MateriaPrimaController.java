package br.com.fiap.XAELOR_CORE.Controller;

import br.com.fiap.XAELOR_CORE.Service.MateriaPrimaService;
import br.com.fiap.XAELOR_CORE.model.MateriaPrima;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/materiaPrima")
public class MateriaPrimaController {

    private final MateriaPrimaService materiaPrimaService;

    public MateriaPrimaController(MateriaPrimaService materiaPrimaService) {
        this.materiaPrimaService = materiaPrimaService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<MateriaPrima>>> listar() {
        List<EntityModel<MateriaPrima>> materias = materiaPrimaService.Listar().stream()
                .map(m -> EntityModel.of(m,
                        linkTo(methodOn(MateriaPrimaController.class).buscarPorID(m.getId())).withSelfRel(),
                        linkTo(methodOn(MateriaPrimaController.class).listar()).withRel("materiasPrimas")
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(materias,
                linkTo(methodOn(MateriaPrimaController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<MateriaPrima>> buscarPorID(@PathVariable Long id) {
        MateriaPrima materiaPrima = materiaPrimaService.BuscarPorID(id);
        return ResponseEntity.ok(EntityModel.of(materiaPrima,
                linkTo(methodOn(MateriaPrimaController.class).buscarPorID(id)).withSelfRel(),
                linkTo(methodOn(MateriaPrimaController.class).listar()).withRel("materiasPrimas"),
                linkTo(methodOn(MateriaPrimaController.class).DeletarPorID(id)).withRel("deletar")
        ));
    }

    @PostMapping
    public ResponseEntity<EntityModel<MateriaPrima>> cadastrar(@Valid @RequestBody MateriaPrima materiaPrima) {
        MateriaPrima salvo = materiaPrimaService.cadastrar(materiaPrima);
        return ResponseEntity.ok(EntityModel.of(salvo,
                linkTo(methodOn(MateriaPrimaController.class).buscarPorID(salvo.getId())).withSelfRel(),
                linkTo(methodOn(MateriaPrimaController.class).listar()).withRel("materiasPrimas")
        ));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<EntityModel<MateriaPrima>> AtualizarPorID(@PathVariable Long id, @Valid @RequestBody MateriaPrima materiaPrima) {
        MateriaPrima existente = materiaPrimaService.BuscarPorID(id);
        existente.setNome(materiaPrima.getNome());
        existente.setTipoUnidade(materiaPrima.getTipoUnidade());
        existente.setDescricao(materiaPrima.getDescricao() == null ? "" : materiaPrima.getDescricao());
        MateriaPrima atualizado = materiaPrimaService.cadastrar(existente);
        return ResponseEntity.ok(EntityModel.of(atualizado,
                linkTo(methodOn(MateriaPrimaController.class).buscarPorID(id)).withSelfRel(),
                linkTo(methodOn(MateriaPrimaController.class).listar()).withRel("materiasPrimas")
        ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> DeletarPorID(@PathVariable Long id) {
        materiaPrimaService.deletarPorID(id);
        return ResponseEntity.noContent().build();
    }
}
