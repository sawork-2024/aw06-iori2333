package com.micropos.carts.repository;

import com.micropos.carts.model.Cart;
import com.micropos.carts.model.Item;
import org.springframework.stereotype.Repository;

@Repository
public class CartRepositoryImpl implements CartRepository {
    private final Cart cart = new Cart();

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void addItem(Item item) {
        cart.addItem(item);
    }

    @Override
    public void removeItem(Item item) {
        cart.removeItem(item);
    }

    @Override
    public void modifyItem(Item item) {
        cart.modifyItem(item);
    }
}
