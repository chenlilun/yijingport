package com.likun.mongo.mongotest.test.singleton.type01;

import java.util.Calendar;

//静态内部类推荐使用
public class Singleton07 {
    private Singleton07(){}
    private static class  SingletonInner{  //类加载时候不会加载 ，底层虚拟
        {

        }
        private final   static  Singleton07 instance = new Singleton07() ;
    }
    public static synchronized Singleton07 getInstance(){  //调用该方法才会加载
        return   SingletonInner .instance ;
    }

    public static void main(String[] args) {
        Singleton08 instance = Singleton08.INSTANCE;
        Singleton08 instance1 = Singleton08.INSTANCE;
        System.out.println(instance.hashCode()+"****"+instance1.hashCode());
    }
}


//枚举模式 推荐
enum Singleton08{
    INSTANCE;
    public void cry(){
        System.out.println("" +
                "kkkkkk");
    }
}

