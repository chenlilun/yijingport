package com.likun.mongo.mongotest.test.singleton.type01;

public class Singleton01 { //静态常量
    private Singleton01() {
    }

    private final static Singleton01 instance = new Singleton01();

    public static Singleton01 getInstance() {
        return instance;
    }
}


