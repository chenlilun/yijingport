package com.likun.mongo.mongotest.test.brige.son;

import com.likun.mongo.mongotest.test.brige.Brand;

public class Vivo  implements Brand {
    @Override
    public void open() {
        System.out.println("VIVO open");
    }

    @Override
    public void call() {
        System.out.println("VIVO call");
    }

    @Override
    public void close() {
        System.out.println("VIVO close");
    }
}
