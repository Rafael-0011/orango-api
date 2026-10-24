package org.acme.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.req.InventoryTransactionReqDTO;
import org.acme.dto.res.InventoryTransactionResDTO;
import org.acme.model.InventoryTransactionModel;

import java.time.LocalDateTime;

@ApplicationScoped
public class InventoryTransactionMapper {

    public InventoryTransactionModel toModel(InventoryTransactionReqDTO dto) {
        InventoryTransactionModel model = new InventoryTransactionModel();
        updateModel(dto, model);
        return model;
    }

    public void updateModel(InventoryTransactionReqDTO dto, InventoryTransactionModel model) {
        model.quantity = dto.quantity();
        model.unitValue = dto.unitValue();
        model.transactionDate = LocalDateTime.now();
        model.notes = dto.notes();
    }

    public InventoryTransactionResDTO toDTO(InventoryTransactionModel model) {
        return new InventoryTransactionResDTO(
                model.id,
                model.product == null ? null : model.product.id,
                model.transactionType,
                model.quantity,
                model.unitValue,
                model.cmv,
                model.transactionDate,
                model.notes);
    }
}
