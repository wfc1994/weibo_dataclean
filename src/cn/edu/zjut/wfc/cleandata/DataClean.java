package cn.edu.zjut.wfc.cleandata;

import cn.edu.zjut.myong.com.weibo.Weibo;
import cn.edu.zjut.wfc.DBWeibo;
import cn.edu.zjut.wfc.NodeResult;
import cn.edu.zjut.wfc.PreNode;
import cn.edu.zjut.wfc.RootCount;
import cn.edu.zjut.wfc.util.FileTool;
import cn.edu.zjut.wfc.util.Similarity;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.json.Json;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataClean{

    /**
     * 找出根微博为sourceId的一个完整转发数，导出成json文件，对数据进行清洗，加入flag字段，并导出成json文件
     * @param sourceId 根微博id
     * @param count 微博数量
     * @param coll 完整表
     * @return 一个完整转发树
     * @throws IOException
     */
    public NodeResult ReadTree(String sourceId, int count, MongoCollection<Weibo> coll) throws IOException {
        Logger log = getLogger(sourceId);

        //先查找根微博
        BasicDBList querylist = new BasicDBList();
        BasicDBObject queryroot = new BasicDBObject();
        queryroot.put("sourceWeiboId", "");
        queryroot.put("weiboId",sourceId);
        BasicDBObject querytree = new BasicDBObject();
        querytree.put("sourceWeiboId", sourceId);
        querylist.add(queryroot);
        querylist.add(querytree);
        BasicDBObject query = new BasicDBObject();
        query.put("$or", querylist);

        //查找所有sourceWeiboId为xx的微博，也就是一棵转发树的所有微博内容
//        FindIterable<Weibo> findIterable = coll.find(query).noCursorTimeout(true);//游标可能超时
        MongoCursor<Weibo> cursor = coll.find(query).batchSize(10000).iterator();
        List<String> forwardList = new ArrayList<>();//收集转发信息
        int flag0 = 0, flag1 = 0, flag2 = 0, flag3 = 0, flag4 = 0, flag5 = 0;//记录匹配类型
        double f0=0,f1=0,f2=0,f3=0,f4=0,f5=0;//比率
        List<Weibo> WeiboList=new ArrayList<Weibo>();
        HashMap<String, List<Weibo>> WeiboMap = new HashMap<>();
        List<Weibo> list=null;
        //先加入List和Map
        while (cursor.hasNext()) {
            Weibo weibo=cursor.next();
            String UserName = weibo.getUserName();//以userName为key
            WeiboList.add(weibo);
            list=WeiboMap.get(UserName);//根据key(preUserName)获取value(list)
            if (list==null){//list为null,UserName还不在hashMap中
                list=new ArrayList<Weibo>();
                list.add(weibo);
                WeiboMap.put(UserName,list);
            }else {//preUserName已经在hashMap中,因此只将weibo添加进去即可，不用put
                list.add(weibo);
            }
        }

        for (Weibo leafWeibo:WeiboList){//遍历每一条微博，找前继节点

            //获取当前微博信息
            String weiboId = leafWeibo.getWeiboId();
            String userId = leafWeibo.getUserId();
            String sourceWeiboId = leafWeibo.getSourceWeiboId();
            String userName = leafWeibo.getUserName();
            String category = leafWeibo.getCategory()+"";
            String url = leafWeibo.getUrl();
            Date time = leafWeibo.getTime();
            int repostNum = leafWeibo.getRepostNum();
            int commentNum = leafWeibo.getCommentNum();
            int liekNum = leafWeibo.getLikeNum();
            String tag = leafWeibo.getTag();
            String  preUserId = leafWeibo.getPreUserId();
            String preUserName = leafWeibo.getPreUserName();
            String sourceUserId = leafWeibo.getSourceUserId();
            String sourceUserName = leafWeibo.getSourceUserName();
            int sourceWeiboRepostNum = leafWeibo.getSourceWeiboRepostNum();
            int sourceWeiboCommentNum = leafWeibo.getSourceWeiboCommentNum();
            int sourceWeiboLikeNum = leafWeibo.getSourceWeiboLikeNum();
            String content = leafWeibo.getContent();

            boolean isRepost = true;
            if(weiboId.equals(sourceId)){
                isRepost = false;
            }


            //初始化前继节点的信息，默认sourceWeiboId为前继节点Id，flag为0表示能完全匹配
            PreNode preNode = new PreNode(weiboId, sourceWeiboId, sourceWeiboId, isRepost, 0,"",userId,userName,category,url,time,repostNum,commentNum,liekNum,tag,preUserId,preUserName,sourceUserId,sourceUserName,sourceWeiboRepostNum,sourceWeiboCommentNum,sourceWeiboLikeNum,content);//默认根节点
            log.info("--------------------------开始---------------------------");
            if (preUserName.equals("")) {//preUserName为空,将当前微博连接到根节点，flag设置为5
                preNode.setFlag(5);//flag为5表示无前继节点
                log.info("无前继节点-->"+preUserName);
                flag5++;
                forwardList.add(preNode.toString());
                continue;//前继节点找到了，重新循环，下一条微博
            }
            List<Weibo> preWeiboList=WeiboMap.get(preUserName);//取出数组,“userName”为preUserName
            if (preWeiboList==null) {//PreWeiboList为空,将当前微博连接到根节点，flag设置为4
                log.info("前继节点无微博-->"+preWeiboList);
                preNode.setFlag(4);//有userName，无文本
                flag4++;
                forwardList.add(preNode.toString());
                continue;//continue之前将其加入forwardList
            }
            //截取转发内容
            String str = "//@" + preUserName + ":";
            int length1 = content.indexOf(str);
            int length2 = str.length();
            String tempContent = content.substring(length1 + length2);
            String forwardContent=CleanText(tempContent);//赞[100]去掉，空格去掉
            log.info("原微博--->"+content);
            log.info("原微博name--->"+preUserName);
            log.info("处理后--->"+tempContent);
            log.info("清理后--->"+forwardContent);

            //各种List
            List<Weibo> preTimeWeiboList = new ArrayList<>();//符合时间条件的存下来
            List<Weibo> preVMWeiboList = new ArrayList<>();//startsWith  Vague Match
            for(Weibo preWeibo:preWeiboList){//PreWeiboList.size>0
                //获取当前微博信息
                String preWeiboId = preWeibo.getWeiboId();
                String preContent = preWeibo.getContent();
                Date preTime = preWeibo.getTime();
                //先匹配时间
                int compareTo = preTime.compareTo(time);//比较时间,小于返回-1，等于返回0，大于返回1
                if (compareTo <= 0) {//只要早于或等于原微博，就认为这是前继微博,直接return true
                    preTimeWeiboList.add(preWeibo);//时间对，加入
                    String cleanPreContent=CleanText(preContent);//去掉赞，必要的清理
                    if (cleanPreContent.equals(forwardContent)) {
                        preNode.setPreWeiboId(preWeiboId);//只取第一个
                        preNode.setFlag(0);
                        preNode.setSimilarity("1");
                        flag0++;
                        log.info("精确匹配--->"+preContent);
                        log.info("清理后--->"+CleanText(preContent));
                        break;
                    } else {//需要模糊匹配
                        preVMWeiboList.add(preWeibo);
                    }
                }
            }
            //先时间匹配，后精确匹配，后模糊匹配
            if (preTimeWeiboList.size() <= 0) {//时间匹配不上
                preNode.setFlag(3);//直接连根节点
                flag3++;
                log.info("时间匹配不上--->"+preWeiboList.get(0).getTime());
            }  else if((preVMWeiboList.size()>0)&&(preNode.getSimilarity().equals(""))){//文本能模糊匹配，相似度>0.7
                double cScore=0;
                Weibo cPreVMWeibo=preVMWeiboList.get(0);
                for(Weibo preVMWeibo:preVMWeiboList){
                    String preContent=preVMWeibo.getContent();
                    String cleanPreContent=CleanText(preContent);//去掉赞
                    double score= Similarity.getSimilarity(cleanPreContent,forwardContent);//相似度
                    if (score>=cScore){
                        cScore=score;
                        cPreVMWeibo=preVMWeibo;
                    }
                }
                preNode.setPreWeiboId(cPreVMWeibo.getWeiboId());
                preNode.setSimilarity(Double.toString(cScore));
                log.info("相似度--->"+cScore);
                if(cScore>=0.7){
                    preNode.setFlag(1);
                    flag1++;
                    log.info("模糊匹配--->"+cPreVMWeibo.getContent());
                    log.info("匹配清理后---->"+CleanText(cPreVMWeibo.getContent()));
                }else{
                    preNode.setFlag(2);
                    flag2++;
                    log.info("更模糊匹配--->"+cPreVMWeibo.getContent());
                    log.info("匹配清理后---->"+CleanText(cPreVMWeibo.getContent()));
                }
            }

            forwardList.add(preNode.toString());
            log.info("--------------------------结束---------------------------");
        }

        if (count!=0){
            f0 = new BigDecimal((double) flag0 / (double) count).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            f1 = new BigDecimal((double) flag1 / (double) count).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            f2 = new BigDecimal((double) flag2 / (double) count).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            f3 = new BigDecimal((double) flag3 / (double) count).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            f4 = new BigDecimal((double) flag4 / (double) count).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            f5 = new BigDecimal((double) flag5 / (double) count).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        NodeResult result = new NodeResult(sourceId, count, flag0, f0, flag1, f1,flag2, f2,flag3, f3,flag4, f4, flag5, f5);
        log.info(result.toString());
        //保存成json文件
        FileTool fileTool = new FileTool();
        String fileName = "G:\\微博数据\\data\\tree\\treeNode-" + sourceId + ".json";
        fileTool.writeByLinestoJson(fileName, forwardList);
        return result;
    }

    public static Logger getLogger(String logName) {
        String logFile="E:\\微博数据\\data\\log\\"+logName;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        Properties logProperties = new Properties();
        logProperties.put("log4j.rootLogger", "INFO, file, stdout");
        logProperties.put("log4j.appender.file", "org.apache.log4j.RollingFileAppender");
        logProperties.put("log4j.appender.file.File", logFile+".log");
        logProperties.put("log4j.appender.file.MaxFileSize", "1GB");
        logProperties.put("log4j.appender.file.MaxBackupIndex", "10");
        logProperties.put("log4j.appender.file.layout", "org.apache.log4j.PatternLayout");
        logProperties.put("log4j.appender.file.layout.ConversionPattern", "%d{MM-dd HH:mm:ss} %-5p | %m%n");
        logProperties.put("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        logProperties.put("log4j.appender.stdout.Target", "System.out");
        logProperties.put("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        logProperties.put("log4j.appender.stdout.layout.ConversionPattern", "%d{MM-dd HH:mm:ss} %-5p | %m%n");
        PropertyConfigurator.configure(logProperties);
        Logger log = Logger.getLogger(DataClean.class.getName());
        return log;
    }

    public static String CleanText(String content){
        String tempContent=content.replaceAll("\\s*", "").
                replaceAll(" +","").replaceAll("\\s+", "").trim();//先去掉
        String pattern = "赞\\[.*?\\]";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(tempContent);
        String cleanContent=tempContent;
        while(m.find())
        {
            if(m.start()>0) {
                cleanContent = tempContent.substring(0, m.start());
            }
        }
        return cleanContent.trim();
    }

    public void savetreedata(){
        try{
            System.out.println("起始时间:"+new Date());
            DBWeibo db = new DBWeibo("weibo", "REPOST", "localhost", 27017);
            MongoCollection<Weibo> coll = db.getWeiboCollection();
            MongoCursor<Weibo> cursor = coll.find(new BasicDBObject("sourceWeiboId", "A0gLi6zwT")).batchSize(10000).iterator();//查询collection中的所有数据
            Weibo weibo = null;
            List<String> rootCountList = new ArrayList<>();//收集转发信息
            while (cursor.hasNext()) {
                weibo = cursor.next();
                String sourceWeiboId = weibo.getWeiboId();
                BasicDBObject query =new BasicDBObject();
                query.put("sourceWeiboId",sourceWeiboId);//sourceWeiboId相同的节点，也就是一棵转发树
                String rootCount= weibo.toJson().build().toString();
                rootCountList.add(rootCount);
            }
            //保存成json文件
            FileTool fileTool=new FileTool();
            fileTool.writeByLinestoJson("E:\\微博数据\\data\\未清洗的完整数据.json",rootCountList);
            System.out.println("终止时间:"+new Date());
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    /**
     * 找到数据库中的根节点，统计根出现的次数
     */
    public void findroot(){
        try{
            DBWeibo db = new DBWeibo("weibo", "REPOST", "localhost", 27017);
            MongoCollection<Weibo> coll = db.getWeiboCollection();
            MongoCursor<Weibo> cursor = coll.find(new BasicDBObject("sourceWeiboId", "")).batchSize(10000).iterator();//sourceWeiboId为空
            Weibo weibo = null;
            List<String> rootCountList = new ArrayList<>();//收集转发信息
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
            }
            //保存成json文件
            FileTool fileTool=new FileTool();
            fileTool.writeByLinestoJson("E:\\微博数据\\data\\Node数据不为0.json",rootCountList);
            System.out.println("成功!");
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    /**
     * 找到数据库中的根节点，找出根微博所对应完整的树
     */
    public void findtree(){
        try{
            DBWeibo db = new DBWeibo("weibo", "REPOST", "localhost", 27017);
            MongoCollection<Weibo> coll = db.getWeiboCollection();

            FindIterable<Weibo> findIterable = coll.find(new BasicDBObject("sourceWeiboId", "")).noCursorTimeout(true);//游标可能超时
            MongoCursor<Weibo> cursor = findIterable.batchSize(10000).iterator();//sourceWeiboId为空

            Weibo weibo = null;
            String sourceWeiboId = null;
            long count=0;
            int num = 0;
            while (cursor.hasNext()) {
                weibo = cursor.next();
                sourceWeiboId = weibo.getWeiboId();
                BasicDBObject query = new BasicDBObject();
                count = coll.count(query);
                this.ReadTree(sourceWeiboId,(int)count,coll);
                num++;
                System.out.println(num);
            }

        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    /**
     * 先构建一个根节点列表，按照根节点去遍历
     * @throws IOException
     */
    public void savetree() throws IOException{
        File file = new File("G:\\微博数据\\data\\Node数据不为0.json");
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        DBWeibo db = new DBWeibo("weibo", "REPOST", "localhost", 27017);
        MongoCollection<Weibo> coll = db.getWeiboCollection();
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        List<String> sourceweiboIdlist = new ArrayList<String>();
        List<Integer> countlist = new ArrayList<Integer>();
        String brs = br.readLine();
        while(brs!=null){
            String[] temp = brs.split(",");
            String[] temp1 = temp[0].split(":");
            String sourceweiboId = temp1[1].substring(1, temp1[1].length()-1);
            sourceweiboIdlist.add(sourceweiboId);
            String[] temp2 = temp[1].split(":");
            Matcher m = p.matcher(temp2[1]);
            int count = Integer.parseInt( m.replaceAll("").trim());
            countlist.add(count);
            brs = br.readLine();
        }
        System.out.println(sourceweiboIdlist.size()+" "+countlist.size());

        for(int i=57701;i<sourceweiboIdlist.size();i++){
            String s = sourceweiboIdlist.get(i);
            int c = countlist.get(i);
            this.ReadTree(s,c,coll);
        }
        db.close();
    }

    public static void main(String args[]){
        DataClean dataClean = new DataClean();
//        dataClean.findroot();
//        dataClean.savetreedata();
//        DBWeibo db = new DBWeibo("weibo", "REPOST", "localhost", 27017);
//        MongoCollection<Weibo> coll = db.getWeiboCollection();
//        dataClean.findtree();
//        db.close();
        try {
            dataClean.savetree();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}