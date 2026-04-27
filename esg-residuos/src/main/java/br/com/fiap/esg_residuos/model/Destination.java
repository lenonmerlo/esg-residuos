package br.com.fiap.esg_residuos.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "DESTINACAO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DESTINACAO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COLETA", nullable = false)
    private Collection collection;

    @Column(name = "DT_DESTINACAO", nullable = false)
    private LocalDate destinationDate;

    @Column(name = "NOME_DESTINACAO", nullable = false, length = 120)
    private String destinationName;

    @Column(name = "TIPO_PROCESSAMENTO", nullable = false, length = 60)
    private String processingType;

    @Column(name = "VOLUME_KG_DESTINADO", nullable = false)
    private BigDecimal destinedVolumeKg;
}