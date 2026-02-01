package com.douglaskuerten.stockcontrol.service;

import com.douglaskuerten.stockcontrol.dto.ProductProfitDTO;
import com.douglaskuerten.stockcontrol.dto.ProductStockSummaryDTO;
import com.douglaskuerten.stockcontrol.model.enums.MovementType;
import com.douglaskuerten.stockcontrol.model.enums.ProductType;
import com.douglaskuerten.stockcontrol.repository.ProductRepository;
import com.douglaskuerten.stockcontrol.repository.StockMovementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;

    public ReportService(ProductRepository productRepository,
                         StockMovementRepository stockMovementRepository) {
        this.productRepository = productRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    public List<ProductStockSummaryDTO> getProductsByTypeWithExitAndAvailable(ProductType productType) {
        return productRepository.findStockSummaryByType(productType, MovementType.EXIT);
    }

    public List<ProductProfitDTO> getProfitByProduct() {
        return stockMovementRepository.findProfitByProduct(MovementType.EXIT);
    }
}
