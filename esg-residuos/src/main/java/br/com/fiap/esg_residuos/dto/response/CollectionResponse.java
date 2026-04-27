package br.com.fiap.esg_residuos.dto.response;

import br.com.fiap.esg_residuos.model.Collection;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CollectionResponse(
        Long id,
        Long collectionPointId,
        String collectionPointName,
        Long wasteId,
        String wastType,
        LocalDate collectionDate,
        BigDecimal volumeKg,
        String status,
        LocalDate destinationDate,
        String destinationHistory
) {
    public static CollectionResponse from(Collection c){
        return new CollectionResponse(
                c.getId(),
                c.getCollectionPoint().getId(),
                c.getCollectionPoint().getName(),
                c.getWaste().getId(),
                c.getWaste().getWasteType(),
                c.getCollectionDate(),
                c.getVolumeKg(),
                c.getStatus(),
                c.getDestinationDate(),
                c.getDestinationHistory()
        );
    }
}
