package com.micropos.carts.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

@Data
@AllArgsConstructor
public class Order implements Serializable {
    private Collection<Item> items;
    private double count;
    private double total;
}
