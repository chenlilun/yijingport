package com.likun.mongo.mongotest.service;

import com.google.gson.Gson;
import com.likun.mongo.mongotest.domain.PostYunbiaoBean;
import com.likun.mongo.mongotest.domain.Student;
import com.likun.mongo.mongotest.domain.T_Batch;
import com.likun.mongo.mongotest.domain.T_PackageBox;
import com.likun.mongo.mongotest.okhttp.OkHttpUtils;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.swing.text.DefaultFormatterFactory;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PackboxService {
    @Autowired
    private MongoTemplate template;
    @Autowired
    private OkHttpUtils okHttpUtils;
    public void  ttt(){
        Student s = new Student() ;
        s.setId("aa");
        s.setAaaBbb("aa");
        s.setGradeId("aa");
        s.setSex("aa");
        s.setStuName("aa");
        DeleteResult remove = template.remove(s);
        System.out.println("ssss"+remove.getDeletedCount());
        System.out.println("sssdddds"+remove.wasAcknowledged());
    }
    public void findAllBoxAndPull() {
        Calendar a = Calendar.getInstance();
        Calendar begin = Calendar.getInstance();
        begin.set(a.get(Calendar.YEAR),a.get(Calendar.MONTH),a.get(Calendar.DAY_OF_MONTH),0, 0,0);
        begin.add(Calendar.DAY_OF_MONTH,-1);

        Calendar end = Calendar.getInstance();
        end.set(a.get(Calendar.YEAR),a.get(Calendar.MONTH),a.get(Calendar.DAY_OF_MONTH),0, 0,0);
        end.add(Calendar.DAY_OF_MONTH,1);
        end.add(Calendar.HOUR_OF_DAY,8);



        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat spl = new SimpleDateFormat(strDateFormat);
        System.out.println("begin==================" + spl.format(begin.getTime()));
        System.out.println("end==================" + spl.format(end.getTime()));
        Criteria c2 = Criteria.where("cdt").gte(begin.getTime()).lte(end.getTime()).and("type").is("MANUAL").and("postFlag").exists(false);
//        template.findAll(Query.query(c2),"T_PackageBox") ;
        List<T_PackageBox> t_packageBox = template.find(Query.query(c2), T_PackageBox.class, "T_PackageBox");
        if (!ObjectUtils.isEmpty(t_packageBox) && t_packageBox.size() > 0) {
            t_packageBox.stream().forEach(b -> {
                //调用接口
                PostYunbiaoBean bean = new PostYunbiaoBean();
                bean.setFactoryCode("9210");
                bean.setInboundFlag(false);
                T_Batch batch = template.findById(b.getBatch(), T_Batch.class, "T_Batch");
                if (!ObjectUtils.isEmpty(batch)) {
                    bean.setColor(batch.getTubeColor());
                }
                if (!ObjectUtils.isEmpty(b.getCdt())) {
                    //设置入库时间
                    bean.setInboundDate( getAddEightHour(b.getCdt(),spl)  );
                    bean.setProduceTime( getAddEightHour(b.getCdt(),spl)  );
                }
                bean.setPallet(getPallet(b.getPalletType()));
                bean.setPackageType(b.getPackageType());
                bean.setProductName(getProDuctName(batch.getProduct()));
                bean.setSpec(batch.getSpec());
                bean.setFoamCount(b.getFoamNum());
                bean.setSalesType(getSalesType(b.getSaleType()));
                bean.setBatchNo(batch.getBatchNo());
                bean.setWorkshop(getWorkShop(batch.getWorkshop()));
                bean.setFactoryName("杭州逸暻生产工厂");
                bean.setPalletType(b.getPalletType());
                bean.setUId(b.getCode());
                bean.setLgort(b.getSapT001l());
                bean.setFoamType(b.getFoamType());
                bean.setGrade(getGrade(b.getGrade()));
                String value=b.getCode();
                 int length=value.length();

                if(length>=3){
                    bean.setSequence(value.substring(length-3,length));
                }
                bean.setClassesInfo(getPrintClass(b.getPrintClass()));  // 甲乙丙丁
                bean.setTeamInfo(getTeam(b.getPrintClass())); // ABC
                List<PostYunbiaoBean.ProductInfoEntity> list = new ArrayList<>() ;
                PostYunbiaoBean.ProductInfoEntity productInfoEntity = new PostYunbiaoBean.ProductInfoEntity() ;
                productInfoEntity.setClasses(getPrintClass(b.getPrintClass()));
                if(!ObjectUtils.isEmpty(b.getGrossWeight())){
                    productInfoEntity.setGrossWeight(b.getGrossWeight());
                }
                if(!ObjectUtils.isEmpty(b.getNetWeight())){
                    productInfoEntity.setNetWeight(b.getNetWeight());
                }
                productInfoEntity.setTeam(getTeam(b.getPrintClass()));
                productInfoEntity.setClasses(getPrintClass(b.getPrintClass()));
                productInfoEntity.setNewProductCode(b.getCode());
                productInfoEntity.setOperatorId("MANUAL");
                productInfoEntity.setPrintFlag(false);

                if(length>=3){
                    productInfoEntity.setSequence(value.substring(length-3,length));
                }

                productInfoEntity.setSilkCount(b.getSilkCount());
                productInfoEntity.setSilkWeight(batch.getSilkWeight());
                productInfoEntity.setProductDate(getProDuctDate(b.getCdt()));
                productInfoEntity.setPrintTime("1970-1-1");
                productInfoEntity.setPrintCount(0);
                list.add(productInfoEntity) ;
                bean.setProductInfo(list);
                Gson gson = new Gson();
                String postData = gson.toJson(bean);
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
//        String printData = okHttpUtils.httpPostJson("http://10.77.0.24:8090/api/doff/getSilkOnLineForWorkshop", headers, postData);
                System.out.println(postData);
//                String printData = okHttpUtils.httpPostJson("http://10.77.0.29:8080/webapi/process/LOA_WMS/ProducePalletAsync", headers, postData);
                String printData = okHttpUtils.httpPostJson("http://10.61.0.19:8080/webapi/process/LOA/ProducePalletAsync", headers, postData);
                System.out.println("AAAAAAAA" + printData);
                if(!ObjectUtils.isEmpty(printData)&&printData.contains("200")){
                    //                printData.if
                    b.setPostFlag("post");
                    template.save(b,"T_PackageBox") ;
                }


            });
        }
    }

    private String getAddEightHour(Date cdt, SimpleDateFormat spl ) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cdt);
        calendar.add(Calendar.HOUR_OF_DAY,8);
        String format = spl.format(calendar.getTime());
        System.out.println("创建时间======="+format);
        return format;
    }

    private String getProDuctDate(Date cdt) {
        String strDateFormat = "yyyy-MM-dd";
        SimpleDateFormat spl = new SimpleDateFormat(strDateFormat);
        Calendar cal=Calendar.getInstance();
        cal.setTime(cdt);
        cal.add(Calendar.HOUR_OF_DAY,8);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if(hour<8){
            cal.add(Calendar.HOUR_OF_DAY,-8);
        }

        return spl.format(cal.getTime());
    }

    private String getPrintClass(String classId) {
        if(ObjectUtils.isEmpty(classId)){
            return "" ;
        }
        switch (classId){
            case "5bfd4a0c67e7ad000188a0d9":
                return "甲";
            case "5bfd4a0c67e7ad000188a0db":
                return "乙";
            case "5bfd4a2e67e7ad000188a0e9":
                return "丙";
            case "5db9654bcddd7e0001a87d48":
                return "丁";
            default:
                return "" ;
        }

    }
    private String getTeam(String classId) {
        if(ObjectUtils.isEmpty(classId)){
            return "" ;
        }
        switch (classId){
            case "5bfd4a0c67e7ad000188a0d9":
                return "A";
            case "5bfd4a0c67e7ad000188a0db":
                return "B";
            case "5bfd4a2e67e7ad000188a0e9":
                return "C";
            case "5db9654bcddd7e0001a87d48":
                return "D";
            default: return "";
        }

    }
    private String getGrade(String grade) {
        if(ObjectUtils.isEmpty(grade)){
            return "" ;
        }
        switch (grade){
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
            default:
                return "";
        }

    }

    private String getWorkShop(String workshop) {
        if(ObjectUtils.isEmpty(workshop)){
            return "" ;
        }
        switch (workshop){
            case "5c877549a3f0a02467a817f0":
                return  "纺丝C车间" ;
            case "5c877549a3f0a02467a817ef":
                return  "纺丝D车间" ;
            case "5cdcb312039aab00016f6544":
                return  "纺丝A车间" ;
            case "5cdcb6f0039aab0001729ef0":
                return  "纺丝B车间" ;
            default:       return "";
        }

    }

    private String getSalesType(String saleType) {
        if(ObjectUtils.isEmpty(saleType)){
            return "内贸";
        }else {
          if(saleType.equals("FOREIGN")){
              return "外贸";
          }  else {
              return "内贸";
          }
        }

    }

    private String getProDuctName(String product) {

        return product==null?"":product.equals("5bffa63c8857b85a437d1f92")?"POY":"FDY";
    }

    private String getPallet(String palletType) {
        if (ObjectUtils.isEmpty(palletType)) {
            return "K";
        } else if (palletType.contains("塑")) {
            return "S";
        } else  {
            return "M";
        }

    }

//    public static void main(String[] args) {
//        findAllBoxAndPull() ;
//    }
    public void test(){
        Student stu = template.findById("bdd28109-1396-49f5-ae84-dc7254e1faeb", Student.class);
        stu.setAaaBbb("aaa");
        template.save(stu) ;
    }
}
