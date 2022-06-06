package com.likun.mongo.mongotest.test.decorator.DecoratorSon;

import com.likun.mongo.mongotest.test.decorator.Decorator;
import com.likun.mongo.mongotest.test.decorator.Drink;

public class Deorator2 extends Decorator {

    public Deorator2(Drink obj) {
        super(obj);
        setDesc("Deorator2");
        setPrice(2.5f);
    }
}
