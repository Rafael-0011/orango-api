package org.acme.dto.req;


import java.math.BigDecimal;

public record InventoryReqDTO(
        Long id,
        Long productId,
        BigDecimal currentQuantity,
        BigDecimal currentAverageCost,
        BigDecimal inventoryMinimum
) {
}
