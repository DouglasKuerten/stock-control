package com.douglaskuerten.stockcontrol.repository;

import com.douglaskuerten.stockcontrol.dto.ProductStockSummaryDTO;
import com.douglaskuerten.stockcontrol.model.entity.Product;
import com.douglaskuerten.stockcontrol.model.enums.MovementType;
import com.douglaskuerten.stockcontrol.model.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCode(String code);

    List<Product> findByProductType(ProductType productType);

    @Query("""
                SELECT new com.douglaskuerten.stockcontrol.dto.ProductStockSummaryDTO(
                    p.id,
                    p.code,
                    p.description,
                    p.productType,
                    COALESCE(SUM(CASE WHEN m.movementType = :exitType THEN m.quantity ELSE 0 END), 0),
                    p.stockQuantity
                )
                FROM Product p
                LEFT JOIN StockMovement m ON m.product.id = p.id
                WHERE (:productType IS NULL OR p.productType = :productType)
                GROUP BY p.id, p.code, p.description, p.productType, p.stockQuantity
                ORDER BY p.code
            """)
    List<ProductStockSummaryDTO> findStockSummaryByType(ProductType productType, MovementType exitType);
}
