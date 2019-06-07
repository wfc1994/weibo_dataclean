package cn.edu.zjut.wfc;

import cn.edu.zjut.myong.com.weibo.Weibo;

import java.io.File;

public class test {
    public static void main(String[] args){
        BerkeleyDB<Weibo> db = new BerkeleyDB<Weibo>("E:\\微博数据\\batch2\\batch\\bdb-local1-0321\\rclient0","REPOST",Weibo.class);
//        List<Weibo> a = db.用游标
//        System.out.println(a);
    }
}
