package com.douglaskuerten.stockcontrol;

import com.douglaskuerten.stockcontrol.dto.ProductDTO;
import com.douglaskuerten.stockcontrol.model.entity.Product;
import com.douglaskuerten.stockcontrol.model.enums.ProductType;
import com.douglaskuerten.stockcontrol.repository.ProductRepository;
import com.douglaskuerten.stockcontrol.service.ProductService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Test
    void shouldMapDtoToEntityOnCreate() {
        ProductRepository repo = mock(ProductRepository.class);
        ProductService service = new ProductService(repo);

        ProductDTO dto = new ProductDTO();
        dto.setCode("P001");
        dto.setDescription("Mouse");
        dto.setProductType(ProductType.ELECTRONIC);
        dto.setSupplierValue(new BigDecimal("10.00"));
        dto.setStockQuantity(7);

        when(repo.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product saved = service.create(dto);

        assertEquals("P001", saved.getCode());
        assertEquals(7, saved.getStockQuantity());
        verify(repo).save(any(Product.class));
    }
}
