package com.micropos.products.repository;

import com.micropos.products.model.Category;
import com.micropos.products.model.Product;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductInMemoryDB implements ProductDB {
    private final List<Product> products = new ArrayList<>();
    private final Map<String, Category> categories = new HashMap<>();

    @Override
    @Cacheable(cacheNames = "products")
    public List<Product> getProducts() {
        return products;
    }

    @Override
    @Cacheable(cacheNames = "product", key = "#productId")
    public Product getProduct(String productId) {
        for (Product p : getProducts()) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Category> getCategories() {
        return new ArrayList<>(categories.values());
    }

    @Override
    public Category getCategory(String categoryName) {
        return categories.get(categoryName);
    }

    ProductInMemoryDB() {
        var drinkCategory = new Category("1711853606", "drink");
        this.products.add(new Product("1", "cola", 3, drinkCategory));
        this.products.add(new Product("2", "sprite", 3, drinkCategory));
        this.products.add(new Product("3", "red bull", 5, drinkCategory));
        this.products.add(new Product("3", "Milk", 5, drinkCategory));

        this.categories.put("drink", new Category("1711853606", "drink"));
    }
}
