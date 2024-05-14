package com.micropos.products.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Product implements Serializable {
    private String id;
    private String name;
    private double price;
    private Category category;

    @Override
    public String toString() {
        return getId() + "\t" + getName() + "\t" + getPrice() + "\t" + getCategory();
    }

}
