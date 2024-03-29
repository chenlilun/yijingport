package com.likun.mongo.mongotest.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.List;

@Document(collection = "student")
@Data
public class Student implements Serializable {
    @MongoId
    private String id;
    //学生姓名
    private String stuName;
    private String sex;
    //引用班级
    private String gradeId ;
    private String aaaBbb ;

    private List<Item> items ;

}
