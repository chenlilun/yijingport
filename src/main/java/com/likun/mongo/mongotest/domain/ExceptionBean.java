package com.likun.mongo.mongotest.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document(collection = "T_ExceptionRecord")
@Data
public class ExceptionBean {

    /**
     * exception : 5bffa63c8857b85a437d1f72
     * silk : 5d2834b3726a950001df53f3
     * handler : 5b384b2cd8712064f101e31e
     * creator : 5c8ed6b5516d1443cfef5558
     * modifier : 5c8ed6b5516d1443cfef5558
     * lineMachine : 5c883c78a3f0a03522f18e1b
     * cdt :
     * doffingNum : B5
     * handled : true
     * handleDateTime :
     * _id : 5d2844dcfc15c2000149d115
     * mdt :
     * spindle : 3
     */
    @MongoId(value = FieldType.STRING)
    private String id;
    private String exception;
    private String creator;


}
