package org.acme.dto;

import java.time.LocalDateTime;

public record ProductDTO(
        Long id,
        String name,
        String barcode,
        String description,
        LocalDateTime createdAt
) {
}
