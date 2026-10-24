package org.acme.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.acme.dto.req.InventoryReqDTO;
import org.acme.dto.PageResponse;
import org.acme.dto.res.InventoryResDTO;
import org.acme.mapper.InventoryMapper;
import org.acme.model.InventoryModel;

@ApplicationScoped
public class InventoryService implements CrudService<InventoryReqDTO, InventoryResDTO> {

    @Inject
    InventoryMapper mapper;

    @Override
    public List<InventoryResDTO> findAll() {
        return InventoryModel.<InventoryModel>findAll().list().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public PageResponse<InventoryResDTO> findAllPage(int page, int size) {
        PanacheQuery<InventoryModel> query =  InventoryModel
                .findAll(Sort.ascending("status"))
                .page(Page.of(page, size));

        List<InventoryResDTO> content = query.list().stream()
                .map(mapper::toDTO)
                .toList();

        return new PageResponse<>(content, page, size, query.count(), query.pageCount());
    }

    @Override
    public InventoryResDTO findById(Long id) {
        return Optional.ofNullable(InventoryModel.findById(id))
                .map(InventoryModel.class::cast)
                .map(mapper::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public InventoryResDTO create(InventoryReqDTO dto) {
        InventoryModel model = mapper.toModel(dto);
        model.persist();
        return mapper.toDTO(model);
    }

    @Override
    @Transactional
    public InventoryResDTO update(Long id, InventoryReqDTO dto) {
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
