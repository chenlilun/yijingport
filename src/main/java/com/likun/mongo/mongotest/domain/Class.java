package com.likun.mongo.mongotest.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("classDoc")
@Data
public class Class {
    @Id
    private String id;
    //班级名称
    private String className;
    //开班时间
    private Date openDate;
    //引用学生信息
    @DBRef
    private List<Student> students;
}
