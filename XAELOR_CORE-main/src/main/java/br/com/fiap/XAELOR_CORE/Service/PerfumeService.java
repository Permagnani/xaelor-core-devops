package br.com.fiap.XAELOR_CORE.Service;

import br.com.fiap.XAELOR_CORE.Repository.PerfumeRepository;
import br.com.fiap.XAELOR_CORE.model.Perfume;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;

    public PerfumeService(PerfumeRepository perfumeRepository) {
        this.perfumeRepository = perfumeRepository;
    }

    public List<Perfume> Listar() {
        return perfumeRepository.findAll();
    }

    public Perfume cadastrar(Perfume perfume){


        if (perfume.getNomePerfume() == null || perfume.getNomePerfume().isBlank()){
            throw new RuntimeException("Nome é obrigatório");
        }
        if (perfume.getGeneroPerfume() == null || perfume.getGeneroPerfume().isBlank()){
            throw new RuntimeException("Gênero do perfume é obrigatório");
        }
        return perfumeRepository.save(perfume);
    }

    public Perfume BuscarPorId(Long id){
        return perfumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Materia Prima não cadastrada"));
    }

    public Perfume Atualizar(Long id, Perfume perfume){
        Perfume existente = BuscarPorId(id);

        existente.setNomePerfume(perfume.getNomePerfume());
        existente.setGeneroPerfume(perfume.getGeneroPerfume());

        return perfumeRepository.save(existente);
    }

    public void DeletarPorId(Long id){
        Perfume perfume = BuscarPorId(id);
        perfumeRepository.delete(perfume);
    }

}
