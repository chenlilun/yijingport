package com.likun.mongo.mongotest.test.decorator.DecoratorSon;

import com.likun.mongo.mongotest.test.decorator.Decorator;
import com.likun.mongo.mongotest.test.decorator.Drink;

public class Deorator1 extends Decorator {

    public Deorator1(Drink obj) {
        super(obj);
        setDesc("Deorator1");
        setPrice(0.5f);
    }
}
