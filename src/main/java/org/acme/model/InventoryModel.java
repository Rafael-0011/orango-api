package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
public class InventoryModel extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne
    @JoinColumn(name = "product_id", unique = true)
    public ProductModel product;

    @Column(name = "current_quantity", nullable = false, precision = 12, scale = 4)
    public BigDecimal currentQuantity = BigDecimal.ZERO;

    @Column(name = "current_average_cost", nullable = false, precision = 10, scale = 2)
    public BigDecimal currentAverageCost = BigDecimal.ZERO;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

}
