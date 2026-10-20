package org.acme.dto;

import jakarta.validation.constraints.Positive;
import org.acme.model.TransactionTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventoryTransactionDTO(
        Long id,
        Long productId,
        TransactionTypeEnum transactionType,
        @Positive
        BigDecimal quantity,
        @Positive
        BigDecimal unitValue,
        BigDecimal cmv,
        LocalDateTime transactionDate,
        String notes
) {
}
