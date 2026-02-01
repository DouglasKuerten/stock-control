package com.douglaskuerten.stockcontrol;

import com.douglaskuerten.stockcontrol.model.entity.Product;
import com.douglaskuerten.stockcontrol.model.entity.StockMovement;
import com.douglaskuerten.stockcontrol.model.enums.MovementType;
import com.douglaskuerten.stockcontrol.model.enums.ProductType;
import com.douglaskuerten.stockcontrol.repository.ProductRepository;
import com.douglaskuerten.stockcontrol.repository.StockMovementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class ReportQueriesTest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    StockMovementRepository stockMovementRepository;

    @Test
    void profitReportShouldReturnTotalExitAndProfit() {
        Product p = new Product();
        p.setCode("P001");
        p.setDescription("Mouse");
        p.setProductType(ProductType.ELECTRONIC);
        p.setSupplierValue(new BigDecimal("10.00"));
        p.setStockQuantity(100);
        p = productRepository.save(p);

        StockMovement exit = new StockMovement();
        exit.setProduct(p);
        exit.setMovementType(MovementType.EXIT);
        exit.setQuantity(2);
        exit.setSaleValue(new BigDecimal("15.00"));
        exit.setSaleDate(LocalDateTime.now());
        stockMovementRepository.save(exit);

        var result = stockMovementRepository.findProfitByProduct(MovementType.EXIT);

        assertFalse(result.isEmpty());
        assertEquals(2, result.get(0).getTotalExitQuantity());
        assertEquals(new BigDecimal("10.00"), result.get(0).getTotalProfit());
    }
}
