package cn.edu.zjut.wfc;

import cn.edu.zjut.myong.com.weibo.Weibo;
import cn.edu.zjut.wfc.util.FileTool;
import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.IntegerBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.*;
import org.bson.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

public class BerkeleyDB<T> {

    Environment env;
    Database db;
    Database catalog;
    EntryBinding<T> bind;
    SortedMap<String, T> map;

    public BerkeleyDB(String envDir, String dbName, Class tClass) {
        // 环境目录是否存在
        File dir = new File(envDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        // 定义环境
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setConfigParam(EnvironmentConfig.CLEANER_MIN_UTILIZATION, "90");
        envConfig.setConfigParam(EnvironmentConfig.CLEANER_MIN_FILE_UTILIZATION, "50");
        envConfig.setTransactional(true);
        envConfig.setAllowCreate(true);
        env = new Environment(dir, envConfig);

        // 开始数据库启动事务
        Transaction txn = env.beginTransaction(null, null);

        // 定义数据库
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(true);
        dbConfig.setAllowCreate(true);
        dbConfig.setSortedDuplicates(true);
        db = env.openDatabase(txn, dbName, dbConfig);

        // 定义绑定
        DatabaseConfig catalogConfig = new DatabaseConfig();
        catalogConfig.setTransactional(true);
        catalogConfig.setAllowCreate(true);
        catalog = env.openDatabase(txn, dbName + "_catalog", catalogConfig);
        StoredClassCatalog scCatalog = new StoredClassCatalog(catalog);
        bind = new SerialBinding<T>(scCatalog, tClass);

        // 映射
        map = new StoredSortedMap<>(db, TupleBinding.getPrimitiveBinding(String.class), bind, true);

        // 提交事务
        txn.commit();
    }

    public void close() {
        catalog.close();
        db.close();
        env.cleanLog();
        env.close();
    }

    public void cleanLog() {
        env.cleanLog();
    }

    public long size() {
        return db.count();
    }

    public void put(String key, T value) {
        // DatabaseEntry represents the key and data of each record
        DatabaseEntry keyEntry = new DatabaseEntry();
        DatabaseEntry dataEntry = new DatabaseEntry();
        // 复制数据
        StringBinding.stringToEntry(key, keyEntry);
        bind.objectToEntry(value, dataEntry);
        // 写入
        Transaction txn = env.beginTransaction(null, null);
        OperationStatus status = db.put(txn, keyEntry, dataEntry);
        if (status != OperationStatus.SUCCESS) {
            throw new RuntimeException("Data insertion got status " + status);
        }
        txn.commit();
    }

    public T get(String key) {
        // DatabaseEntry represents the key and data of each record
        DatabaseEntry keyEntry = new DatabaseEntry();
        DatabaseEntry dataEntry = new DatabaseEntry();
        // 复制数据
        StringBinding.stringToEntry(key, keyEntry);
        // 读取
        OperationStatus status = db.get(null, keyEntry, dataEntry, LockMode.DEFAULT);
        if (status != OperationStatus.SUCCESS) {
            throw new RuntimeException("Data query got status " + status);
        }
        // 返回结果
        return bind.entryToObject(dataEntry);
    }

    public List<T> getAll() {
        // DatabaseEntry represents the key and data of each record
        DatabaseEntry keyEntry = new DatabaseEntry();
        DatabaseEntry dataEntry = new DatabaseEntry();
        // retrieve all data
        List<T> data = new ArrayList<>();
        Cursor cursor = db.openCursor(null, null);
        while (cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            int key = IntegerBinding.entryToInt(keyEntry);
            T value = bind.entryToObject(dataEntry);
            data.add(value);
        }
        cursor.close();
        // 返回结果
        return data;
    }

    //遍历一个文件夹中的数据
    public List<T> getData() {
        // 连接到 mongodb 服务
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        // 连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase("weibo2");
        System.out.println("Connect to database successfully");
        MongoCollection<Document> collection = mongoDatabase.getCollection("test");
        System.out.println("集合 test 选择成功");


        // DatabaseEntry represents the key and data of each record
        DatabaseEntry keyEntry = new DatabaseEntry();    DatabaseEntry dataEntry = new DatabaseEntry();
        // retrieve all data
        Cursor cursor = db.openCursor(null, null);
        while (cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            int key = IntegerBinding.entryToInt(keyEntry);
            T value = bind.entryToObject(dataEntry);
            Weibo v = (Weibo) value;
            Document s = new Document();
            s.append("content",v.toString());
            collection.insertOne(s);
        }
        cursor.close();

        // 返回结果
        return null;
    }

    public boolean contains(String key) {
        return map.containsKey(key);
    }

}
