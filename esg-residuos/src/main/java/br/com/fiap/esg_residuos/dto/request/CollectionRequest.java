package br.com.fiap.esg_residuos.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CollectionRequest(
        @NotNull(message = "Collection point is required")
        Long collectionPointId,

        @NotNull(message = "Waste is required")
        Long wasteId,

        @NotNull(message = "Volume is required")
        @Positive(message = "Volume must be positive")
        BigDecimal volumeKg
) {}
