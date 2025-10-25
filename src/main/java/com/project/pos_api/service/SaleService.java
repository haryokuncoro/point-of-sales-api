package com.project.pos_api.service;

import com.project.pos_api.dto.SaleRequest;
import com.project.pos_api.dto.SaleResponse;
import com.project.pos_api.entity.Product;
import com.project.pos_api.entity.Sale;
import com.project.pos_api.entity.SaleItem;
import com.project.pos_api.exception.ResourceNotFoundException;
import com.project.pos_api.repository.ProductRepository;
import com.project.pos_api.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    @Transactional
    public SaleResponse createSale(SaleRequest request) {
        Sale sale = new Sale();
        sale.setDate(LocalDateTime.now());
        sale.setPaymentMethod(request.getPaymentMethod());
        List<SaleItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (SaleRequest.Item it : request.getItems()) {
            Product product = productRepository.findById(it.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            if (product.getStock() < it.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - it.getQuantity());
            productRepository.save(product);
            BigDecimal linePrice = product.getPrice().multiply(BigDecimal.valueOf(it.getQuantity()));
            total = total.add(linePrice);
            SaleItem si = SaleItem.builder()
                    .product(product)
                    .quantity(it.getQuantity())
                    .price(product.getPrice())
                    .sale(sale)
                    .build();
            items.add(si);
        }
        sale.setItems(items);
        sale.setTotal(total);
        Sale saved = saleRepository.save(sale);
        List<SaleResponse.Item> responseItems = saved.getItems().stream()
                .map(i -> new SaleResponse.Item(i.getProduct().getId(), i.getProduct().getName(), i.getQuantity(), i.getPrice()))
                .collect(Collectors.toList());
        return SaleResponse.builder()
                .id(saved.getId())
                .date(saved.getDate())
                .total(saved.getTotal())
                .paymentMethod(saved.getPaymentMethod())
                .items(responseItems)
                .build();
    }

    public List<SaleResponse> getAllSales() {
        return saleRepository.findAll().stream().map(s -> {
            List<SaleResponse.Item> items = s.getItems().stream()
                    .map(i -> new SaleResponse.Item(i.getProduct().getId(), i.getProduct().getName(), i.getQuantity(), i.getPrice()))
                    .collect(Collectors.toList());
            return SaleResponse.builder()
                    .id(s.getId())
                    .date(s.getDate())
                    .total(s.getTotal())
                    .paymentMethod(s.getPaymentMethod())
                    .items(items)
                    .build();
        }).collect(Collectors.toList());
    }
}
