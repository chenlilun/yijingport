package com.likun.mongo.mongotest.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@Document(collection = "teacher")
public class Teacher {


    @MongoId(value = FieldType.STRING)
    private String id ;
    private  String name ;
//    private List<String> gradeId ;
}
