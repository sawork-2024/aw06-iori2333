package com.micropos.products.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Category implements Serializable {
    private String id;
    private String name;

    @Override
    public String toString() {
        return getId() + "\t" + getName();
    }
}
