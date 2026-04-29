package br.com.fiap.esg_residuos.dto.response;

import br.com.fiap.esg_residuos.model.CollectionAlert;

import java.time.LocalDate;

public record CollectionAlertResponse(
        Long id,
        Long collectionPointId,
        String collectionPointName,
        Long collectionId,
        LocalDate alertDate,
        String alertType,
        String message
) {
    public static CollectionAlertResponse from(CollectionAlert a) {
        return new CollectionAlertResponse(
                a.getId(),
                a.getCollectionPoint().getId(),
                a.getCollectionPoint().getName(),
                a.getCollection() != null ? a.getCollection().getId() : null,
                a.getAlertDate(),
                a.getAlertType(),
                a.getMessage()
        );
    }
}
