package org.acme.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.ProductDTO;
import org.acme.model.ProductModel;

@ApplicationScoped
public class ProductMapper {

    public ProductModel toModel(ProductDTO dto) {
        ProductModel model = new ProductModel();
        updateModel(dto, model);
        return model;
    }

    public void updateModel(ProductDTO dto, ProductModel model) {
        model.name = dto.name();
        model.barcode = dto.barcode();
        model.description = dto.description();
        if (dto.createdAt() != null) {
            model.createdAt = dto.createdAt();
        }
    }

    public ProductDTO toDTO(ProductModel model) {
        return new ProductDTO(model.id, model.name, model.barcode, model.description, model.createdAt);
    }
}
