package org.acme.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.req.ProductReqDTO;
import org.acme.dto.res.ProductResDTO;
import org.acme.model.ProductModel;

@ApplicationScoped
public class ProductMapper {

    public ProductModel toModel(ProductReqDTO dto) {
        ProductModel model = new ProductModel();
        updateModel(dto, model);
        return model;
    }

    public void updateModel(ProductReqDTO dto, ProductModel model) {
        model.name = dto.name();
        model.barcode = dto.barcode();
        model.description = dto.description();
        model.unitOfMeasure = dto.unitOfMeasure();

    }

    public ProductResDTO toDTO(ProductModel model) {
        return new ProductResDTO(model.id, model.name, model.barcode, model.description,model.unitOfMeasure, model.createdAt);
    }
}
