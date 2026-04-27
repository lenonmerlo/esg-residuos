package br.com.fiap.esg_residuos.dto.response;

import br.com.fiap.esg_residuos.model.Waste;

public record WasteResponse(Long id, String wastType, String description) {
    public static WasteResponse from(Waste waste) {
        return new WasteResponse(
                waste.getId(),
                waste.getWasteType(),
                waste.getDescription()
        );
    }
}
