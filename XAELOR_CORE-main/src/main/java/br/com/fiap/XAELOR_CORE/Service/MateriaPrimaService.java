package br.com.fiap.XAELOR_CORE.Service;

import br.com.fiap.XAELOR_CORE.Repository.MateriaPrimaRepository;
import br.com.fiap.XAELOR_CORE.enums.TipoUnidade;
import br.com.fiap.XAELOR_CORE.model.MateriaPrima;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MateriaPrimaService {

    private final MateriaPrimaRepository materiaPrimaRepository;

    public MateriaPrimaService(MateriaPrimaRepository materiaPrimaRepository) {
        this.materiaPrimaRepository = materiaPrimaRepository;
    }

    public MateriaPrima cadastrar(MateriaPrima materiaPrima){

        if (materiaPrima.getNome() == null){
            throw new RuntimeException("Nome é obrigatorio");
        }

        if (materiaPrima.getTipoUnidade() == null){
            throw new RuntimeException("Tipo Unidade é obrigatório para cadastrar Materias Primas");
        }

        return materiaPrimaRepository.save(materiaPrima);
    }
    public List<MateriaPrima> Listar(){
        return materiaPrimaRepository.findAll();
    }

    public MateriaPrima BuscarPorID(Long id){
        return materiaPrimaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Materia Prima não cadastrada"));
    }

    public void deletarPorID(Long id){
        MateriaPrima materiaPrima = BuscarPorID(id);
        materiaPrimaRepository.delete(materiaPrima);
    }

}
