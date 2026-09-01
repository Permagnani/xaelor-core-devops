package br.com.fiap.XAELOR_CORE.Service;

import br.com.fiap.XAELOR_CORE.Repository.PerfumeMateriaPrimaRepository;
import br.com.fiap.XAELOR_CORE.enums.TipoUnidade;
import br.com.fiap.XAELOR_CORE.model.PerfumeMateriaPrima;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfumeMateriaPrimaService {

    private final PerfumeMateriaPrimaRepository perfumeMateriaPrimaRepository;

    public PerfumeMateriaPrimaService(PerfumeMateriaPrimaRepository perfumeMateriaPrimaRepository) {
        this.perfumeMateriaPrimaRepository = perfumeMateriaPrimaRepository;
    }

    public PerfumeMateriaPrima cadastrar(PerfumeMateriaPrima perfumeMateriaPrima){
        if(perfumeMateriaPrima.getValorPorUnidade() == null ||
                perfumeMateriaPrima.getValorPorUnidade() <= 0 ||
                    perfumeMateriaPrima.getTipoUnidade() == null) {
            throw new RuntimeException("Campos obrigatórios devem ser preenchidos");
        }

        return perfumeMateriaPrimaRepository.save(perfumeMateriaPrima);
    }

    public List<PerfumeMateriaPrima> Listar(){ return perfumeMateriaPrimaRepository.findAll(); }

    public PerfumeMateriaPrima BuscarPorId( Long Id){
        return perfumeMateriaPrimaRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Materia Prima não cadastrada"));
    }

    public void DeletarPorID(Long id){
        PerfumeMateriaPrima perfumeMateriaPrima = BuscarPorId(id);
        perfumeMateriaPrimaRepository.delete(perfumeMateriaPrima);
    }

    public PerfumeMateriaPrima atualizarPorId(PerfumeMateriaPrima perfumeMateriaPrima, Long id){
        PerfumeMateriaPrima existente = BuscarPorId(id);

        if(perfumeMateriaPrima.getValorPorUnidade() == null){
            throw new RuntimeException("Valor da Unidade é obrigatório");
        }
        if(perfumeMateriaPrima.getTipoUnidade() == null) {
            throw new RuntimeException("Tipo de Unidade é obrigatório");
        }
        if(perfumeMateriaPrima.getMargemLucro() == null) {
            throw new RuntimeException("Margem de lucro é obrigatório");
        }
            return perfumeMateriaPrimaRepository.save(existente);
    }
}
