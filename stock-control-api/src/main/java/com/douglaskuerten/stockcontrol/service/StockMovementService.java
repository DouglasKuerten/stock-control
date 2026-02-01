package com.douglaskuerten.stockcontrol.service;

import com.douglaskuerten.stockcontrol.dto.StockMovementDTO;
import com.douglaskuerten.stockcontrol.exception.InsufficientStockException;
import com.douglaskuerten.stockcontrol.exception.NotFoundException;
import com.douglaskuerten.stockcontrol.model.entity.Product;
import com.douglaskuerten.stockcontrol.model.entity.StockMovement;
import com.douglaskuerten.stockcontrol.model.enums.MovementType;
import com.douglaskuerten.stockcontrol.repository.ProductRepository;
import com.douglaskuerten.stockcontrol.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockMovementService {
    private final StockMovementRepository movementRepository;
    private final ProductRepository productRepository;

    public StockMovementService(StockMovementRepository movementRepository, ProductRepository productRepository) {
        this.movementRepository = movementRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public StockMovement create(StockMovementDTO stockMovementDTO) {
        Product product = productRepository.findById(stockMovementDTO.getProductId()).orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        moveStock(product, stockMovementDTO);

        productRepository.save(product);

        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setMovementType(stockMovementDTO.getMovementType());
        movement.setQuantity(stockMovementDTO.getQuantity());
        movement.setSaleValue(stockMovementDTO.getSaleValue());
        movement.setSaleDate(stockMovementDTO.getSaleDate() != null ? stockMovementDTO.getSaleDate() : LocalDateTime.now());

        return movementRepository.save(movement);
    }

    public StockMovement getById(Long id) {
        return movementRepository.findById(id).orElseThrow(() -> new NotFoundException("Movimentação no Estoque não encontrada"));
    }

    public List<StockMovement> getAll() {
        return movementRepository.findAll();
    }

    public StockMovement update(Long id, StockMovementDTO stockMovementDTO) {
        StockMovement existing = getById(id);

        Product oldProduct = existing.getProduct();

        revertMovementFromStock(oldProduct, existing.getMovementType(), existing.getQuantity());

        if (!oldProduct.getId().equals(stockMovementDTO.getProductId())) {
            productRepository.save(oldProduct);

            Product newProduct = productRepository.findById(stockMovementDTO.getProductId())
                    .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

            moveStock(newProduct, stockMovementDTO);
            productRepository.save(newProduct);

            existing.setProduct(newProduct);
        } else {
            moveStock(oldProduct, stockMovementDTO);
            productRepository.save(oldProduct);
        }
        existing.setMovementType(stockMovementDTO.getMovementType());
        existing.setQuantity(stockMovementDTO.getQuantity());
        existing.setSaleValue(stockMovementDTO.getSaleValue());
        existing.setSaleDate(stockMovementDTO.getSaleDate() != null ? stockMovementDTO.getSaleDate() : existing.getSaleDate());

        return movementRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        StockMovement existing = getById(id);

        Product product = existing.getProduct();
        revertMovementFromStock(product, existing.getMovementType(), existing.getQuantity());

        productRepository.save(product);
        movementRepository.delete(existing);
    }

    private void moveStock(Product product, StockMovementDTO stockMovementDTO) {
        if (stockMovementDTO.getMovementType() == MovementType.EXIT) {
            checkAvaliableStock(product, stockMovementDTO.getQuantity());
            validateOutboundMovement(stockMovementDTO);
            product.setStockQuantity(product.getStockQuantity() - stockMovementDTO.getQuantity());
        } else {
            product.setStockQuantity(product.getStockQuantity() + stockMovementDTO.getQuantity());
        }
    }

    private void checkAvaliableStock(Product product, int requestedQuantity) {
        int available = product.getStockQuantity() != null ? product.getStockQuantity() : 0;

        if (available < requestedQuantity) {
            throw new InsufficientStockException("Estoque Insuficiente. Disponível: " + available + ", Solicitado: " + requestedQuantity);
        }
    }

    private void validateOutboundMovement(StockMovementDTO stockMovementDTO) {
        if (stockMovementDTO.getSaleValue() == null) {
            throw new IllegalArgumentException("O valor de venda é obrigatório quando é uma movimentação de saída");
        }
    }

    private void revertMovementFromStock(Product product, MovementType oldType, int oldQuantity) {
        if (oldType == MovementType.EXIT) {
            product.setStockQuantity(product.getStockQuantity() + oldQuantity);
            return;
        }

        if (product.getStockQuantity() < oldQuantity) {
            throw new IllegalStateException(
                    "Não é possivel reverter a movimentação de entrada porque isso tornária o estoque negativo. Atual: "
                            + product.getStockQuantity() + ", ao reverter: " + oldQuantity
            );
        }
        product.setStockQuantity(product.getStockQuantity() - oldQuantity);
    }
}
