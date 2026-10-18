package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.acme.dto.InventoryTransactionDTO;
import org.acme.mapper.InventoryTransactionMapper;
import org.acme.model.InventoryTransactionModel;

@ApplicationScoped
public class InventoryTransactionService implements CrudService<InventoryTransactionDTO> {

    @Inject
    InventoryTransactionMapper mapper;

    @Override
    public List<InventoryTransactionDTO> findAll() {
        return InventoryTransactionModel.<InventoryTransactionModel>findAll().list().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public InventoryTransactionDTO findById(Long id) {
        return Optional.ofNullable(InventoryTransactionModel.findById(id))
                .map(InventoryTransactionModel.class::cast)
                .map(mapper::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public InventoryTransactionDTO create(InventoryTransactionDTO dto) {
        InventoryTransactionModel model = mapper.toModel(dto);
        model.persist();
        return mapper.toDTO(model);
    }

    @Override
    @Transactional
    public InventoryTransactionDTO update(Long id, InventoryTransactionDTO dto) {
        InventoryTransactionModel model = InventoryTransactionModel.findById(id);
        if (model == null) {
            return null;
        }
        mapper.updateModel(dto, model);
        return mapper.toDTO(model);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return InventoryTransactionModel.deleteById(id);
    }

}
