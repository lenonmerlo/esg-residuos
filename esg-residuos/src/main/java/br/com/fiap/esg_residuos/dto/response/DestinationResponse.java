package br.com.fiap.esg_residuos.dto.response;

import br.com.fiap.esg_residuos.model.Destination;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DestinationResponse(
        Long id,
        Long collectionId,
        LocalDate destinationDate,
        String destinationName,
        String processingType,
        BigDecimal destinedVolumeKg
) {
    public static DestinationResponse from(Destination d) {
        return new DestinationResponse(
                d.getId(),
                d.getCollection().getId(),
                d.getDestinationDate(),
                d.getDestinationName(),
                d.getProcessingType(),
                d.getDestinedVolumeKg()
        );
    }
}
