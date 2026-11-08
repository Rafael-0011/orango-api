package org.acme.dto.res;

import org.acme.model.StatusInventory;
import org.acme.model.UnitOfMeasureEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventoryResDTO(
        Long id,
        Long productId,
        String name,
        String barcode,
        UnitOfMeasureEnum unitOfMeasure,
        BigDecimal currentQuantity,
        BigDecimal currentAverageCost,
        LocalDateTime updatedAt,
        StatusInventory statusInventory
) {
}
