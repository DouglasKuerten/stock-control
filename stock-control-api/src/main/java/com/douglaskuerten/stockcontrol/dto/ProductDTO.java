package com.douglaskuerten.stockcontrol.dto;

import com.douglaskuerten.stockcontrol.model.enums.ProductType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ProductDTO {

    @NotNull
    private String code;

    @NotNull
    private String description;

    @NotNull
    private ProductType productType;

    @NotNull
    @Positive
    private BigDecimal supplierValue;

    @NotNull
    @Min(0)
    private Integer stockQuantity;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public ProductType getProductType() {
        return productType;
    }

    public BigDecimal getSupplierValue() {
        return supplierValue;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public void setSupplierValue(BigDecimal supplierValue) {
        this.supplierValue = supplierValue;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
