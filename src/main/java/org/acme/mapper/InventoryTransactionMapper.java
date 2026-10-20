package org.acme.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.InventoryTransactionDTO;
import org.acme.model.InventoryTransactionModel;
import org.acme.model.ProductModel;

@ApplicationScoped
public class InventoryTransactionMapper {

    public InventoryTransactionModel toModel(InventoryTransactionDTO dto) {
        InventoryTransactionModel model = new InventoryTransactionModel();
        updateModel(dto, model);
        return model;
    }

    public void updateModel(InventoryTransactionDTO dto, InventoryTransactionModel model) {
        model.transactionType = dto.transactionType();
        model.quantity = dto.quantity();
        model.unitValue = dto.unitValue();
        if (dto.transactionDate() != null) {
            model.transactionDate = dto.transactionDate();
        }
        model.notes = dto.notes();
    }

    public InventoryTransactionDTO toDTO(InventoryTransactionModel model) {
        return new InventoryTransactionDTO(
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
