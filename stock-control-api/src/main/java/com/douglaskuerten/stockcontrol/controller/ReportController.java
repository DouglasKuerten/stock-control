package com.douglaskuerten.stockcontrol.controller;

import com.douglaskuerten.stockcontrol.dto.ProductProfitDTO;
import com.douglaskuerten.stockcontrol.dto.ProductStockSummaryDTO;
import com.douglaskuerten.stockcontrol.model.enums.ProductType;
import com.douglaskuerten.stockcontrol.service.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/products")
    public List<ProductStockSummaryDTO> productsByType(
            @RequestParam(required = false) ProductType productType
    ) {
        return reportService.getProductsByTypeWithExitAndAvailable(productType);
    }

    @GetMapping("/profit")
    public List<ProductProfitDTO> profitByProduct() {
        return reportService.getProfitByProduct();
    }
}
