package com.likun.mongo.mongotest;

import java.util.Calendar;
import java.util.Date;

public class Test {
    public static void main(String[] args) {
        getBeforeTwoDay() ;
        System.out.println("010220220413GE07059999999999999999999907A01333300573".substring(13,14));
        final StringBuilder sb = new StringBuilder("JikonAdapterSilkCarInfoFetchEvent").append("4444request9999");
        System.out.println(sb.toString());
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
