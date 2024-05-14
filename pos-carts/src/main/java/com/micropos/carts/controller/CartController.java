package com.micropos.carts.controller;

import com.micropos.rest.api.*;
import com.micropos.rest.dto.*;
import com.micropos.carts.mapper.CartMapper;
import com.micropos.carts.model.Item;
import com.micropos.carts.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController implements CartApi {
    private CartMapper cartMapper = CartMapper.INSTANCE;

    @Autowired
    private CartService cartService;

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<OrderDto> checkout() {
        try {
            var order = cartService.checkout();
            return new ResponseEntity<>(cartMapper.toOrderDto(order), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<ItemDto>> getCart() {
        var items = cartService.getCart().getItems();
        var cartDto = new ArrayList<>(cartMapper.toCartDto(items));
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ItemDto> getCartItem(String productId) {
        var item = cartService.getItem(productId);
        if (item == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cartMapper.toItemDto(item), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ItemDto>> removeProductFromCart(String productId) {
        cartService.removeItem(productId);
        return getCart();
    }

    @Override
    public ResponseEntity<List<ItemDto>> updateProductQuantityInCart(String productId, Integer quantity) {
        cartService.modifyItem(new Item(productId, quantity));
        return getCart();
    }

    @Override
    public ResponseEntity<List<ItemDto>> addProductToCart(String productId) {
        cartService.addToCart(new Item(productId, 1));
        return getCart();
    }
}
