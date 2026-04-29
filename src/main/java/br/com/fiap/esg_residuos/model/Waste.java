package br.com.fiap.esg_residuos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RESIDUO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Waste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESIDUO")
    private Long id;

    @Column(name = "TIPO_RESIDUO", nullable = false, unique = true, length = 30)
    private String wasteType;

    @Column(name = "DESCRICAO", length = 200)
    private String description;
}
