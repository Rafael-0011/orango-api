package org.acme.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventoryDTO(
        Long id,
        Long productId,
        BigDecimal currentQuantity,
        BigDecimal currentAverageCost,
        LocalDateTime updatedAt
) {
}
