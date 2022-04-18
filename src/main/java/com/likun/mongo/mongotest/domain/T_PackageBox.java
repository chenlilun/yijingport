package com.likun.mongo.mongotest.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.Date;
@Document(collection = "T_PackageBox")
@Data
public class T_PackageBox implements Serializable {

    /**
     * code : 060220201202HD060705300144
     * modifier : if_warehouse
     * saleType : DOMESTIC
     * type : MANUAL
     * packageType : 普通箱包
     * silkCount : 93
     * pipeType : 0.0
     * tare : 0.0
     * budatClass : 5bfd4a0c67e7ad000188a0d9
     * creator : 5c8ed6b5516d1443cfef550f
     * printDate : 2020-12-01T23:35:49.000+0000
     * batch : 5c884afba3f0a07291abd14f
     * palletType : 一类回收
     * command : {"temporaryBox":{"id":"5d147bf34ed4b6697103dbf1"},"count":93}
     * cdt : 2020-12-01T23:35:49.000+0000
     * grossWeight : 744.5
     * netWeight : 702.68
     * printClass : 5bfd4a0c67e7ad000188a0d9
     * sapT001l : 9234
     * foamType : FDY方衬板1120*1120*19（小孔）
     * grade : 1770980760270929960
     * _id : 5fc6d35595b4b200013b73d7
     * mdt : 2020-12-02T00:31:25.000+0000
     * foamNum : 11
     * budat : 2020-12-01T16:00:00.000+0000
     */
    private String code;
    private String modifier;
    private String saleType;
    private String type;
    private String packageType;
    private int silkCount;
    private double pipeType;
    private double tare;
    private String budatClass;
    private String creator;
    private Date printDate;
    private String batch;
    private String palletType;
    private String command;
    private Date cdt;
    private double grossWeight;
    private double netWeight;
    private String printClass;
    private String sapT001l;
    private String foamType;
    private String grade;
    @MongoId
    private String _id;
    private Date mdt;
    private Integer foamNum;
    private Integer printCount;
    private Boolean inWarehouse;
    private Date budat;
    private String postFlag;
    private String[] silkCarRecords ;



}
