package org.acme.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventoryTransactionDTO(
        Long id,
        Long productId,
        String transactionType,
        BigDecimal quantity,
        BigDecimal unitValue,
        LocalDateTime transactionDate,
        String notes
) {
}
