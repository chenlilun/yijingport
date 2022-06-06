package com.likun.mongo.mongotest.test.brige.son;

import com.likun.mongo.mongotest.test.brige.Brand;
import com.likun.mongo.mongotest.test.brige.Phone;

public class FoldePhone extends Phone {
    public FoldePhone(Brand brand) {
        super(brand);
    }
    public void  open(){
        super.open();
        System.out.println("折叠式手机开机");
    }
    public void  close(){
        super.close();
        System.out.println("折叠式手机关机");
    }

    public void  call(){
        super.call();
        System.out.println("折叠式手机打电话");
    }

    public static void main(String[] args) {

    }
}
