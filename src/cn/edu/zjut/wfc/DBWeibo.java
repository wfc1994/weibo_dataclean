package cn.edu.zjut.wfc;

import cn.edu.zjut.myong.com.weibo.Weibo;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.Date;

//操作mongoDB的类
public class DBWeibo {
    private MongoClient mClient;
    private MongoCollection<Weibo> mColl;

    public DBWeibo(String db, String coll, String host, int port) {
        mClient = new MongoClient(host, port);// 连接到 mongodb 服务，本地填写localhost
        MongoDatabase mDB = mClient.getDatabase(db);//连接到数据库
        // 建立集合
        CodecRegistry registry = CodecRegistries.fromRegistries(
                MongoClient.getDefaultCodecRegistry(), CodecRegistries.fromCodecs(new WeiboCodec()));
        mColl = mDB.getCollection(coll, Weibo.class).withCodecRegistry(registry);//获取集合
        // 建立索引
        mColl.createIndex(
                Indexes.ascending( "userId", "weiboId"),
                new IndexOptions().unique(true).name("repostIdIndex"));
        mColl.createIndex(
                Indexes.ascending(  "weiboId"),
                new IndexOptions().name("weiboIdIndex"));
        mColl.createIndex(
                Indexes.ascending(  "sourceWeiboId"),
                new IndexOptions().name("sourceWeiboIdIndex"));
        mColl.createIndex(
                Indexes.ascending("time"),
                new IndexOptions().name("timeIndex"));
        mColl.createIndex(
                Indexes.ascending( "preUserName"),
                new IndexOptions().name("preUserNameIndex"));
        mColl.createIndex(
                Indexes.ascending( "userName"),
                new IndexOptions().name("userNameIndex"));
    }

    public MongoCollection<Weibo> getWeiboCollection() {
        return mColl;
    }


    public MongoClient getmClient() {
        return mClient;
    }

    public MongoCursor<Weibo> findWeibosWithin(Date dFrom, Date dEnd) {
        return mColl.find(new BasicDBObject(
                "time",
                new BasicDBObject()
                        .append("$lt", dEnd)
                        .append("$gte", dFrom)))
                .sort(new BasicDBObject("time", 1))
                .batchSize(500)
                .iterator();
    }

    public Weibo getWeibo(String weiboId) {
        MongoCursor<Weibo> cursor = mColl.find(new BasicDBObject("weiboId", weiboId)).batchSize(500).iterator();//查询
        if (cursor.hasNext()) {
            return cursor.next();
        } else {
            return null;
        }
    }

    public MongoCursor<Weibo> findWeibos(String uid, Date dFrom, Date dEnd) {
        BasicDBObject query = new BasicDBObject();
        query.put("userId", uid);
        query.put("time", new BasicDBObject()
                .append("$lt", dEnd)
                .append("$gte", dFrom));
        return mColl.find(query).sort(new BasicDBObject("time", 1)).batchSize(500).iterator();
    }

    public boolean containWeibo(String weiboId) {
        return mColl.count(new BasicDBObject("weiboId", weiboId)) > 0;
    }

    /**
     * 关闭数据库。
     */
    public void close() {
        try {
            mClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
