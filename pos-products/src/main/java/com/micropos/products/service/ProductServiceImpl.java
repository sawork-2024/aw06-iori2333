package com.micropos.products.service;

import com.micropos.products.repository.ProductDB;
import com.micropos.products.model.Category;
import com.micropos.products.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    private ProductDB productDB;

    @Autowired
    public void setPosDB(ProductDB productDB) {
        this.productDB = productDB;
    }

    @Override
    public List<Product> products() {
        return productDB.getProducts();
    }

    @Override
    public List<Category> categories() {
        return productDB.getCategories();
    }
}
