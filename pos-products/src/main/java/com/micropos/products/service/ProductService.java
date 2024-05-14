package com.micropos.products.service;

import com.micropos.products.model.Category;
import com.micropos.products.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> products();

    List<Category> categories();
}
