package com.douglaskuerten.stockcontrol.service;

import com.douglaskuerten.stockcontrol.dto.ProductDTO;
import com.douglaskuerten.stockcontrol.exception.NotFoundException;
import com.douglaskuerten.stockcontrol.model.entity.Product;
import com.douglaskuerten.stockcontrol.model.enums.ProductType;
import com.douglaskuerten.stockcontrol.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(ProductDTO productDTO) {
        Product product = new Product();
        product.setCode(productDTO.getCode());
        product.setDescription(productDTO.getDescription());
        product.setProductType(productDTO.getProductType());
        product.setSupplierValue(productDTO.getSupplierValue());
        product.setStockQuantity(productDTO.getStockQuantity());
        return productRepository.save(product);
    }

    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado"));
    }

    public List<Product> getAll(ProductType productType) {
        if (productType == null) return productRepository.findAll();
        return productRepository.findByProductType(productType);
    }

    @Transactional
    public Product update(Long id, ProductDTO productDTO) {
        Product existing = getById(id);
        existing.setCode(productDTO.getCode());
        existing.setDescription(productDTO.getDescription());
        existing.setProductType(productDTO.getProductType());
        existing.setSupplierValue(productDTO.getSupplierValue());
        existing.setStockQuantity(productDTO.getStockQuantity());
        return productRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Product existing = getById(id);
        productRepository.delete(existing);
    }
}
