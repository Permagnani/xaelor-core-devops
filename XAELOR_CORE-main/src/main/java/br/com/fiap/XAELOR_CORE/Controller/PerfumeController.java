package br.com.fiap.XAELOR_CORE.Controller;

import br.com.fiap.XAELOR_CORE.Service.PerfumeService;
import br.com.fiap.XAELOR_CORE.model.Perfume;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/perfume")
public class PerfumeController {

    private final PerfumeService perfumeService;

    public PerfumeController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Perfume>>> listar() {
        List<EntityModel<Perfume>> perfumes = perfumeService.Listar().stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PerfumeController.class).buscarPorId(p.getId())).withSelfRel(),
                        linkTo(methodOn(PerfumeController.class).listar()).withRel("perfumes")
                ))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Perfume>> collection = CollectionModel.of(perfumes,
                linkTo(methodOn(PerfumeController.class).listar()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Perfume>> buscarPorId(@PathVariable Long id) {
        Perfume perfume = perfumeService.BuscarPorId(id);
        EntityModel<Perfume> model = EntityModel.of(perfume,
                linkTo(methodOn(PerfumeController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(PerfumeController.class).listar()).withRel("perfumes"),
                linkTo(methodOn(PerfumeController.class).deletarPorId(id)).withRel("deletar")
        );
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Perfume>> cadastrar(@Valid @RequestBody Perfume perfume) {
        Perfume salvo = perfumeService.cadastrar(perfume);
        EntityModel<Perfume> model = EntityModel.of(salvo,
                linkTo(methodOn(PerfumeController.class).buscarPorId(salvo.getId())).withSelfRel(),
                linkTo(methodOn(PerfumeController.class).listar()).withRel("perfumes")
        );
        return ResponseEntity.ok(model);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<EntityModel<Perfume>> atualizar(@PathVariable Long id, @Valid @RequestBody Perfume perfume) {
        Perfume atualizado = perfumeService.Atualizar(id, perfume);
        EntityModel<Perfume> model = EntityModel.of(atualizado,
                linkTo(methodOn(PerfumeController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(PerfumeController.class).listar()).withRel("perfumes")
        );
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        perfumeService.DeletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
