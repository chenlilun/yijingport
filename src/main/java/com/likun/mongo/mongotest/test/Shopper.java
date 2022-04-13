package com.likun.mongo.mongotest.test;

import lombok.Data;

import java.util.List;

@Data
public class Shopper {
    private  String name ;
    private List<Product> productList  ;
}
