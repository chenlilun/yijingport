package com.likun.mongo.mongotest.test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class TTTT {
    public static void main(String[] args) {
        System.out.println("D2345".substring(0, 1));
        String s = "b,3,3,null".split(",")[3];
        System.out.println(s==null);
        System.out.println("null".equals(s));
        Product prod1 = new Product(1L, 1, new BigDecimal("15.5"), "面包", "零食");

        Product prod2 = new Product(2L, 3, new BigDecimal("20"), "饼干", "零食");
        Product prod3 = new Product(3L, 4, new BigDecimal("30"), "月饼", "零食");
        Product prod4 = new Product(4L, 5, new BigDecimal("10"), "青岛啤酒", "啤酒");
        Product prod5 = new Product(5L, 6, new BigDecimal("15"), "百威啤酒", "啤酒");
        Product ooo = new Product(1L, 2, new BigDecimal("15.5"), "面包", "零食");
        List<Product> prodList = Arrays.asList(prod1, prod2, prod3, prod4, prod5);
        List<Product> ssssssssssssbbbbbbbbbbbbb = prodList.stream().peek(a -> {
            if (a.getNum() == 1) {
                a.setName("ssssssssssssbbbbbbbbbbbbb");
            }
        }).collect(Collectors.toList());
        System.out.println("*****"+ssssssssssssbbbbbbbbbbbbb);



        Set<Product> sets = new HashSet<>() ;
        sets.add(prod1) ;
        sets.add(ooo) ;

      List<Product>  prodList2 = new ArrayList<>() ;
      prodList.stream().forEach(a->{
          boolean match = prodList2.stream().anyMatch(two -> two.getCategory().equals(a.getCategory()));
          System.out.println(match);
      });

        prodList.stream().forEach(a->{
            a.setName("sb");
        });
        prodList =   prodList.subList(0,2)  ;
        List<Product> aaa = Arrays.asList(ooo);
        sets.addAll( aaa) ;
        prodList.stream().forEach(a->{
            a.setName("sb");
        });
        Map<String, List<Product>> map = prodList.stream().collect(Collectors.groupingBy(Product::getCategory));
//        System.out.println(map);
//        map.entrySet().stream().map()
        //几个属性拼接分组
/*      prodList.stream().collect(Collectors.groupingBy(item -> item.getCategory() + "_" + item.getName())).entrySet().forEach(a->{
          System.out.println(a);
      });*/
      //根据不同条件分组
        Map<String, List<Product>> collect = prodList.stream().collect(Collectors.groupingBy(item -> {
            if (item.getNum() < 3) {
                return "a";
            } else {
                return "b";
            }
        }));
        //多级分组
        Map<String, Map<String, Double>> collect1 = prodList.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.groupingBy(item -> {
            if (item.getNum() < 3) {
                return "3333";
            } else {
                return "55555555";
            }
        }, Collectors.summingDouble(a -> {
            return Double.valueOf(a.getPrice() + "");
        }))));
        //求分组总数
        Map<String, Product> prodMap = prodList.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(Product::getNum)), Optional::get)));

         prodList.stream().collect(Collectors.groupingBy(Product::getCategory,Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(Product::getNum)) ,Optional::get)));


        Map<String, Set<String>> collect2 = prodList.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.mapping(Product::getName, Collectors.toSet())));
        /**
         * 核心代码
         *
         * *******/
        final Integer[] i = {1};
        List<Product>  results  =  new ArrayList<>()  ;
        map.entrySet().stream().forEach(a -> {
            a.getValue().forEach(b->{
                b.setOrderBy(i[0]);
                results.add(b) ;
            });
            i[0]++  ;
        });


    }
}
