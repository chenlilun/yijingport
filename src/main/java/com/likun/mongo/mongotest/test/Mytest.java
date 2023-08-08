package com.likun.mongo.mongotest.test;


import java.util.Date;
import java.util.stream.Stream;

public class Mytest {
    public static void main(String[] args) {
            Date date = new Date() ;
        System.out.println(date.toString());
        Date date1 = new Date(date.toString()) ;
        System.out.println(date1);
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
        //
    }

    public static void main(String[] args) {
        System.out.println(new Date().toString());
    }
}
