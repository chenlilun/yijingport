package com.likun.mongo.mongotest.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.Date;

@Data
public class T_Batch implements Serializable {

    /**
     * note :
     * batchNo : HD031501
     * product : 5bffa63c8857b85a437d1f92
     * creator : 5b384b2cd8712064f101e31e
     * multiDyeing : false
     * workshop : 5c877549a3f0a02467a817ef
     * modifier : 5c997cd0d4dd400001014fb1
     * centralValue : 268.0
     * spec : 268dtex/144f
     * tubeWeight : 0.33
     * silkWeight : 15.25
     * cdt : 2019-03-13T00:12:41.817+0000
     * tubeColor : 黄蓝条
     * holeNum : 144
     * _id : 5c884afba3f0a07291abd14c
     * mdt : 2020-02-06T10:15:50.000+0000
     */
    private String note;
    private String batchNo;
    private String product;
    private String creator;
    private boolean multiDyeing;
    private String workshop;
    private String modifier;
    private double centralValue;
    private String spec;
    private double tubeWeight;
    private double silkWeight;
    private Date cdt;
    private String tubeColor;
    private int holeNum;
    @MongoId
    private String _id;
    private Date mdt;


}
