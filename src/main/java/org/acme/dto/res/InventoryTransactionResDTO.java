package org.acme.dto.res;

import jakarta.validation.constraints.Positive;
import org.acme.model.TransactionTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventoryTransactionResDTO(
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
