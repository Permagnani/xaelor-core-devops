package br.com.fiap.XAELOR_CORE.model;

import br.com.fiap.XAELOR_CORE.enums.TipoUnidade;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "TB_PERFUME_MATERIA_PRIMA")
public class PerfumeMateriaPrima {

    @Id
    @Column (name = "PERMAT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "PERFUME_ID")
    private Perfume id_perfume;

    @ManyToOne
    @JoinColumn (name = "PERFUME_NOME")
    private Perfume nome_perfume;

    @ManyToOne
    @JoinColumn (name = "MATPRIMA_ID")
    private MateriaPrima id_materiaPrima;

    @ManyToOne
    @JoinColumn (name = "MATPRIMA_NOME")
    private MateriaPrima nome_materiaPrima;

    @Positive
    @Column(nullable = false, name = "PERMAT_VALORUNIDADE")
    private Double valorPorUnidade;


    @Column(nullable = false, name = "PERMAT_TIPOUNIDADE")
    @Enumerated(EnumType.STRING)
    private TipoUnidade tipoUnidade;

    @Positive
    @Column(nullable = false, name = "PERMAT_CUSTOTOTAL")
    private Double custoTotal;

    @Positive
    @Column(nullable = false, name = "PERMAT_MARGEMLUCRO")
    private BigDecimal margemLucro;

    @Positive
    @Column(nullable = false, name = "PERMAT_VALORFINAL")
    private Double valorFinal;
}
