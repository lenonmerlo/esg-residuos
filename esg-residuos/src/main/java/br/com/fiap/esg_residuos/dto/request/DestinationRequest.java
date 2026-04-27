package br.com.fiap.esg_residuos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DestinationRequest(
        @NotNull(message = "Collection is required")
        Long collectionId,

        @NotBlank(message = "Destination name is required")
        @Size(max = 120, message = "Destination name must have at most 120 characters")
        String destinationName,

        @NotBlank(message = "Processing type is required")
        @Size(max = 60, message = "Processing type must have at most 60 characters")
        String processingType,

        @NotNull(message = "Volume is required")
        @Positive(message = "Volume must be positive")
        BigDecimal destinedVolumeKg
) {
}
