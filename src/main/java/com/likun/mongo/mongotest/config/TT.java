package com.likun.mongo.mongotest.config;

import org.apache.commons.lang.text.StrBuilder;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TT {
    public static void main(String[] args) {
        String b  = null   ;
//        String a  = b.toUpperCase();
        System.out.println(ObjectUtils.nullSafeEquals(b, "a"));
        StrBuilder strBuilder = new StrBuilder();
      strBuilder.append("aa") ;
      strBuilder.append("bb") ;
      strBuilder.append("bd") ;
        System.out.println(strBuilder.toString());
        List<String> items = Arrays.asList(
                "apple", "apple",
                "orange", "orange", "orange",
                "blueberry",
                "peach", "peach", "peach", "peach"
        );
        // 分组，计数
        Map<String, Long> result = items.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println(result);
        Map<String, Long> finalMap = new LinkedHashMap<>();
        // 排序
        result.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> finalMap.put(e.getKey(), e.getValue()));

        System.out.println(finalMap);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.HOUR,6);
        System.out.println(instance.get(Calendar.HOUR_OF_DAY));



    }
}
































