package br.com.fiap.esg_residuos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "COLETA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COLETA")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PONTO_COLETA", nullable = false)
    private CollectionPoint collectionPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_RESIDUO", nullable = false)
    private Waste waste;

    @Column(name = "DT_COLETA", nullable = false)
    private LocalDate collectionDate;

    @Column(name = "VOLUME_KG", nullable = false)
    private BigDecimal volumeKg;

    @Column(name = "STATUS_COLETA", nullable = false, length = 20)
    private String status;

    @Column(name = "DT_DESTINACAO")
    private LocalDate destinationDate;

    @Column(name = "HISTORICO_DESTINACAO", length = 4000)
    private String destinationHistory;
}
