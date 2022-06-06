package com.likun.mongo.mongotest.test.decorator;

import lombok.Data;

@Data
public abstract class Drink {
    public String desc ;
    private float price = 0.0f  ;
    public abstract float cost();
}
