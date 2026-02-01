package com.douglaskuerten.stockcontrol.controller;

import com.douglaskuerten.stockcontrol.dto.StockMovementDTO;
import com.douglaskuerten.stockcontrol.model.entity.StockMovement;
import com.douglaskuerten.stockcontrol.service.StockMovementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock-movement")
@Tag(name = "Stock Movement")
public class StockMovementController {
    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StockMovement create(@Valid @RequestBody StockMovementDTO dto) {
        return stockMovementService.create(dto);
    }

    @GetMapping("/{id}")
    public StockMovement getById(@PathVariable Long id) {
        return stockMovementService.getById(id);
    }

    @GetMapping
    public List<StockMovement> getAll() {
        return stockMovementService.getAll();
    }

    @PutMapping("/{id}")
    public StockMovement update(@PathVariable Long id, @Valid @RequestBody StockMovementDTO dto) {
        return stockMovementService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        stockMovementService.delete(id);
    }
}
