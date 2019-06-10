package cn.edu.zjut.wfc;

import cn.edu.zjut.myong.com.weibo.Weibo;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.Transaction;

import java.io.File;
import java.util.Date;
import java.util.List;

//对batch2的数据解序列化的操作
public class test {
    public static void main(String[] args){
        BerkeleyDB<Weibo> db = new BerkeleyDB<Weibo>("G:\\微博数据\\batch2\\batch\\bdb-local1-0321\\rclient0","REPOST",Weibo.class);
        db.getData();
        System.out.println(new Date());
    }
}
