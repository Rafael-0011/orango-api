package org.acme.dto.req;

import org.acme.model.UnitOfMeasureEnum;

import java.time.LocalDateTime;

public record ProductReqDTO(
        Long id,
        String name,
        String barcode,
        String description,
        UnitOfMeasureEnum unitOfMeasure,
        LocalDateTime createdAt
) {
}
