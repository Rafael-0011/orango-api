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
        assert model.product != null;
        return new InventoryResDTO(
                model.id,
                model.product.id,
                model.product.name,
                model.product.barcode,
                model.product.unitOfMeasure,
                model.currentQuantity,
                model.currentAverageCost,
                model.updatedAt,
                model.status
                );
    }
}
