package com.micropos.carts.service;

import com.micropos.carts.model.Cart;
import com.micropos.carts.model.Item;
import com.micropos.carts.model.Order;

public interface CartService {
    Cart getCart();

    Cart addToCart(Item item);

    Item getItem(String id);

    Cart removeItem(String id);

    Cart modifyItem(Item item);

    Item randomItem();

    Order checkout();
}
