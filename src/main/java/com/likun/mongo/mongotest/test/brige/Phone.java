package com.likun.mongo.mongotest.test.brige;

public abstract class Phone {
    public Brand brand ;

    public Phone(Brand brand) {
        super();
        this.brand = brand;
    }
    protected void open(){
        this.brand.open();
    }
    protected void call(){
        this.brand.call();
    }
    protected void close(){
        this.brand.close();
    }
}
