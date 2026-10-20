package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", length = 10)
    public TransactionTypeEnum transactionType;

    @Column(nullable = false, precision = 12, scale = 4)
    public BigDecimal quantity;

    @Column(name = "unit_value", nullable = false, precision = 10, scale = 2)
    public BigDecimal unitValue;

    @Column(name = "cmv", nullable = false, precision = 12, scale = 2)
    public BigDecimal cmv = BigDecimal.ZERO;

    @Column(name = "transaction_date")
    public LocalDateTime transactionDate;

    @Column(columnDefinition = "text")
    public String notes;
}
