package com.likun.mongo.mongotest.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ParamsBean {
    private  String gteDate ;
    private  String letDate ;
    private  String workshop ;
    private  String line ;
}
