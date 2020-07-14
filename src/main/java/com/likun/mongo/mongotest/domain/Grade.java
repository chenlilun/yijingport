package com.likun.mongo.mongotest.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "grade")
@Data
public class Grade {
    @Id
    private  String id;
    private  String gradeName;
}
