package org.acme.dto.req;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record InventoryTransactionReqDTO(
        Long id,
        Long productId,
        @Positive(message = "A quantidade deve ser maior do que zero")
        BigDecimal quantity,
        @Positive(message = "A Valor unitario deve ser maior do que zero")
        BigDecimal unitValue,
        String notes,
        BigDecimal inventoryMinimum

) {
}
