package com.likun.mongo.mongotest.test;



public class Mytest {
    public static void main(String[] args) {
        SungSum sungSum = new SungSum();
        OpenAndClose openAndClose = new OpenAndClose();
        openAndClose.open(sungSum);
    }


}


interface IOpenAndClose {
     void open(ITV itv);
}
class  OpenAndClose implements  IOpenAndClose{

    @Override
    public void open(ITV itv) {
        itv.play();
    }
}

interface ITV {
    public void play();
}

class SungSum implements ITV {
    @Override
    public void play() {
        System.out.println("三星电视运行......");
    }
}
