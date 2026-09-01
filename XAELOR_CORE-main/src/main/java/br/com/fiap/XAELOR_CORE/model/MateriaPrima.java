package br.com.fiap.XAELOR_CORE.model;

import br.com.fiap.XAELOR_CORE.enums.TipoUnidade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_MATERIAPRIMA")
public class MateriaPrima {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "MATPRIMA_ID")
    private Long id;

    @Column(nullable=false, name = "MATPRIMA_NOME")
    private String nome;

    @Column(nullable = false, name = "MATPRIMA_TIPOUNIDADE")
    @Enumerated(EnumType.STRING)
    private TipoUnidade tipoUnidade;

    @Column(name = "MATPRIMA_DESCRICAO")
    private String descricao;
}
