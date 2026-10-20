package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class ProductModel extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 100)
    public String name;

    @Column(unique = true, length = 50)
    public String barcode;

    @Column(columnDefinition = "text")
    public String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measure", nullable = false, length = 10)
    public UnitOfMeasureEnum unitOfMeasure;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    public static ProductModel findProduct(Long productId) {
        if (productId == null) {
            throw new BadRequestException("productId é obrigatório");
        }
        ProductModel product = ProductModel.findById(productId);
        if (product == null) {
            throw new NotFoundException("Produto não encontrado: " + productId);
        }
        return product;
    }

    /** Busca o saldo do produto com lock; se for a primeira movimentação, cria a linha zerada. */
    public static InventoryModel findOrCreateInventoryLocked(ProductModel product) {
        ProductModel.findById(product.id, LockModeType.PESSIMISTIC_WRITE);
        InventoryModel inventory = InventoryModel
                .find("product.id", product.id)
                .firstResult();

        if (inventory == null) {
            inventory = new InventoryModel();
            inventory.product = product;
            inventory.currentQuantity = BigDecimal.ZERO;
            inventory.currentAverageCost = BigDecimal.ZERO;
            inventory.persist();
        }
        return inventory;
    }
}
