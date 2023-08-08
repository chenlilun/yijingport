package com.likun.mongo.mongotest.yunbiaotest;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class Token implements Serializable {

    /**
     * departmentName :
     * redirect : /10002
     * serverVersion : 3.3.25.105
     * orgName :
     * departmentCode :
     * orgFullName :
     * target : 72222105-0ee3-4bcd-8af9-42604b231579
     * token : 9386c6a5-6b91-4321-9953-9cbe281152f0
     * third_auth_type :
     * path : /10002
     * logout : /10002/logout
     * domain : wms.hengyi.com:8400
     * departmentFullName :
     * user : 陈立坤
     * account : 18011514
     */
    @SerializedName("departmentName")
    public String departmentName;
    @SerializedName("redirect")
    public String redirect;
    @SerializedName("serverVersion")
    public String serverVersion;
    @SerializedName("orgName")
    public String orgName;
    @SerializedName("departmentCode")
    public String departmentCode;
    @SerializedName("orgFullName")
    public String orgFullName;
    @SerializedName("target")
    public String target;
    @SerializedName("token")
    public String token;
    @SerializedName("third_auth_type")
    public String third_auth_type;
    @SerializedName("path")
    public String path;
    @SerializedName("logout")
    public String logout;
    @SerializedName("domain")
    public String domain;
    @SerializedName("departmentFullName")
    public String departmentFullName;
    @SerializedName("user")
    public String user;
    @SerializedName("account")
    public String account;
    @SerializedName("Exception")
    public ExceptionYB Exception;
    @Data
    public static class  ExceptionYB{
        @SerializedName("code")
        public int code;
        @SerializedName("detail")
        public String detail;
        @SerializedName("msg")
        public String msg;
    }
}
