package org.acme.dto;

import org.acme.model.UnitOfMeasureEnum;

import java.time.LocalDateTime;

public record ProductDTO(
        Long id,
        String name,
        String barcode,
        String description,
        UnitOfMeasureEnum unitOfMeasure,
        LocalDateTime createdAt
) {
}
