package com.micropos.products.service;

import com.micropos.products.repository.ProductDB;
import com.micropos.products.model.Category;
import com.micropos.products.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private CircuitBreakerFactory factory;

    private ProductDB productDB;

    @Autowired
    public void setPosDB(ProductDB productDB) {
        this.productDB = productDB;
    }

    @Override
    public List<Product> products() {
        CircuitBreaker circuitBreaker = factory.create("circuitbreaker");

        return circuitBreaker.run(() -> productDB.getProducts(), throwable -> List.of());
    }

    @Override
    public List<Category> categories() {
        return productDB.getCategories();
    }
}
