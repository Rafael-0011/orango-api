package org.acme.dto;

import org.acme.model.StatusInventory;

import java.math.BigDecimal;

public record StatusMinimumInventoryDto(
        StatusInventory statusInventory,
        BigDecimal inventoryMinimum
) {
}
