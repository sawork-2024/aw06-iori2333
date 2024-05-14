package com.micropos.carts.mapper;

import com.micropos.carts.model.Order;
import com.micropos.rest.api.*;
import com.micropos.rest.dto.*;
import com.micropos.carts.model.Cart;
import com.micropos.carts.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

@Mapper
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    Collection<ItemDto> toCartDto(Collection<Item> items);

    Collection<Item> toCart(Collection<ItemDto> cartItems);

    ItemDto toItemDto(Item cartItem);

    Item toItem(ItemDto cartItem);

    OrderDto toOrderDto(Order order);

    Order toOrder(OrderDto order);
}
