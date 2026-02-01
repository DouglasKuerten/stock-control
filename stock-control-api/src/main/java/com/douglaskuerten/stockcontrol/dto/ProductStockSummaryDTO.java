package com.douglaskuerten.stockcontrol.dto;

import com.douglaskuerten.stockcontrol.model.enums.ProductType;

public class ProductStockSummaryDTO {

    private final Long productId;
    private final String code;
    private final String description;
    private final ProductType productType;
    private final Integer totalExitQuantity;
    private final Integer availableQuantity;

    public ProductStockSummaryDTO(Long productId,
                                  String code,
                                  String description,
                                  ProductType productType,
                                  Long totalExitQuantity,
                                  Integer availableQuantity) {
        this.productId = productId;
        this.code = code;
        this.description = description;
        this.productType = productType;
        this.totalExitQuantity = totalExitQuantity == null ? 0 : totalExitQuantity.intValue();
        this.availableQuantity = availableQuantity == null ? 0 : availableQuantity;
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

    public ProductType getProductType() {
        return productType;
    }

    public Integer getTotalExitQuantity() {
        return totalExitQuantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }
}
