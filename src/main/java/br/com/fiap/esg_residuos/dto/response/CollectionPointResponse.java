package br.com.fiap.esg_residuos.dto.response;

import br.com.fiap.esg_residuos.model.CollectionPoint;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CollectionPointResponse(
        Long id,
        String name,
        BigDecimal capacityKg,
        BigDecimal alertVolumeKg,
        BigDecimal occupiedVolumeKg,
        String status,
        LocalDate updateAt
) {
    public static CollectionPointResponse from(CollectionPoint cp) {
        return new CollectionPointResponse(
                cp.getId(),
                cp.getName(),
                cp.getCapacityKg(),
                cp.getAlertVolumeKg(),
                cp.getOccupiedVolumeKg(),
                cp.getStatus(),
                cp.getUpdatedAt()
        );
    }
}
