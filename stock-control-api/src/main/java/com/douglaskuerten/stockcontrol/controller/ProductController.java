package com.douglaskuerten.stockcontrol.controller;

import com.douglaskuerten.stockcontrol.dto.ProductDTO;
import com.douglaskuerten.stockcontrol.model.entity.Product;
import com.douglaskuerten.stockcontrol.model.enums.ProductType;
import com.douglaskuerten.stockcontrol.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@Valid @RequestBody ProductDTO productDTO) {
        return productService.create(productDTO);
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping
    public List<Product> getAll(@RequestParam(required = false) ProductType productType) {
        return productService.getAll(productType);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return productService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
