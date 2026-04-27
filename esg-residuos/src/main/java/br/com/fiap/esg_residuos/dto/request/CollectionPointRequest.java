package br.com.fiap.esg_residuos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CollectionPointRequest(
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must have at most 100 characters")
    String name,

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be positive")
    BigDecimal capacityKg,

    @NotNull(message = "Alert volume is required")
    @Positive(message = "Alert volume must be positive")
    BigDecimal alertVolumeKg
) {
}
