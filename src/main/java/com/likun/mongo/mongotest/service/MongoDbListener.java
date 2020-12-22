package com.likun.mongo.mongotest.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.likun.mongo.mongotest.utils.MongoDBUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.singletonList;

public class MongoDbListener{
    private volatile MongoDatabase database;
    private String DBHOST;
    private String DBUSER;
    private String DBPWD;
    private String DBNAME;
    private int DBPORT;

    public MongoDbListener(String host, int port, String user, String pwd, String dbname) {
        this.DBHOST = host;
        this.DBUSER = dbname;
        this.DBPWD = pwd;
        this.DBNAME = user;
        this.DBPORT = port;
        database = MongoDBUtil.getConnectWithAuth(DBHOST, DBPORT, DBUSER, DBPWD, DBNAME);
    }
    public void watchDB(){
        List<Bson> pipeline = singletonList(Aggregates.match(Filters.or(
                Document.parse("{'fullDocument.username': 'process_engine'}"),
                Filters.in("operationType", Arrays.asList("insert", "update", "delete")))));
        MongoCursor<ChangeStreamDocument<Document>> cursor = database.getCollection("teacher").watch(pipeline).iterator();
        JsonParser jsonParser = new JsonParser();
        String message = null;
        while (cursor.hasNext()){
            ChangeStreamDocument<Document> next = cursor.next();
            String Operation = next.getOperationType().getValue();
            String tableName = next.getNamespace().getCollectionName();
            //获取主键id的值
            assert next.getDocumentKey() != null;
            String pk_id = jsonParser.parse(next.getDocumentKey().toJson()).getAsJsonObject().get("_id").getAsString();
            //同步修改数据的操作
            if (next.getUpdateDescription() != null) {
                assert next.getUpdateDescription().getUpdatedFields() != null;
                JsonObject jsonObject = jsonParser.parse(next.getUpdateDescription().getUpdatedFields().toJson()).getAsJsonObject();
                message = parseJson(jsonObject, tableName, pk_id, Operation);
            }
            //同步插入数据的操作
            if (next.getFullDocument() != null) {
                JsonObject jsonObject = jsonParser.parse(next.getFullDocument().toJson()).getAsJsonObject();
                message = parseJson(jsonObject, tableName, pk_id, Operation);
                System.out.println(jsonObject.toString()+"-----"+tableName+pk_id+"--========"+Operation);

            }
            //同步删除数据的操作
            if (next.getUpdateDescription() == null && Operation.matches("delete")) {
                JsonObject jsonObject = jsonParser.parse(pk_id).getAsJsonObject();
                message = parseJson(jsonObject, tableName, pk_id, Operation);
            }

//            System.out.println(message);
        }

    }
    private  String parseJson(JsonObject object, String tableName, String pkid, String Operation) {
        String eventType = object.toString(); //更改的事件名称,具体的事件
        StringBuilder sb = new StringBuilder();
        /**
         * 将主键Id插入到pk_Id中去
         */
        if (Operation.matches("insert")) {
            sb.append(eventType);
            System.out.println(eventType+"===="+object.toString());
        } else if (Operation.matches("update")) {
        } else if (Operation.matches("delete")) {

        }
        return sb.toString();
    }


    public static void main(String[] args) {

        String MONGO_DBHOST = "10.61.0.13";
        int MONGO_DBPORT =27017;
        String MONGO_DBUSER ="mes-auto";
        String MONGO_DBPWD ="mes-auto-mongo@com.hengyi.japp";
        String MONGO_DBNAME ="mes-auto";
        MongoDbListener listener = new MongoDbListener(MONGO_DBHOST, MONGO_DBPORT, MONGO_DBUSER, MONGO_DBPWD, MONGO_DBNAME);
        listener.watchDB();
    }

}
