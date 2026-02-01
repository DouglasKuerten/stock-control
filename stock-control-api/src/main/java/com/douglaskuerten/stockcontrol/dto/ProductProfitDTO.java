package com.douglaskuerten.stockcontrol.dto;

import java.math.BigDecimal;

public class ProductProfitDTO {

    private final Long productId;
    private final String code;
    private final String description;
    private final Integer totalExitQuantity;
    private final BigDecimal totalSalesValue;
    private final BigDecimal totalProfit;

    public ProductProfitDTO(Long productId,
                            String code,
                            String description,
                            Long totalExitQuantity,
                            BigDecimal totalSalesValue,
                            BigDecimal totalProfit) {
        this.productId = productId;
        this.code = code;
        this.description = description;
        this.totalExitQuantity = totalExitQuantity == null ? 0 : totalExitQuantity.intValue();
        this.totalSalesValue = totalSalesValue == null ? BigDecimal.ZERO : totalSalesValue;
        this.totalProfit = totalProfit == null ? BigDecimal.ZERO : totalProfit;
    }

    public Long getProductId() {
        return productId;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public Integer getTotalExitQuantity() {
        return totalExitQuantity;
    }

    public BigDecimal getTotalSalesValue() {
        return totalSalesValue;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }
}
