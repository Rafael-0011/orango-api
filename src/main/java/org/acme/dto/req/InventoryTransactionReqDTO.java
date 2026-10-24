package org.acme.dto.req;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record InventoryTransactionReqDTO(
        Long id,
        Long productId,
        @Positive
        BigDecimal quantity,
        @Positive
        BigDecimal unitValue,
        String notes
) {
}
