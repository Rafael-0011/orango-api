package org.acme.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.InventoryDTO;
import org.acme.model.InventoryModel;
import org.acme.model.ProductModel;

@ApplicationScoped
public class InventoryMapper {

    public InventoryModel toModel(InventoryDTO dto) {
        InventoryModel model = new InventoryModel();
        updateModel(dto, model);
        return model;
    }

    public void updateModel(InventoryDTO dto, InventoryModel model) {
        model.product = dto.productId() == null ? null : ProductModel.findById(dto.productId());
        model.currentQuantity = dto.currentQuantity();
        model.currentAverageCost = dto.currentAverageCost();
        if (dto.updatedAt() != null) {
            model.updatedAt = dto.updatedAt();
        }
    }

    public InventoryDTO toDTO(InventoryModel model) {
        return new InventoryDTO(
                model.id,
                model.product == null ? null : model.product.id,
                model.currentQuantity,
                model.currentAverageCost,
                model.updatedAt);
    }
}
