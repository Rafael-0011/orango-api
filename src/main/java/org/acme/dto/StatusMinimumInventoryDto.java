package org.acme.dto;

import org.acme.model.StatusInventory;

public record StatusMinimumInventoryDto(
        StatusInventory statusInventory,
        Integer inventoryMinimum
) {
}
