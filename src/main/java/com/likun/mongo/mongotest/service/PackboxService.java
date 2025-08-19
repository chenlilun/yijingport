package com.likun.mongo.mongotest.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.likun.mongo.mongotest.domain.*;
import com.likun.mongo.mongotest.interf.IGlobalCache;
import com.likun.mongo.mongotest.okhttp.OkHttpUtils;
import com.likun.mongo.mongotest.utils.response.CommonCode;
import com.likun.mongo.mongotest.utils.response.QueryResponseResult;
import com.likun.mongo.mongotest.utils.response.QueryResult;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PackboxService {
    @Autowired
    private MongoTemplate template;
    @Autowired
    private OkHttpUtils okHttpUtils;

    public void ttt() {
        Student s = new Student();
        s.setId("aa");
        s.setAaaBbb("aa");
        s.setGradeId("aa");
        s.setSex("aa");
        s.setStuName("aa");
        DeleteResult remove = template.remove(s);
//        System.out.println("ssss" + remove.getDeletedCount());
//        System.out.println("sssdddds" + remove.wasAcknowledged());
    }

    public void findAllBoxAndPull() {
        Calendar a = Calendar.getInstance();
        Calendar begin = Calendar.getInstance();
        begin.set(a.get(Calendar.YEAR), a.get(Calendar.MONTH), a.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        begin.add(Calendar.DAY_OF_MONTH, -4);

        Calendar end = Calendar.getInstance();
        end.set(a.get(Calendar.YEAR), a.get(Calendar.MONTH), a.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        end.add(Calendar.DAY_OF_MONTH, 1);
        end.add(Calendar.HOUR_OF_DAY, 8);


        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat spl = new SimpleDateFormat(strDateFormat);
//        System.out.println("begin==================" + spl.format(begin.getTime()));
//        System.out.println("end==================" + spl.format(end.getTime()));
        Criteria c2 = Criteria.where("printDate").gte(begin.getTime()).lte(end.getTime()).and("type").ne("AUTO").and("postFlag").exists(false);
//        Criteria c2 = Criteria.where("code").is("010120220528GF011025101304");
//        template.findAll(Query.query(c2),"T_PackageBox") ;
        List<T_PackageBox> t_packageBox = template.find(Query.query(c2), T_PackageBox.class, "T_PackageBox");
        if (!ObjectUtils.isEmpty(t_packageBox) && t_packageBox.size() > 0) {
            t_packageBox.stream().forEach(b -> {
                //调用接口
                PostYunbiaoBean bean = new PostYunbiaoBean();
                bean.setFactoryCode("3100");
                bean.setInboundFlag(false);
                T_Batch batch = template.findById(b.getBatch(), T_Batch.class, "T_Batch");
                if (!ObjectUtils.isEmpty(batch)) {
                    bean.setColor(batch.getTubeColor());
                }
                if (!ObjectUtils.isEmpty(b.getCdt())) {
                    //设置入库时间
                    bean.setInboundDate(getAddEightHour(b.getCdt(), spl));
                    bean.setProduceTime(getAddEightHour(b.getCdt(), spl));
                }
                bean.setPallet(getPallet(b.getPalletType()));
                bean.setPackageType(getPackageType(b));
                bean.setProductName(getProDuctName(batch.getProduct()));
                bean.setSpec(batch.getSpec());
                bean.setFoamCount(b.getFoamNum());
                bean.setSalesType(getSalesType(b.getSaleType()));
                bean.setBatchNo(batch.getBatchNo());
                bean.setWorkshop(getWorkShop(b.getCode()));
                bean.setLine(getLine(b.getCode()));
                bean.setFactoryName("恒逸高新材料A工厂");
                bean.setPalletType(getPalletype(b.getPalletType()));
                bean.setUId(b.getCode());
                bean.setLgort(b.getSapT001l());
                bean.setFoamType(b.getFoamType());
                bean.setGrade(getGrade(b.getGrade()));
                String value = b.getCode();
                int length = value.length();

                if (length >= 3) {
                    bean.setSequence(value.substring(length - 3, length));
                }
                bean.setClassesInfo(getPrintClass(b.getPrintClass()));  // 甲乙丙丁
                bean.setTeamInfo(getTeam(b.getPrintClass())); // ABC
                List<PostYunbiaoBean.ProductInfoEntity> list = new ArrayList<>();
                if (getPackageType(b).equals("小纸箱")) {
                    setMututiArray(b, batch, value, length, list);
                } else {
                    setSingArray(b, batch, value, length, list);
                }


                bean.setProductInfo(list);
                Gson gson = new Gson();
                String postData = gson.toJson(bean);
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
//        String printData = okHttpUtils.httpPostJson("http://10.77.0.24:8090/api/doff/getSilkOnLineForWorkshop", headers, postData);
//                String printData = okHttpUtils.httpPostJson("http://10.77.0.29:8080/webapi/process/LOA_WMS/ProducePalletAsync", headers, postData);
                String printData = okHttpUtils.httpPostJson("http://10.2.0.221:8081/webapi/process/LOA/ProducePallet", headers, postData);
//                String printData = okHttpUtils.httpPostJson("http://192.168.0.147:8081/webapi/process/LOA/ProducePallet", headers, postData);
//                System.out.println("AAAAAAAA" + printData);
                if (!ObjectUtils.isEmpty(printData) && printData.contains("\"status\":200")) {
                    //                printData.if
                    b.setPostFlag("post");
                    template.save(b, "T_PackageBox");
                }


            });
        }
    }

    private void setSingArray(T_PackageBox b, T_Batch batch, String value, int length, List<PostYunbiaoBean.ProductInfoEntity> list) {
        PostYunbiaoBean.ProductInfoEntity productInfoEntity = new PostYunbiaoBean.ProductInfoEntity();
        productInfoEntity.setClasses(getPrintClass(b.getPrintClass()));
        if (!ObjectUtils.isEmpty(b.getGrossWeight())) {
            productInfoEntity.setGrossWeight(b.getGrossWeight());
        }
        if (!ObjectUtils.isEmpty(b.getNetWeight())) {
            productInfoEntity.setNetWeight(b.getNetWeight());
        }
        productInfoEntity.setTeam(getTeam(b.getPrintClass()));
        productInfoEntity.setClasses(getPrintClass(b.getPrintClass()));
        productInfoEntity.setNewProductCode(b.getCode());
        productInfoEntity.setOperatorId("MANUAL");
        productInfoEntity.setPrintFlag(false);
        if (length >= 3) {
            productInfoEntity.setSequence(value.substring(length - 3, length));
        }
        productInfoEntity.setSilkCount(b.getSilkCount());
        productInfoEntity.setSilkWeight(batch.getSilkWeight());
        productInfoEntity.setProductDate(getProDuctDate(b.getCdt()));
        productInfoEntity.setPrintTime("1970-1-1");
        productInfoEntity.setPrintCount(0);
        list.add(productInfoEntity);
    }

    private void setMututiArray(T_PackageBox b, T_Batch batch, String value, int length, List<PostYunbiaoBean.ProductInfoEntity> list) {
        if (b.getSilkCount() > 0) {
            int i = b.getSilkCount() / 4;
            for (int j = 0; j < i; j++) {
                PostYunbiaoBean.ProductInfoEntity productInfoEntity = new PostYunbiaoBean.ProductInfoEntity();
                productInfoEntity.setNewProductCode(b.getCode() + String.format("%02d", j + 1));
                productInfoEntity.setClasses(getPrintClass(b.getPrintClass()));
                if (!ObjectUtils.isEmpty(b.getGrossWeight())) {
                    productInfoEntity.setGrossWeight(b.getGrossWeight() / b.getSilkCount() * 4);
                }
                if (!ObjectUtils.isEmpty(b.getNetWeight())) {
                    productInfoEntity.setNetWeight(b.getNetWeight() / b.getSilkCount() * 4);
                }
                productInfoEntity.setTeam(getTeam(b.getPrintClass()));
                productInfoEntity.setClasses(getPrintClass(b.getPrintClass()));
                productInfoEntity.setOperatorId("MANUAL");
                productInfoEntity.setPrintFlag(false);
                if (length >= 3) {
                    productInfoEntity.setSequence(value.substring(length - 3, length));
                }
                productInfoEntity.setSilkCount(4);
                productInfoEntity.setSilkWeight(batch.getSilkWeight());
                productInfoEntity.setProductDate(getProDuctDate(b.getCdt()));
                productInfoEntity.setPrintTime("1970-1-1");
                productInfoEntity.setPrintCount(0);
                list.add(productInfoEntity);
            }

        }


    }

    private String getPalletype(String palletType) {
        if (!ObjectUtils.isEmpty(palletType)) {
            if ("塑料托盘".equals(palletType)) {
                return "久鼎塑托";
            } else if ("熏蒸木架".equals(palletType)) {
                return "熏蒸木托";
            } else if ("普通木架".equals(palletType)) {
                return "普通木托";
            }
        }
        return "";
    }

    private String getPackageType(T_PackageBox b) {
        if (!ObjectUtils.isEmpty(b.getType()) && b.getType().equals("SMALL")) {
            return "小纸箱";
        }
        if ("BIG_SILK_CAR".equals(b.getType())) {
            return "丝车";
        }
        if ("小箱包装".equals(b.getPackageType())) {
            return "小纸箱";
        }
        if (!ObjectUtils.isEmpty(b.getPackageType()) && b.getPackageType().contains("普通")) {
            return "普通箱包";
        }
        if (!ObjectUtils.isEmpty(b.getPackageType()) && b.getPackageType().equals("白皮包装")) {
            return "普通箱包-无抬头";
        }
        return "普通箱包";
    }

    private String getAddEightHour(Date cdt, SimpleDateFormat spl) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cdt);
        calendar.add(Calendar.HOUR_OF_DAY, 8);
        String format = spl.format(calendar.getTime());
//        System.out.println("创建时间=======" + format);
        return format;
    }

    private String getProDuctDate(Date cdt) {
        String strDateFormat = "yyyy-MM-dd";
        SimpleDateFormat spl = new SimpleDateFormat(strDateFormat);
        Calendar cal = Calendar.getInstance();
        cal.setTime(cdt);
        cal.add(Calendar.HOUR_OF_DAY, 8);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour < 8) {
            cal.add(Calendar.HOUR_OF_DAY, -8);
        }

        return spl.format(cal.getTime());
    }

    private String getPrintClass(String classId) {
        if (ObjectUtils.isEmpty(classId)) {
            return "";
        }
        switch (classId) {
            case "5bfd4a0c67e7ad000188a0d9":
                return "甲";
            case "5bfd4a0c67e7ad000188a0db":
                return "乙";
            case "5bfd4a2e67e7ad000188a0e9":
                return "丙";
            case "5db9654bcddd7e0001a87d48":
                return "丁";
            default:
                return "";
        }

    }

    private String getTeam(String classId) {
        if (ObjectUtils.isEmpty(classId)) {
            return "";
        }
        switch (classId) {
            case "5bfd4a0c67e7ad000188a0d9":
                return "A";
            case "5bfd4a0c67e7ad000188a0db":
                return "B";
            case "5bfd4a2e67e7ad000188a0e9":
                return "C";
            case "5db9654bcddd7e0001a87d48":
                return "D";
            default:
                return "";
        }

    }

    private String getGrade(String grade) {
        if (ObjectUtils.isEmpty(grade)) {
            return "";
        }
        switch (grade) {
            case "1770980760270929960":
                return "B";
            case "1770980569354600486":
                return "AA";
            case "1784018919720222752":
                return "废丝";
            case "1788876173229424672":
                return "AAA";
            case "1770980703740100645":
                return "A";
            case "1770980834862432293":
                return "C";
            case "5d35d5fe87b4a50001736dca":
                return "不定重";
            case "5d9015d3cc3de50001497342":
                return "不定重优等";
            case "60d968ad4fcf4b000156a56c":
                return "AA（试）";
            default:
                return "";
        }

    }

    private String getWorkShop(String workshop) {
        if (ObjectUtils.isEmpty(workshop)) {
            return "";
        }
        switch (workshop.substring(13, 14)) {
            case "C":
                return "纺丝C车间";
            case "D":
                return "纺丝D车间";
            case "A":
                return "纺丝A车间";
            case "B":
                return "纺丝B车间";
            case "F":
                return "纺丝F车间";
            default:
                return "";
        }

    }


    private String getLine(String code) {
        if (ObjectUtils.isEmpty(code)) {
            return "";
        }
        switch (code.substring(13, 14)) {
            case "C":
                return "CD";
            case "D":
                return "DD";
            case "A":
                return "AD";
            case "B":
                return "BD";
            case "F":
                return "FD";
            default:
                return "";
        }

    }

    private String getSalesType(String saleType) {
        if (ObjectUtils.isEmpty(saleType)) {
            return "内销";
        } else {
            if (saleType.equals("FOREIGN")) {
                return "外贸";
            } else {
                return "内销";
            }
        }

    }

    private String getProDuctName(String product) {

        return product == null ? "" : product.equals("5bffa63c8857b85a437d1f92") ? "POY" : "FDY";
    }

    private String getPallet(String palletType) {
        if (ObjectUtils.isEmpty(palletType)) {
            return "K";
        } else if (palletType.contains("塑")) {
            return "S";
        } else {
            return "M";
        }

    }

    //    public static void main(String[] args) {
