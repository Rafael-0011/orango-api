package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.acme.dto.req.ProductReqDTO;
import org.acme.dto.res.ProductResDTO;
import org.acme.mapper.ProductMapper;
import org.acme.model.ProductModel;

@ApplicationScoped
public class ProductService implements CrudService<ProductReqDTO, ProductResDTO> {

    @Inject
    ProductMapper mapper;

    @Override
    public List<ProductResDTO> findAll() {
        return ProductModel.<ProductModel>findAll().list().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public ProductResDTO findById(Long id) {
        return Optional.ofNullable(ProductModel.findById(id))
                .map(ProductModel.class::cast)
                .map(mapper::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public ProductResDTO create(ProductReqDTO dto) {
        ProductModel model = mapper.toModel(dto);
        model.persist();
        return mapper.toDTO(model);
    }

    @Override
    @Transactional
    public ProductResDTO update(Long id, ProductReqDTO dto) {
        ProductModel model = ProductModel.findById(id);
        if (model == null) {
            return null;
        }
        mapper.updateModel(dto, model);
        return mapper.toDTO(model);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return ProductModel.deleteById(id);
    }

}
