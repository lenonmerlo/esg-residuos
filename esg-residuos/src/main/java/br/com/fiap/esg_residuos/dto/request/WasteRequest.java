package br.com.fiap.esg_residuos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WasteRequest(
        @NotBlank(message = "Waste type is required")
        @Size(max = 30, message = "Waste type must have at most 30 characters")
        String wasteType,

        @Size(max = 200, message = "Description must have at most 200 characters")
        String description
) {
}
