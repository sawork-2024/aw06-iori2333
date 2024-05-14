package com.micropos.products.controller;

import com.micropos.products.mapper.ProductMapper;
import com.micropos.rest.api.*;
import com.micropos.rest.dto.*;
import com.micropos.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PosController implements ProductApi, CategoriesApi {
    @Autowired
    private ProductService productService;

    private ProductMapper productMapper = ProductMapper.INSTANCE;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ProductApi.super.getRequest();
    }

    @Override
    public ResponseEntity<List<ProductDto>> listProducts() {
        var products = productService.products().stream()
            .map(p -> productMapper.toProductDto(p)).toList();
        return ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity<ProductDto> showProductById(String productId) {
        return productService.products().stream()
            .filter(p -> p.getId().equals(productId))
            .findFirst()
            .map(p -> ResponseEntity.ok(productMapper.toProductDto(p)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<CategoryDto>> listCategories() {
        var categories = productService.categories().stream()
            .map(c -> new CategoryDto(c.getName(), c.getId()))
            .toList();
        return ResponseEntity.ok(categories);
    }
}
