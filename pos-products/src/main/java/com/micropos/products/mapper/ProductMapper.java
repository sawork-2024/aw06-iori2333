package com.micropos.products.mapper;

import com.micropos.rest.dto.*;
import com.micropos.products.model.Product;
import com.micropos.products.model.Category;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Collection<ProductDto> toProductsDto(Collection<Product> products);

    Collection<Product> toProducts(Collection<ProductDto> products);

    Product toProduct(ProductDto productDto);

    ProductDto toProductDto(Product pet);

    CategoryDto toCategoryDto(Category category);

    Category toCategory(CategoryDto categoryDto);
}
