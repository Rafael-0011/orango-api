package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.acme.dto.InventoryDTO;
import org.acme.mapper.InventoryMapper;
import org.acme.model.InventoryModel;

@ApplicationScoped
public class InventoryService implements CrudService<InventoryDTO> {

    @Inject
    InventoryMapper mapper;

    @Override
    public List<InventoryDTO> findAll() {
        return InventoryModel.<InventoryModel>findAll().list().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public InventoryDTO findById(Long id) {
        return Optional.ofNullable(InventoryModel.findById(id))
                .map(InventoryModel.class::cast)
                .map(mapper::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public InventoryDTO create(InventoryDTO dto) {
        InventoryModel model = mapper.toModel(dto);
        model.persist();
        return mapper.toDTO(model);
    }

    @Override
    @Transactional
    public InventoryDTO update(Long id, InventoryDTO dto) {
        InventoryModel model = InventoryModel.findById(id);
        if (model == null) {
            return null;
        }
        mapper.updateModel(dto, model);
        return mapper.toDTO(model);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return InventoryModel.deleteById(id);
    }

}
