package com.likun.mongo.mongotest.test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Text {
    public static void main(String[] args) {
        System.out.println(        Long.parseLong("01:52:54")
        );
        Product prod1 = new Product(1L, 1, new BigDecimal("15.5"), "A", "零食");
        Product prod2 = new Product(2L, 1, new BigDecimal("20"), "AA", "零食");


        Product prod3 = new Product(3L, 4, new BigDecimal("30"), "月饼", "零食");
        Product prod4 = new Product(4L, 5, new BigDecimal("10"), "青岛啤酒", "啤酒");
        Product prod5 = new Product(5L, 6, new BigDecimal("15"), "百威啤酒", "啤酒");
        List<Product> prodList = new ArrayList<>();
        prodList.add(prod1);
        prodList.add(prod2);
        Optional<Product> f = prodList.stream().filter(item -> item.getName().equals("C")).findFirst();
        List<Product> collect;
        if (f.isPresent()) {  // 有该条数据num + 1   对应的AA -1
            collect = prodList.stream().map(item -> {
                if (f.get().getName() == item.getName()) {  // 有该条数据num + 1  降等的
                    item.setNum(item.getNum() + 1);
                }
                if (item.getName() == "AA") {  // 有该条数据num + 1
                    item.setNum(item.getNum() - 1);
                }
                return item;
            }).collect(Collectors.toList());
            collect.removeIf(a -> a.getName().equals("AA") && a.getNum() == 0);
            System.out.println(collect);
        } else {   // 没有的话 整条 数据 +1   然后 对应的AA -1
            collect = prodList;
            Optional<Product> AA = prodList.stream().filter(item -> item.getName().equals("AA")).findFirst();
            if (AA.isPresent()) {
                Product product = AA.get();
                Product newPro = new Product(2L, 1, new BigDecimal("20"), "C", "零食");
                collect.add(newPro);
            }
            collect.removeIf(a -> a.getName().equals("AA") && a.getNum() == 1);
            System.out.println("没有C===" + collect);
        }
//        collect.removeIf(a->a.getName().equals("AA")&&a.getNum()==1);
        System.out.println("最终===" + collect);
        System.out.println("aaa" + prodList);
    /*    prodList.stream().filter(a->a.getNum()==1).forEach(item->{  //匹到了
            if(item.getNum()==1){
                Product product = new Product() ;
                product.setNum(2);
                product.setCategory(item.getCategory());
                product.setName("新增的数据");
                prodList.add(product) ;
            }
        });*/
        System.out.println(prodList);


//        prodList.add(prod2);
//        prodList.add(prod3);
//        prodList.add(prod4);
//        prodList.add(prod5);
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.stream().forEach(a -> {
            prodList.removeIf(b -> b.getNum() == a);
        });
        System.out.println(prodList + "***");


        List<Shopper> shoppers = new ArrayList<>();

        Shopper shopper1 = new Shopper();
        List<Product> products1 = new ArrayList<>();
        shopper1.setName("zhangsan");
        products1.add(prod1);
        products1.add(prod2);
        shopper1.setProductList(products1);
        Shopper shopper2 = new Shopper();
        List<Product> products2 = new ArrayList<>();
        shopper2.setName("lisi");
        products2.add(prod3);
        products2.add(prod4);
        products2.add(prod5);
        shopper2.setProductList(products2);

        shoppers.add(shopper1);
        shoppers.add(shopper2);
        long aa = shoppers.stream().flatMap(shop -> shop.getProductList().stream().filter(p -> p.getCategory().equals("零食"))).count();
        System.out.println(aa);
        String workshop2 = "eujhik";
        System.out.println(workshop2.substring(0, 1));
        System.out.println(workshop2.substring(0, 1));
    }
}
