package cn.edu.zjut.wfc;

import cn.edu.zjut.myong.com.weibo.Weibo;
import cn.edu.zjut.wfc.util.FileTool;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import javax.json.Json;
import java.util.ArrayList;
import java.util.List;

public class MongoDBJDBC{
    public static void main( String args[] ){
        try{
            DBWeibo db = new DBWeibo("weibo", "REPOST", "localhost", 27017);
            MongoCollection<Weibo> coll = db.getWeiboCollection();
            MongoCursor<Weibo> cursor = coll.find(new BasicDBObject("sourceWeiboId", "")).batchSize(10000).iterator();//sourceWeiboId为空
            Weibo weibo = null;
            List<String> rootCountList = new ArrayList<>();//收集转发信息
            int i=1;
            while (cursor.hasNext()) {
                weibo = cursor.next();
                String sourceWeiboId=weibo.getWeiboId();
                BasicDBObject query =new BasicDBObject();
                query.put("sourceWeiboId",sourceWeiboId);//sourceWeiboId相同的节点，也就是一棵转发树
                long count=coll.count(query);
                String rootCount= Json.createObjectBuilder()
                        .add("sourceWeiboId", sourceWeiboId)
                        .add("count", count).build().toString();
                if (count!=0)
                    rootCountList.add(rootCount);
                System.out.println(i);
                i++;
            }
            //保存成json文件
            FileTool fileTool=new FileTool();
            fileTool.writeByLinestoJson("E:\\微博数据\\data\\Node数据不为0.json",rootCountList);
            System.out.println("成功!");
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}