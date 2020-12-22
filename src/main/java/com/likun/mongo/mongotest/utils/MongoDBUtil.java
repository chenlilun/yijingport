package com.likun.mongo.mongotest.utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;

public class MongoDBUtil {

    //不认证
    public static MongoDatabase getConnect(String host, int port, String dbName){
        MongoClient mongoClient = new MongoClient(host, port);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        mongoClient.close();
        return mongoDatabase;
    }

    public static MongoDatabase getConnect(String dbName){
        return getConnect("127.0.0.1", 27017,dbName);
    }

    //认证
    public static MongoDatabase getConnectWithAuth(String host,int port,String dbName,String userName,String password){
        List<ServerAddress> adds = new ArrayList<>();
        ServerAddress serverAddress = new ServerAddress(host, port);
        adds.add(serverAddress);
        List<MongoCredential> credentials = new ArrayList<>();
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(userName, dbName, password.toCharArray());
        credentials.add(mongoCredential);
        MongoClient mongoClient = new MongoClient(adds, credentials);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        mongoClient.close();
        return mongoDatabase;
    }

/*    public static MongoDatabase getConnectWithAuth(String dbName,String userName,String password){
        return getConnectWithAuth("127.0.0.1", 27017, dbName, userName, password);
    }*/

}
