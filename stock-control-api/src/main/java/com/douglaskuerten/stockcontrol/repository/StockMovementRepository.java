package com.douglaskuerten.stockcontrol.repository;

import com.douglaskuerten.stockcontrol.dto.ProductProfitDTO;
import com.douglaskuerten.stockcontrol.model.entity.StockMovement;
import com.douglaskuerten.stockcontrol.model.enums.MovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    @Query("""
                SELECT new com.douglaskuerten.stockcontrol.dto.ProductProfitDTO(
                    p.id,
                    p.code,
                    p.description,
                    COALESCE(SUM(m.quantity), 0),
                    COALESCE(SUM(m.saleValue * m.quantity), 0),
                    COALESCE(SUM( (m.saleValue - p.supplierValue) * m.quantity ), 0)
                )
                FROM StockMovement m
                JOIN m.product p
                WHERE m.movementType = :exitType
                GROUP BY p.id, p.code, p.description
                ORDER BY p.code
            """)
    List<ProductProfitDTO> findProfitByProduct(MovementType exitType);
}
