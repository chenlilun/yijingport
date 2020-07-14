package com.likun.mongo.mongotest;

import java.util.Calendar;
import java.util.Date;

public class Test {
    public static void main(String[] args) {
        getBeforeTwoDay() ;
    }

    public static Date getBeforeTwoDay(){
        Date date = new Date() ;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -2);
        Date time = calendar.getTime();
        System.out.println("天： " + time.getDate());
        System.out.println("天： " + time.getTime());
        return  time  ;


    }
}
