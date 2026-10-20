package org.acme.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import org.acme.dto.InventoryDTO;
import org.acme.dto.InventoryTransactionDTO;
import org.acme.dto.PageResponse;
import org.acme.mapper.InventoryTransactionMapper;
import org.acme.model.InventoryModel;
import org.acme.model.InventoryTransactionModel;
import org.acme.model.ProductModel;
import org.acme.model.TransactionTypeEnum;

@ApplicationScoped
public class InventoryTransactionService implements CrudService<InventoryTransactionDTO> {

    @Inject
    InventoryTransactionMapper mapper;

    private static final int COST_SCALE = 4;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    @Override
    public List<InventoryTransactionDTO> findAll() {
        return InventoryTransactionModel.<InventoryTransactionModel>findAll().list().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public InventoryTransactionDTO findById(Long id) {
        return Optional.ofNullable(InventoryTransactionModel.findById(id))
                .map(InventoryTransactionModel.class::cast)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public PageResponse<InventoryTransactionDTO> findAllPage(int page, int size) {
        PanacheQuery<InventoryTransactionModel> query =  InventoryTransactionModel
                .findAll(Sort.descending("transactionDate"))
                .page(Page.of(page, size));

        List<InventoryTransactionDTO> content = query.list().stream()
                .map(mapper::toDTO)
                .toList();

        return new PageResponse<>(content, page, size, query.count(), query.pageCount());
    }

    @Override
    public InventoryTransactionDTO create(InventoryTransactionDTO dto) {
        return null;
    }

    @Transactional
    public InventoryTransactionDTO createInFlow(InventoryTransactionDTO dto) {
        ProductModel product = ProductModel.findProduct(dto.productId());
        validateUnitOfMeasure(product, dto.quantity());
        InventoryModel inventory = ProductModel.findOrCreateInventoryLocked(product);

        BigDecimal newAvgCost = calculateAverageCost(
                inventory.currentQuantity, inventory.currentAverageCost,
                dto.quantity(), dto.unitValue());

        InventoryTransactionModel model = mapper.toModel(dto);
        model.product = product;
        model.transactionType = TransactionTypeEnum.INFLOW;
        model.cmv = BigDecimal.ZERO; // entrada não gera CMV
        if (model.transactionDate == null) {
            model.transactionDate = LocalDateTime.now();
        }
        model.persist();

        inventory.currentQuantity = inventory.currentQuantity.add(dto.quantity());
        inventory.currentAverageCost = newAvgCost;
        inventory.updatedAt = LocalDateTime.now();

        return mapper.toDTO(model);
    }


    @Transactional
    public InventoryTransactionDTO createOutFlow(InventoryTransactionDTO dto) {
        ProductModel product = ProductModel.findProduct(dto.productId());
        validateUnitOfMeasure(product, dto.quantity());
        InventoryModel inventory = ProductModel.findOrCreateInventoryLocked(product);

        if (inventory.currentQuantity.compareTo(dto.quantity()) < 0) {
            throw new WebApplicationException(
                    "Estoque insuficiente. Disponível: " + inventory.currentQuantity, 422);
        }

        InventoryTransactionModel model = mapper.toModel(dto);
        model.product = product;
        model.transactionType = TransactionTypeEnum.OUTFLOW;
        model.cmv = calculateCmv(dto.quantity(), inventory.currentAverageCost);
        if (model.transactionDate == null) {
            model.transactionDate = LocalDateTime.now();
        }
        model.persist();

        // saída altera só a quantidade; o custo médio permanece
        inventory.currentQuantity = inventory.currentQuantity.subtract(dto.quantity());
        inventory.updatedAt = LocalDateTime.now();

        return mapper.toDTO(model);
    }

    @Override
    @Transactional
    public InventoryTransactionDTO update(Long id, InventoryTransactionDTO dto) {
        InventoryTransactionModel model = InventoryTransactionModel.findById(id);
        if (model == null) {
            return null;
        }
        mapper.updateModel(dto, model);
        return mapper.toDTO(model);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return InventoryTransactionModel.deleteById(id);
    }

    private BigDecimal calculateAverageCost(BigDecimal currentQty,
                                            BigDecimal currentAvgCost,
                                            BigDecimal inQty,
                                            BigDecimal inUnitValue) {
        BigDecimal totalValue = currentQty.multiply(currentAvgCost)
                .add(inQty.multiply(inUnitValue));
        BigDecimal totalQty = currentQty.add(inQty);

        return totalValue.divide(totalQty, COST_SCALE, ROUNDING);
    }

    private BigDecimal calculateCmv(BigDecimal outQty, BigDecimal avgCost) {
        return outQty.multiply(avgCost).setScale(COST_SCALE, ROUNDING);
    }

    private void validateUnitOfMeasure(ProductModel product, BigDecimal quantity) {
        if (!product.unitOfMeasure.isFractional()
                && quantity.stripTrailingZeros().scale() > 0) {
            throw new BadRequestException(
                    "Produto em " + product.unitOfMeasure + " só aceita quantidade inteira");
        }
    }
}
