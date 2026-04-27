package br.com.fiap.esg_residuos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "PONTO_COLETA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PONTO_COLETA")
    private Long id;

    @Column(name = "NOME_PONTO", nullable = false, length = 100)
    private String name;

    @Column(name = "CAPACIDADE_KG", nullable = false)
    private BigDecimal capacityKg;

    @Column(name = "VOLUME_ALERTA_KG", nullable = false)
    private BigDecimal alertVolumeKg;

    @Column(name = "VOLUME_OCUPADO_KG", nullable = false)
    private BigDecimal occupiedVolumeKg;

    @Column(name = "STATUS_PONTO", nullable = false, length = 20)
    private String status;

    @Column(name = "DT_ATUALIZACAO", nullable = false)
    private LocalDate updatedAt;
}
