package org.acme.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.acme.model.UnitOfMeasureEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductReqDTO(
        Long id,
        String name,
        String barcode,
        String description,
        UnitOfMeasureEnum unitOfMeasure,
        LocalDateTime createdAt,
        @Positive(message = "A Minimo deve ser maior do que zero")
        @NotNull(message = "A Minimo Não pode ser nulo")
        BigDecimal inventoryMinimum

) {
}