//        findAllBoxAndPull() ;
//    }
    public void test() {
        Student stu = template.findById("bdd28109-1396-49f5-ae84-dc7254e1faeb", Student.class);
        stu.setAaaBbb("aaa");
        template.save(stu);
    }
    /**
     * 格式化 JSON 字符串中的日期字段
     * @param jsonStr 原始 JSON 字符串
     * @param dateField 需要格式化的日期字段名
     * @return 格式化后的 JSON 字符串
     */
    public static String formatDateField(String jsonStr, String dateField) {
        try {
            // 解析 JSON 字符串
            JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();

            if (jsonObject.has(dateField)) {
                String originalDate = jsonObject.get(dateField).getAsString();

                // 定义日期格式转换器
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm:ss a", Locale.ENGLISH);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                // 转换日期格式
                LocalDateTime dateTime = LocalDateTime.parse(originalDate, inputFormatter);

                // 增加8小时
                dateTime = dateTime.plusHours(8);

                // 格式化日期
                String formattedDate = dateTime.format(outputFormatter);

                // 更新 JSON 中的字段值
                jsonObject.addProperty(dateField, formattedDate);
            }

            // 返回格式化后的 JSON 字符串
            return jsonObject.toString();

        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常时返回原始字符串
            return jsonStr;
        }
    }
    public QueryResponseResult updatePackageBox(T_PackageBox post) {
        String newMesPostData = new Gson().toJson(post);
        System.out.println("原始数据:" + newMesPostData);
        String formattedJson =formatDateField(newMesPostData, "budat");

        try {
            Criteria c = Criteria.where("code").is(post.getCode());
//        template.findAll(Query.query(c2),"T_PackageBox") ;
            List<T_PackageBox> t_packageBoxs = template.find(Query.query(c), T_PackageBox.class, "T_PackageBox");
            T_PackageBox t;
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            if (t_packageBoxs.size() > 0) {
                t = t_packageBoxs.get(0);
                if (ObjectUtils.isEmpty(post.getType())) {
                    t.setType(post.getType());
                }
                //小箱包情况
                if ("小纸箱".equals(post.getPackageType())) {
                    t.setPackageType("小箱包装");
                    t.setType("SMALL");
                    if (!ObjectUtils.isEmpty(post.getSmallPacageBoxCount())&&post.getSmallPacageBoxCount()>0) {
                        t.setSmallPacageBoxCount(post.getSmallPacageBoxCount());
                    }
                    if (!ObjectUtils.isEmpty(t.getCommand())) {
                        List<String> strs = new ArrayList<>();
                        Command command = new Gson().fromJson(t.getCommand(), Command.class);
                        command.getSilkCarRecords().stream().forEach(a -> {
                            strs.add(a.id);
                        });
                        t.setSilkCarRecordsSmall(strs);
                    }
                } else if ("丝车".equals(post.getPackageType())) {
                    t.setPackageType("");
                    t.setType("BIG_SILK_CAR");
                } else if ("普通箱包-无抬头".equals(post.getPackageType())) {
                    t.setPackageType("白皮包装");
//                t.setType("MANUAL");
                } else {
                    t.setPackageType("普通箱包");
//                t.setType("MANUAL");
                }
                t.setGrossWeight(post.getGrossWeight());
                t.setNetWeight(post.getNetWeight());
                t.setGrade(getGradeReverse(post.getGrade()));
                t.setPrintClass(getPrintClassReverse(post.getPrintClass()));
                t.setBudatClass(getPrintClassReverse(post.getPrintClass()));
                t.setSilkCount(post.getSilkCount());
                if (!ObjectUtils.isEmpty(post.getFoamNum())) {
                    t.setFoamNum(post.getFoamNum());
                }
                t.setModifier("if_warehouse");
                if (!ObjectUtils.isEmpty(post.getFoamType())) {
                    t.setFoamType(post.getFoamType());
                }
                if (!ObjectUtils.isEmpty(post.getPalletType())) {
                    t.setPalletType(post.getPalletType());
                }
                if (!ObjectUtils.isEmpty(post.getAutomaticPackeLine())) {
                    t.setAutomaticPackeLine(post.getAutomaticPackeLine());
                }
                if (!ObjectUtils.isEmpty(post.getBudat())) {
                    t.setBudat(post.getBudat());
                }
                if (!ObjectUtils.isEmpty(post.getPalletCode())) {
                    t.setPalletCode(post.getPalletCode());
                }

                if (!ObjectUtils.isEmpty(post.getSapT001l())) {
                    t.setSapT001l(post.getSapT001l());
                }
                if (!ObjectUtils.isEmpty(post.getTare())) {
                    t.setTare(post.getTare());
                }
                if (!ObjectUtils.isEmpty(post.getPipeType())) {
                    t.setPipeType(post.getPipeType());
                }

                if (!ObjectUtils.isEmpty(post.getSilkCount())) {
                    t.setSilkCount(post.getSilkCount());
                }
                t.setSaleType(getSalesTypeReverse(post.getSaleType()));
                t.setInWarehouse(post.getInWarehouse());
                template.save(t, "T_PackageBox");
                //调用入库接口
                Gson gson = new Gson();
                String postData = gson.toJson(t);

//                System.out.println("newMes参数1" + postData);
//                String printData = okHttpUtils.httpPostJson("http://10.77.0.29:8080/webapi/process/LOA_WMS/ProducePalletAsync", headers, postData);
                String printData = okHttpUtils.httpPostJson("http://10.2.0.215:9999/warehouse/PackageBoxFetchEvent", headers, postData);
//                System.out.println("AAABBBB" + printData);
//                String newMes = okHttpUtils.httpPostJson("http://192.168.129.252:8090/open/automaticintegration/production/yunbiaoUpdatePackage", headers, newMesPostData);
//                System.out.println("newMes" + newMes);//                System.out.println("newMes3" + postData);
                QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, null);
                return queryResponseResult;
            }else {


                System.out.println("newMes参数2" + formattedJson);
                String newMes = okHttpUtils.httpPostJson("http://10.2.0.233:8090/open/automaticintegration/production/yunbiaoUpdatePackage", headers, formattedJson);
                System.out.println("newMes2结果" + newMes);
            }

        } catch (Exception e) {
            QueryResult queryResult = new QueryResult();
            queryResult.setMsg(e.getMessage());//数据列表
            queryResult.setTotal(1);//数据总记录数
            QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.FAIL, queryResult);
            return queryResponseResult;
        }

        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.FAIL, null);
        return queryResponseResult;

    }


    private String getGradeReverse(String grade) {
        if (ObjectUtils.isEmpty(grade)) {
            return "";
        }
        switch (grade) {
            case "B":
                return "1770980760270929960";
            case "AA":
                return "1770980569354600486";

            case "废丝":
                return "1784018919720222752";


            case "AAA":
                return "1788876173229424672";

            case "A":
                return "1770980703740100645";

            case "C":
                return "1770980834862432293";

            case "不定重":
                return "5d35d5fe87b4a50001736dca";
            case "不定重优等":
                return "5d9015d3cc3de50001497342";
            case "AA（试）":
                return "60d968ad4fcf4b000156a56c";
            default:
                return "";
        }

    }


    private String getTeamReverse(String classId) {
        if (ObjectUtils.isEmpty(classId)) {
            return "";
        }
        switch (classId) {
            case "A":
                return "5bfd4a0c67e7ad000188a0d9";
            case "B":
                return "5bfd4a0c67e7ad000188a0db";
            case "C":
                return "5bfd4a2e67e7ad000188a0e9";
            case "D":
                return "5db9654bcddd7e0001a87d48";
            default:
                return "";
        }

    }

    private String getPrintClassReverse(String classId) {
        if (ObjectUtils.isEmpty(classId)) {
            return "";
        }
        switch (classId) {
            case "甲":
                return "5bfd4a0c67e7ad000188a0d9";
            case "乙":
                return "5bfd4a0c67e7ad000188a0db";
            case "丙":
                return "5bfd4a2e67e7ad000188a0e9";
            case "丁":
                return "5db9654bcddd7e0001a87d48";
            default:
                return "";
        }

    }

    private String getSalesTypeReverse(String saleType) {
        if (ObjectUtils.isEmpty(saleType)) {
            return "DOMESTIC";
        } else {
            if (saleType.equals("外贸")) {
                return "FOREIGN";
            } else {
                return "DOMESTIC";
            }
        }

    }

    @Autowired
    private IGlobalCache globalCache;

    public QueryResponseResult delRedisByPackaged(String silkcar) throws  IOException {
        Set<String> keys = null;
        BufferedWriter writer = null;
        if (!StringUtils.isEmpty(silkcar)&&"1".equals(silkcar)) {
            keys = globalCache.keys("*RECORD*");
        } else {
            keys = globalCache.keys("*" + silkcar + "*");
        }
        if (!ObjectUtils.isEmpty(keys) && keys.size() > 0) {
            AtomicInteger i  = new AtomicInteger();
            try {
                 writer = new BufferedWriter(new FileWriter("D:\\aaaaa.txt"));
                BufferedWriter finalWriter = writer;
                keys.forEach((k) -> {
                i.getAndIncrement();
//                System.out.println("循环数量&&&&&&&&&&&："+i.get());
//                Map<Object, Object> hmget = globalCache.hmget(k);
                if(k.contains("FACTORY")){

                        // 创建一个字符串数组
                        // Create a string array
                        String[] array = {"我爱你一年", "我不爱你"};
                        // 创建一个文件写入器，并用缓冲写入器包装它
                        // Create a file writer and wrap it with a buffered writer

                        // 遍历数组中的每个元素，并将其写入文件中，每个元素占一行
                        // Loop through each element in the array and write it to the file, one element per line


                    try {
                        finalWriter.write(k+":"+globalCache.sGet(k));
                        finalWriter.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // 关闭写入器
                        // Close the writer

                        // Print a success message
                        System.out.println("写入成功");

                }
     /*           if(i.get()>6){
                   try {
                       String date = (String) globalCache.hget(k, "startDateTime");

                       if(ObjectUtils.isEmpty(date)){
                           System.out.println("date"+date);
                           System.out.println("KKKKK:"+k);
//                           System.out.println("都可看到"+globalCache.hmget(k));
                       }else {
//                           if(Long.parseLong(date)<  Long.parseLong("1682527026000")){
//                               System.out.println("昨天:"+k   );

//                           }
                       }

                   }catch (Exception e){
                       System.out.println("错无"+k);

                   }


                }*/

//                hmget.forEach((ks, kv) -> {
//                    System.out.println("kv"+kv.toString());
////                    Carbean carbean = new Gson().fromJson(kv.toString(),Carbean.class) ;
//                    System.out.println("kv"+kv.toString());
//          /*          if (kv.toString().contains("\"operator\":{\"id\":\"if_warehouse\"}")) {
//                        SilkcarHisBean silkcarHisBean = new Gson().fromJson(kv.toString(), SilkcarHisBean.class);
//                        if(!ObjectUtils.isEmpty(silkcarHisBean)){
//                            String id = silkcarHisBean.getPackageBox().id;
//                            T_PackageBox packageBox = template.findById(id, T_PackageBox.class);
//                            System.out.println(packageBox.getType());
//                            if(!ObjectUtils.isEmpty(packageBox)){
//                                if("BIG_SILK_CAR".equals(packageBox.getType())){
//                                    globalCache.del(k);
//                                }else {
//                                    System.out.println("=====NONONONO==="+packageBox.get_id());
//                                }
//
//                            }
//                        }
//
//                    }*/
//
//                });

            });
            } catch (IOException e) {
                // Print an error message
                System.out.println("写入失败");
                e.printStackTrace();
            }finally {
                writer.close();

            }
        }

        return    new QueryResponseResult(CommonCode.SUCCESS,   null) ;
    }

    public QueryResponseResult delRedisByCar(String silkcar) {
        Set<String> keys = null;

        keys = globalCache.keys("*" + silkcar + "*");
        final int[] i = {0};
        if (!ObjectUtils.isEmpty(keys) && keys.size() > 0) {

            keys.forEach((k) -> {
                if(i[0] ==0){
                    i[0] = i[0] +1 ;
                    globalCache.del(k);

                }


            });
        }

        return    new QueryResponseResult(CommonCode.SUCCESS,   null) ;
    }
}
