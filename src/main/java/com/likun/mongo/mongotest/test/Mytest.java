package com.likun.mongo.mongotest.test;

import org.springframework.util.ObjectUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Mytest {
    public static void main(String[] args) {
        String a = "0JFN1001A011E";
        int length = a.length();
        System.out.println(a.substring(length - 5, length - 4));
   /*     int num =(int)(Math.random()*(10));
        System.out.println(num);


        String aa = "0JFH1002A0119E" ;
        System.out.println(aa.substring(0, 12)+"****"+aa.substring(13));
        HashSet a = new HashSet() ;
        a.add("a");
        a.add("a");
        a.add("b");
        System.out.println(a.size());
        List arr = new ArrayList() ;
        arr.add("a");
        arr.add("b");
        arr.add("c");
        arr.stream().filter(i->i.equals("a")).forEach(System.out::print);*/
        String d = null;
        System.out.println(ObjectUtils.nullSafeEquals("A", d));
        List<Integer> arr = Stream.of(1, 2, 3, 4).collect(Collectors.toList());
        for (int i = 0; i < arr.size(); i++) {
            for (int j = i + 1; j < arr.size(); j++) {
//                System.out.println(arr.get(i)+""+arr.get(j));
            }
        }
    }
}
