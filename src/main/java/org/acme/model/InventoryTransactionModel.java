package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transactions")
public class InventoryTransactionModel extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    public ProductModel product;

    @Column(name = "transaction_type", length = 10)
    public String transactionType;

    @Column(nullable = false, precision = 12, scale = 4)
    public BigDecimal quantity;

    @Column(name = "unit_value", nullable = false, precision = 10, scale = 2)
    public BigDecimal unitValue;

    @Column(name = "transaction_date")
    public LocalDateTime transactionDate;

    @Column(columnDefinition = "text")
    public String notes;
}
