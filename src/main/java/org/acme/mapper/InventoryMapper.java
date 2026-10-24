package org.acme.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.req.InventoryReqDTO;
import org.acme.dto.res.InventoryResDTO;
import org.acme.model.InventoryModel;
import org.acme.model.ProductModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApplicationScoped
public class InventoryMapper {

    public InventoryModel toModel(InventoryReqDTO dto) {
        InventoryModel model = new InventoryModel();
        updateModel(dto, model);
        return model;
    }

    public void updateModel(InventoryReqDTO dto, InventoryModel model) {
        model.product = dto.productId() == null ? null : ProductModel.findById(dto.productId());
        model.currentQuantity = dto.currentQuantity();
        model.currentAverageCost = dto.currentAverageCost();
        model.updatedAt = LocalDateTime.now();
        if (dto.inventoryMinimum() != null){
           model.inventoryMinimum = dto.inventoryMinimum();
        }
    }

    public InventoryResDTO toDTO(InventoryModel model) {
        return new InventoryResDTO(
                model.id,
                model.product == null ? null : model.product.id,
                model.currentQuantity,
                model.currentAverageCost,
                model.updatedAt,
                model.status
                );
    }
}
