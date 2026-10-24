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
import jakarta.ws.rs.WebApplicationException;
import org.acme.dto.req.InventoryTransactionReqDTO;
import org.acme.dto.PageResponse;
import org.acme.dto.res.InventoryTransactionResDTO;
import org.acme.mapper.InventoryTransactionMapper;
import org.acme.model.*;

@ApplicationScoped
public class InventoryTransactionService implements CrudService<InventoryTransactionReqDTO, InventoryTransactionResDTO> {

    @Inject
    InventoryTransactionMapper mapper;

    private static final int COST_SCALE = 4;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    @Override
    public List<InventoryTransactionResDTO> findAll() {
        return InventoryTransactionModel.<InventoryTransactionModel>findAll().list().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public InventoryTransactionResDTO findById(Long id) {
        return Optional.ofNullable(InventoryTransactionModel.findById(id))
                .map(InventoryTransactionModel.class::cast)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public PageResponse<InventoryTransactionResDTO> findAllPage(int page, int size) {
        PanacheQuery<InventoryTransactionModel> query =  InventoryTransactionModel
                .findAll(Sort.descending("transactionDate"))
                .page(Page.of(page, size));

        List<InventoryTransactionResDTO> content = query.list().stream()
                .map(mapper::toDTO)
                .toList();

        return new PageResponse<>(content, page, size, query.count(), query.pageCount());
    }

    @Override
    public InventoryTransactionResDTO create(InventoryTransactionReqDTO dto) {
        return null;
    }

    @Transactional
    public InventoryTransactionResDTO createInFlow(InventoryTransactionReqDTO dto) {
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

        statusAttention(inventory);

        return mapper.toDTO(model);
    }


    @Transactional
    public InventoryTransactionResDTO createOutFlow(InventoryTransactionReqDTO dto) {
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

        statusAttention(inventory);

        return mapper.toDTO(model);
    }

    @Override
    @Transactional
    public InventoryTransactionResDTO update(Long id, InventoryTransactionReqDTO dto) {
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

    private void statusAttention(InventoryModel inventory) {
        if (inventory.inventoryMinimum.compareTo(inventory.currentQuantity) >= 0){
            inventory.status = StatusInventory.CRITICAL;
        }
        else {
            inventory.status = StatusInventory.NORMAL;
        }
    }
}
