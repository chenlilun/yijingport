package com.likun.mongo.mongotest.test.decorator;

public class Decorator extends  Drink {
    public Decorator(Drink obj) {
        this.obj = obj;
    }

    private Drink obj ;
    @Override
    public float cost() {
        return super.getPrice()+ obj.getPrice();
    }

    @Override
    public String getDesc() {
        return super.getDesc()+"**"+super.getPrice()+"&&&&"+obj.getDesc();
    }
}
