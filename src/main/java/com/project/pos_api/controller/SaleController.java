package com.project.pos_api.controller;

import com.project.pos_api.dto.SaleRequest;
import com.project.pos_api.dto.SaleResponse;
import com.project.pos_api.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<SaleResponse> createSale(@RequestBody SaleRequest request) {
        SaleResponse saved = saleService.createSale(request);
        return ResponseEntity.created(URI.create("/api/sales/" + saved.getId())).body(saved);
    }

    @GetMapping
    public List<SaleResponse> all() {
        return saleService.getAllSales();
    }
}
