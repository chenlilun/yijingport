package com.likun.mongo.mongotest.test;

import lombok.Data;

@Data
public class Person {
    private String name;
    private int age;



    private int salary;
    private String sex;
    private String area;
    public Person(String name, int age, int salary, String sex, String area) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.sex = sex;
        this.area = area;
    }

}
