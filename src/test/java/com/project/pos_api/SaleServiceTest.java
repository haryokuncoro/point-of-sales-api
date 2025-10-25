package com.project.pos_api;

import com.project.pos_api.dto.SaleRequest;
import com.project.pos_api.dto.SaleResponse;
import com.project.pos_api.entity.Product;
import com.project.pos_api.entity.Sale;
import com.project.pos_api.repository.ProductRepository;
import com.project.pos_api.repository.SaleRepository;
import com.project.pos_api.service.SaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaleServiceTest {

    private SaleRepository saleRepository;
    private ProductRepository productRepository;
    private SaleService saleService;

    @BeforeEach
    void setup() {
        saleRepository = mock(SaleRepository.class);
        productRepository = mock(ProductRepository.class);
        saleService = new SaleService(saleRepository, productRepository);
    }

    @Test
    void testCreateSale() {
        Product p = Product.builder().id(1L).name("Espresso").price(BigDecimal.valueOf(20)).stock(10).build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));
        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SaleRequest.Item item = new SaleRequest.Item(1L, 2);
        SaleRequest request = new SaleRequest(List.of(item), "CASH");

        SaleResponse response = saleService.createSale(request);

        assertEquals(1, response.getItems().size());
        assertEquals(BigDecimal.valueOf(20), response.getItems().get(0).getPrice());
        verify(productRepository, times(1)).save(any());
        verify(saleRepository, times(1)).save(any());
    }
}
