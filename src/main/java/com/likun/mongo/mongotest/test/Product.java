package com.likun.mongo.mongotest.test;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class Product {
    long  id ;
    Integer num;
    BigDecimal price;
    String name;
    String category;
    int orderBy;

    public Product(Long id, Integer num, BigDecimal price, String name, String category) {
        this.id = id;
        this.num = num;
        this.price = price;
        this.name = name;
        this.category = category;
    }


}
