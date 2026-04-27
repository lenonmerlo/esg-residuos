package br.com.fiap.esg_residuos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "ALERTA_COLETA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALERTA")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PONTO_COLETA", nullable = false)
    private CollectionPoint collectionPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COLETA")
    private Collection collection;

    @Column(name = "DT_ALERTA", nullable = false)
    private LocalDate alertDate;

    @Column(name = "TIPO_ALERTA", nullable = false, length = 30)
    private String alertType;

    @Column(name = "MENSAGEM", nullable = false, length = 300)
    private String message;
}