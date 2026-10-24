package org.acme.dto.res;

import org.acme.model.UnitOfMeasureEnum;

import java.time.LocalDateTime;

public record ProductResDTO(
        Long id,
        String name,
        String barcode,
        String description,
        UnitOfMeasureEnum unitOfMeasure,
        LocalDateTime createdAt
) {
}
