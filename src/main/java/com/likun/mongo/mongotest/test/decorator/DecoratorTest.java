package com.likun.mongo.mongotest.test.decorator;

import com.likun.mongo.mongotest.test.decorator.DecoratorSon.Deorator1;

public class DecoratorTest {
    public static void main(String[] args) {
        //Coffee1  + 2 Deorator1 + 1 个 Deorator2
        Drink order = new Coffee1();
        System.out.println(order.getDesc()+"****"+order.cost());
         order = new Deorator1(order);

        System.out.println(order.getDesc()+"****"+order.cost());
    }
}
