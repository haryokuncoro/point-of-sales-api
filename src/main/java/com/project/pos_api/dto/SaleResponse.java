package com.project.pos_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data @Builder
public class SaleResponse {
    private Long id;
    private LocalDateTime date;
    private BigDecimal total;
    private String paymentMethod;
    private List<Item> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal price;
    }
}
