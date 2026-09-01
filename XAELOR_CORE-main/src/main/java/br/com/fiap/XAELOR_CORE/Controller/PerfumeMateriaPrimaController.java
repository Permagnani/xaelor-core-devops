package br.com.fiap.XAELOR_CORE.Controller;

import br.com.fiap.XAELOR_CORE.Service.PerfumeMateriaPrimaService;
import br.com.fiap.XAELOR_CORE.model.PerfumeMateriaPrima;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/perfumeMateriaPrima")
public class PerfumeMateriaPrimaController {

    private final PerfumeMateriaPrimaService perfumeMateriaPrimaService;

    public PerfumeMateriaPrimaController(PerfumeMateriaPrimaService perfumeMateriaPrimaService) {
        this.perfumeMateriaPrimaService = perfumeMateriaPrimaService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PerfumeMateriaPrima>>> listar() {
        List<EntityModel<PerfumeMateriaPrima>> lista = perfumeMateriaPrimaService.Listar().stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PerfumeMateriaPrimaController.class).buscarPorId(p.getId())).withSelfRel(),
                        linkTo(methodOn(PerfumeMateriaPrimaController.class).listar()).withRel("perfumeMateriaPrimas")
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(PerfumeMateriaPrimaController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PerfumeMateriaPrima>> buscarPorId(@PathVariable Long id) {
        PerfumeMateriaPrima perfumeMateriaPrima = perfumeMateriaPrimaService.BuscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(perfumeMateriaPrima,
                linkTo(methodOn(PerfumeMateriaPrimaController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(PerfumeMateriaPrimaController.class).listar()).withRel("perfumeMateriaPrimas"),
                linkTo(methodOn(PerfumeMateriaPrimaController.class).deletarPorId(id)).withRel("deletar")
        ));
    }

    @PostMapping
    public ResponseEntity<EntityModel<PerfumeMateriaPrima>> cadastrar(@Valid @RequestBody PerfumeMateriaPrima perfumeMateriaPrima) {
        PerfumeMateriaPrima salvo = perfumeMateriaPrimaService.cadastrar(perfumeMateriaPrima);
        return ResponseEntity.ok(EntityModel.of(salvo,
                linkTo(methodOn(PerfumeMateriaPrimaController.class).buscarPorId(salvo.getId())).withSelfRel(),
                linkTo(methodOn(PerfumeMateriaPrimaController.class).listar()).withRel("perfumeMateriaPrimas")
        ));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<EntityModel<PerfumeMateriaPrima>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PerfumeMateriaPrima perfumeMateriaPrima) {
        PerfumeMateriaPrima atualizado = perfumeMateriaPrimaService.atualizarPorId(perfumeMateriaPrima, id);
        return ResponseEntity.ok(EntityModel.of(atualizado,
                linkTo(methodOn(PerfumeMateriaPrimaController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(PerfumeMateriaPrimaController.class).listar()).withRel("perfumeMateriaPrimas")
        ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        perfumeMateriaPrimaService.DeletarPorID(id);
        return ResponseEntity.noContent().build();
    }
}