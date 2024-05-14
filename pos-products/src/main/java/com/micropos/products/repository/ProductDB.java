package com.micropos.products.repository;

import com.micropos.products.model.Category;
import com.micropos.products.model.Product;

import java.util.List;

public interface ProductDB {
    List<Product> getProducts();

    Product getProduct(String productId);

    List<Category> getCategories();

    Category getCategory(String categoryName);
}
