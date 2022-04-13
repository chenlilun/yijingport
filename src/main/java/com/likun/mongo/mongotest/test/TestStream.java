package com.likun.mongo.mongotest.test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestStream {
    public static void main(String[] args) {


        List<Person> personList = new ArrayList<>();

        personList.add(new Person("Tom", 23, 8900, "male", "NewYork"));
        personList.add(new Person("Jack", 52, 7800, "male", "Washington"));
        personList.add(new Person("Lily", 24, 7000, "female", "Washington"));
        personList.add(new Person("Anni", 31, 8200, "female", "NewYork"));
        personList.add(new Person("Owen", 32, 9500, "male", "NewYork"));
        personList.add(new Person("Tony", 53, 8900, "male", "NewYork"));
        personList.add(new Person("Alisa", 23, 7900, "female", "NewYork"));

        List<Integer> integerList = Arrays.asList(6, 7, 8, 5, 1, 9, 11, 3, 45, 8);


        // 查找年龄大于23岁的员工的姓名
        List<String> nameList = personList.stream().filter(x -> x.getAge() > 23).map(Person::getName).collect(Collectors.toList());
        System.out.print("大于23岁员工姓名：" + nameList + "\n");
        // 输出大于6的数
        Stream<Integer> integerStream = integerList.stream();
        integerStream.filter(x -> x > 6).forEach(System.out::println);

        // 查找名字最长的员工的姓名
        Optional<String> nameList2 = personList.stream().map(Person::getName).max(Comparator.comparing(String::length));
        System.out.println("\n名字最长的员工：" + nameList2.get());

        //获取Integer集合中的最大值
        //自然排序
        Optional<Integer> max1 = integerList.stream().max(Integer::compareTo);
        //自定义排序
        Optional<Integer> max2 = integerList.stream().max((o1, o2) -> o1.compareTo(o2));

        System.out.println("\n自然排序的最大值： " + max1.get());
        System.out.println("\n自定义排序的最大值： " + max2.get());

        //获取员工薪水最高的人
        Optional<Person> maxSalary = personList.stream().max(Comparator.comparingInt(Person::getSalary));
        System.out.println("\n薪水最高的员工为： " + maxSalary.get().getName() + ", 薪水为：" + maxSalary.get().getSalary());

        //统计Integer集合中大于8的元素的个数
        long count = integerList.stream().filter(x -> x > 8).count();
        System.out.println("\nInteger集合中大于8的元素的个数为：" + count);
        //统计Person集合中薪水大于8000的员工个数
        long count1 = personList.stream().filter(x -> x.getSalary() > 8000).count();
        System.out.println("\n薪水大于8000的员工的个数为：" + count1);


        String[] strArray = {"abx", "bvcs", "adegew", "fIs"};
        List<Integer> integerList1 = Arrays.asList(1, 3, 5, 7, 9, 11);

        List<String> upperCaseStrArray = Arrays.stream(strArray).map(String::toUpperCase).collect(Collectors.toList());
        List<Integer> addThreeIntegerList1 = integerList1.stream().map(x -> x + 3).collect(Collectors.toList());

        System.out.println("\n转换字符串数组中的小写字母为大写：" + upperCaseStrArray);
        System.out.println("\n整数数组元素加3后：" + addThreeIntegerList1);

        //将员工的薪水都增加1000元
        //不改变原来员工集合的方式
        List<Person> personListNew1 = personList.stream().map(person -> {
            Person personNew = new Person(
                    person.getName(), 0, 0, null, null
            );
            personNew.setSalary(person.getSalary() + 1000);
            return personNew;
        }).collect(Collectors.toList());
        System.out.println("\n一次改动前：" + personList.get(0).getName() + "-->" + personList.get(0).getSalary());
        System.out.println("\n一次改动后：" + personListNew1.get(0).getName() + "-->" + personListNew1.get(0).getSalary());
        //改变原来员工集合的方式
        List<Person> personListNew2 = personList.stream().map(person -> {
            person.setSalary(person.getSalary() + 1000);
            return person;
        }).collect(Collectors.toList());
        System.out.println("\n二次改动前：" + personList.get(0).getName() + "-->" + personList.get(0).getSalary());
        System.out.println("\n二次改动后：" + personListNew2.get(0).getName() + "-->" + personListNew2.get(0).getSalary());


        //将两个字符数组合并为一个新的字符数组
        List<String> list = Arrays.asList("m1, k1, l1, a1", "1b, 2b, 3b, 4");
        List<String> listCombined = list.stream().flatMap(s -> {
            //将每个元素转化为一个stream
            String[] split = s.trim().split(",");
            Stream<String> s2 = Arrays.stream(split);
            return s2;
        }).collect(Collectors.toList());
        System.out.println("\n处理前的集合：" + list.size());
        System.out.println("\n处理后的集合：" + listCombined.size());


        //求和方式1
        Optional<Integer> sum = integerList.stream().reduce((x, y) -> x + y);
        //求和方式2
        Optional<Integer> sum2 = integerList.stream().reduce(Integer::sum);
        //求和方式3
        Integer sum3 = integerList.stream().reduce(0, Integer::sum);

        System.out.println("\n求和方式: " + sum.get() + ", " + sum2.get() + ", " + sum3);


        //求乘积
        Optional<Integer> product = integerList.stream().reduce((x, y) -> x * y);
        //求最大值方式一
        Optional<Integer> max = integerList.stream().reduce((x, y) -> x > y ? x : y);
        //求最大值方式二
        Integer maxInteger = integerList.stream().reduce(1, Integer::max);
        System.out.println("\n求乘积：" + product.get());
        System.out.println("\n求和：" + max.get() + ", " + maxInteger);


        //求所有员工的工资之和与最高工资
        //方式一
        Optional<Integer> sumSalary = personList.stream().map(Person::getSalary).reduce(Integer::sum);
        //方式二
        Integer sumSalary2 = personList.stream().reduce(0, (sum0, p) -> sum0 += p.getSalary(), (sum1, sum4) -> sum1 + sum4);
        //方式三
        Integer sumSalary3 = personList.stream().reduce(0, (sum0, p) -> sum0 += p.getSalary(), Integer::sum);

        //求最高工资方式一
        Integer maxSalary1 = personList.stream().reduce(0, (max_0, p) -> max_0 > p.getSalary() ? max_0 : p.getSalary(), Integer::max);
        //求最高工资方式二
        Integer maxSalary2 = personList.stream().reduce(0, (max_1, p) -> max_1 > p.getSalary() ? max_1 : p.getSalary(), (max_2, max3) -> max_2 > max3 ? max_2 : max3);

        System.out.println("\n所有员工工资之和：" + sumSalary.get() + ", " + sumSalary2 + ", " + sumSalary3);
        System.out.println("\n最高工资：" + maxSalary1 + ", " + maxSalary2);


        //求整数集合中的偶数
        List<Integer> evenNumber = integerList.stream().filter(x -> x % 2 == 0).collect(Collectors.toList());

        Set<Integer> evenNumberSet = integerList.stream().filter(x -> x % 2 == 0).collect(Collectors.toSet());

        //薪水大于8000的员工名字和薪水，name->salary
        Map<?, Integer> nameSalaryMap = personList.stream().filter(p -> p.getSalary() > 8000).collect(Collectors.toMap(Person::getName, p -> p.getSalary()));
        System.out.println("\n偶数列表：" + evenNumber);
        System.out.println("\n偶数集合：" + evenNumberSet);
        System.out.println("\n薪水大于8000的员工名字和薪水：" + nameSalaryMap);

        /**
         * Collectors提供了一系列用于数据统计的静态方法：
         *
         * 计数：count
         * 平均值：averagingInt、averagingLong、averagingDouble
         * 最值：maxBy、minBy
         * 求和：summingInt、summingLong、summingDouble
         * 统计以上所有：summarizingInt、summarizingLong、summarizingDouble
         *
         * 案例：统计员工人数、平均工资、工资总额、最高工资。
         */
        //员工总人数
        long personCount = personList.stream().count();
        Long personCount2 = personList.stream().collect(Collectors.counting());
        System.out.println("\n员工总数：" + personCount + ", " + personCount2);

        //员工平均工资
        Double averageSalary = personList.stream().collect(Collectors.averagingDouble(Person::getSalary));
        //员工最高工资
        Optional<Integer> maxSalary_1 = personList.stream().map(Person::getSalary).collect(Collectors.maxBy(Integer::compare));
        //工资总额
        Integer sumSalary_1 = personList.stream().collect(Collectors.summingInt(Person::getSalary));
        //一次性统计所有信息
        DoubleSummaryStatistics doubleSummaryStatistics = personList.stream().collect(Collectors.summarizingDouble(Person::getSalary));

        System.out.println("\n平均工资：￥" + averageSalary);
        System.out.println("\n最高工资：￥" + maxSalary_1.get());
        System.out.println("\n工资总额：￥" + sumSalary_1);
        System.out.println("\n一次性统计员工信息：" + doubleSummaryStatistics);


        /**
         * 分组(partitioningBy/groupingBy)
         * 分区：将stream按条件分为两个Map，比如员工按薪资是否高于8000分为两部分。
         * 分组：将集合分为多个Map，比如员工按性别分组。有单级分组和多级分组。
         *
         * 案例：将员工按薪资是否高于9000分为两部分；将员工按性别和地区分组
         */
        //将员工薪水大于9000的分组
        Map<Boolean, List<Person>> partitionBySalary = personList.stream().collect(Collectors.partitioningBy(x -> x.getSalary() > 9000));
        //将员工按照性别分组
        Map<String, List<Person>> partitionBySex = personList.stream().collect(Collectors.groupingBy(Person::getSex));
        // 将员工先按性别分组，再按地区分组
        Map<String, Map<String, List<Person>>> partitionBySexAndArea = personList.stream().collect(Collectors.groupingBy(Person::getSex, Collectors.groupingBy(Person::getArea)));

        System.out.println("\n薪水大于9000：" + partitionBySalary.get(true).stream().collect(Collectors.toMap(Person::getName, p -> p.getSalary())) + "\n薪水小于9000：" + partitionBySalary.get(false).stream().collect(Collectors.toMap(Person::getName, p -> p.getSalary())));
        System.out.println("\n按性别分组：\n男性：" + partitionBySex.get("male").stream().collect(Collectors.toMap(Person::getName, p -> p.getSex())) + "\n女性：" + partitionBySex.get("female").stream().collect(Collectors.toMap(Person::getName, p -> p.getSex())));
        System.out.println("\n先按照性别分组，再按照地区分组：" +
                "\n男性：" + partitionBySexAndArea.get("male")
                +
                "\n女性：" + partitionBySexAndArea.get("female"));
        /**
         * joining可以将stream中的元素用特定的连接符（没有的话，则直接连接）连接成一个字符串。
         */
        //所有员工的姓名
        String joinName = personList.stream().map(p -> p.getName()).collect(Collectors.joining("-"));
        System.out.println("\n员工姓名拼接：" + joinName);

        /**
         * Collectors类提供的reducing方法，相比于stream本身的reduce方法，增加了对自定义归约的支持。
         */
        //每个员工扣除税款后工资应纳税金额求和
        Integer sumSalaryTaxed = personList.stream().collect(Collectors.reducing(0, Person::getSalary, (sum_5, i) -> sum_5 + i - 5000));
        Optional<Integer> sumSalaryBeforeTaxed2 = personList.stream().map(Person::getSalary).reduce(Integer::sum);
        System.out.println("\n扣除5000起征额后所有员工的工资总额：" + sumSalaryTaxed);
        System.out.println("\n员工工资总额：" + sumSalaryBeforeTaxed2.get());


        /**
         * sorted，中间操作。有两种排序：
         *
         * sorted()：自然排序，流中元素需实现Comparable接口
         * sorted(Comparator com)：Comparator排序器自定义排序
         */
        //案例：将员工按工资由高到低（工资一样则按年龄由大到小）排序

        //按工资升序（自然排序）,输出名字序列
        List<String> sortedPersonList = personList.stream()
                .sorted(Comparator.comparing(Person::getSalary))
                .map(Person::getName).collect(Collectors.toList());
        //按工资倒序输出，名字序列
        List<String> reverseSortedList = personList.stream()
                .sorted(Comparator.comparing(Person::getSalary).reversed())
                .map(Person::getName).collect(Collectors.toList());
        //先按工资排序，再按照年龄升序输出
        List<String> sortedBySalaryAndAge = personList.stream()
                .sorted(Comparator.comparing(Person::getSalary).thenComparing(Person::getAge))
                .map(Person::getName)
                .collect(Collectors.toList());
        //先按工资排序，再按年龄自定义排序倒序输出
        List<String> sortedBySalaryAndReverseAge = personList.stream()
                .sorted((o1, o2) -> o1.getSalary() == o2.getSalary() ? o2.getAge() - o1.getAge() : o2.getSalary() - o1.getSalary())
                .map(Person::getName).collect(Collectors.toList());

        System.out.println("\n工资升序：" + sortedPersonList);
        System.out.println("\n工资逆序：" + reverseSortedList);
        System.out.println("\n工资升序，年龄升序：" + sortedBySalaryAndAge);
        System.out.println("\n工资降序，年龄降序：" + sortedBySalaryAndReverseAge);


        /**
         * 流也可以进行合并、去重、限制、跳过等操作。
         */

        String[] arr1 = {"a", "b", "c", "d"};
        String[] arr2 = {"d", "e", "f", "g"};
        Stream<String> stringStream1 = Stream.of(arr1);
        Stream<String> stringStream2 = Stream.of(arr2);
        //concat合并，distinct 去重
        List<String> newArrayList = Stream.concat(stringStream1, stringStream2)
                .distinct().collect(Collectors.toList());
        //limit限制从流中获取得前几个数据
        List<Integer> collect = Stream.iterate(1, x -> x + 2).limit(10)
                .collect(Collectors.toList());
        //skip跳过前几个数据
        List<Integer> collect2 = Stream.iterate(1, x -> x + 2).skip(2).limit(5)
                .collect(Collectors.toList());
        System.out.println("\n流合并：" + newArrayList);
        System.out.println("\nlimit: " + collect);
        System.out.println("\nskip: " + collect2);
    }


}
