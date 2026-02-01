package com.douglaskuerten.stockcontrol;

import com.douglaskuerten.stockcontrol.dto.StockMovementDTO;
import com.douglaskuerten.stockcontrol.exception.InsufficientStockException;
import com.douglaskuerten.stockcontrol.model.entity.Product;
import com.douglaskuerten.stockcontrol.model.enums.MovementType;
import com.douglaskuerten.stockcontrol.repository.ProductRepository;
import com.douglaskuerten.stockcontrol.repository.StockMovementRepository;
import com.douglaskuerten.stockcontrol.service.StockMovementService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StockMovementServiceTest {

    @Test
    void shouldThrowWhenExitQuantityIsGreaterThanStock() {
        StockMovementRepository movementRepo = mock(StockMovementRepository.class);
        ProductRepository productRepo = mock(ProductRepository.class);

        StockMovementService service = new StockMovementService(movementRepo, productRepo);

        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(5);

        when(productRepo.findById(1L)).thenReturn(Optional.of(product));

        StockMovementDTO dto = new StockMovementDTO();
        dto.setProductId(1L);
        dto.setMovementType(MovementType.EXIT);
        dto.setQuantity(10);
        dto.setSaleValue(new BigDecimal("100.00"));

        assertThrows(InsufficientStockException.class, () -> service.create(dto));

        verify(movementRepo, never()).save(any());
    }

    @Test
    void shouldIncreaseStockOnEntry() {
        StockMovementRepository movementRepo = mock(StockMovementRepository.class);
        ProductRepository productRepo = mock(ProductRepository.class);

        StockMovementService service = new StockMovementService(movementRepo, productRepo);

        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(5);

        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(productRepo.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        when(movementRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        StockMovementDTO dto = new StockMovementDTO();
        dto.setProductId(1L);
        dto.setMovementType(MovementType.ENTRY);
        dto.setQuantity(3);

        service.create(dto);

        assertEquals(8, product.getStockQuantity());
    }
}
