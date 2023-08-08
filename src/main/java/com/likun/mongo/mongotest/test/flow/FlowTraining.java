package com.likun.mongo.mongotest.test.flow;

import org.json.JSONArray;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlowTraining {
    public static void main(String[] args) {
        //Consumer 一个输入，无输出
        Consumer consumer = (i)-> System.out.println("有输入没有输出Consumer："+i) ;
        consumer.accept("consumer");
        //Supplier  无输入，有输出
        Supplier supplier = ()-> "Supplier no input has output";
        System.out.println(supplier.get());
        // Function<T,R>  输入T，输出R
        Function<Integer,Integer> function  = (integer -> integer*9)  ;
        System.out.println(function.apply(888));
        // BiFunction<T,U,R> 输入T,U 输出R
        BiFunction<Integer ,Integer ,String >  biFunction = ( i,j)-> (i+j)+"" ;
        System.out.println(biFunction.apply(6,6));
        //Predicate  有输入，输出boolean类型
        Predicate<Integer> predicate  = (i)->i>5 ;
        Predicate<Integer> predicate2  = (i)->i>50 ;
        System.out.println(predicate.or(predicate2).and(predicate).or(predicate2.or(predicate)).test(15));
        //创建流
        String[] arr = {"ss","2s","ssss","sdffds"} ;
        Arrays.stream(arr).forEach(System.out::print);
        Arrays.asList(arr).forEach(System.out::print);
        Stream.of(arr).forEach(System.out::print);
        Stream.iterate(1,(i) -> i+1).limit(10).forEach(System.out::println);
        Stream.generate(() -> new Random().nextInt(10)).limit(10).forEach(System.out::println);

        String[] strArray1 = {"ss","ss","","sdffg","bca-de","fff"};
        String collect = Stream.of(strArray1)
                .filter(i -> !i.isEmpty())//过滤空字符串
                .sorted() //排序
                .limit(4) //只取第一个元素
                .map(i -> i.replace("-", ""))//替换 "-"
                .flatMap(i -> Stream.of(i.split("")))//将字符拆成字符数组
                .sorted() //排序
                .collect(Collectors.joining("|"));//将字符拼接组合到一起
        System.out.println(collect);//最后输出abcde

    }
}
