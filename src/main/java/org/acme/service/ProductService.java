package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.acme.dto.ProductDTO;
import org.acme.mapper.ProductMapper;
import org.acme.model.ProductModel;

@ApplicationScoped
public class ProductService implements CrudService<ProductDTO> {

    @Inject
    ProductMapper mapper;

    @Override
    public List<ProductDTO> findAll() {
        return ProductModel.<ProductModel>findAll().list().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public ProductDTO findById(Long id) {
        return Optional.ofNullable(ProductModel.findById(id))
                .map(ProductModel.class::cast)
                .map(mapper::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public ProductDTO create(ProductDTO dto) {
        ProductModel model = mapper.toModel(dto);
        model.persist();
        return mapper.toDTO(model);
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
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
