package com.project.pos_api.service;

import com.project.pos_api.entity.Product;
import com.project.pos_api.exception.ResourceNotFoundException;
import com.project.pos_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository repo;
    public List<Product> findAll() { return repo.findAll(); }
    public Product create(Product p) { return repo.save(p); }
    public Product update(Long id, Product updated) {
        return repo.findById(id).map(p -> {
            p.setName(updated.getName());
            p.setPrice(updated.getPrice());
            p.setStock(updated.getStock());
            return repo.save(p);
        }).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
    public void delete(Long id) { repo.deleteById(id); }
    public Product findById(Long id) { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found")); }
}
