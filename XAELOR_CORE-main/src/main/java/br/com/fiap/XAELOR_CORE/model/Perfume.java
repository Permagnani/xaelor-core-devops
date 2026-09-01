package br.com.fiap.XAELOR_CORE.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_PERFUME")
public class Perfume {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "PERFUME_ID")
    private Long id;

    @Column (nullable = false, name = "PERFUME_NOME")
    private String nomePerfume;

    @Column (nullable = false, name = "PERFUME_GENERO")
    private String generoPerfume;

    @Column (name = "PERFUME_DESCRICAO")
    private String descricaoPerfume;
}
