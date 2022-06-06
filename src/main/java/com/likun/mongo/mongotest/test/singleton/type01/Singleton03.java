package com.likun.mongo.mongotest.test.singleton.type01;

import sun.security.jca.GetInstance;

public class Singleton03 { //懒汉式 线程不安全
    private Singleton03() {
    }

    private static Singleton03 instance;

    public static Singleton03 getInstance() {
        if (instance == null) {
            instance = new Singleton03();
        }
        return instance;
    }
}

class Singleton04 { // 线程安全   新问题：效率低啊  执行getInstance()方法每次都要同步 不推荐
    private Singleton04() {
    }

    private static Singleton04 instance;

    public static synchronized Singleton04 getInstance() {
        if (instance == null) {
            instance = new Singleton04();
        }
        return instance;
    }
}

class Singleton05 { // 线程安全   双重检查  强烈推荐使用
    private Singleton05() {
    }

    private static volatile Singleton05 instance;   //volatile 共享变量 只要有变化就共享  轻量类型的 synchronized

    public static Singleton05 getInstance() {
        if (instance == null) {
            synchronized (Singleton05.class) {
                if (instance == null) {
                    instance = new Singleton05();
                }
            }
        }
        return instance;
    }
}
