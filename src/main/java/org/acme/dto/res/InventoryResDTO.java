package org.acme.dto.res;

import org.acme.model.StatusInventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventoryResDTO(
        Long id,
        Long productId,
        BigDecimal currentQuantity,
        BigDecimal currentAverageCost,
        LocalDateTime updatedAt,
        StatusInventory statusInventory
) {
}
