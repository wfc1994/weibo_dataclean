package cn.edu.zjut.myong.com.weibo;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.io.Serializable;
import java.util.Date;

public class Weibo implements Serializable, Comparable<Weibo> {

    private static final long serialVersionUID = 2013074196271988658L;

    public enum Category {
        Normal,
        Recommendation,
        Advertisement,
        Hot
    }

    public Weibo() { }

    public Weibo(String userId, String userName, String weiboId,
                 Category category, String content, String url, Date time, String tag) {
        this.weiboId = weiboId;
        this.userId = userId;
        this.userName = userName;
        if (category == null)
            category = Category.Normal;
        this.category = category;
        if (content == null)
            content = "";
        this.content = content;
        if (url == null)
            url = "";
        this.url = url;
        this.time = time;
        this.tag = tag;
    }

    public Weibo(String userId, String userName, String weiboId,
                 Category category, String content, String url, Date time, String tag,
                 boolean isRepost, String preUser, String sourceUserId, String preWeiboId, String sourceWeiboId) {
        this(userId, userName, weiboId, category, content, url, time, tag);
        this.isRepost = isRepost;
        this.preUserName = preUser;
        this.preWeiboId = preWeiboId;
        this.sourceUserId = sourceUserId;
        this.sourceWeiboId = sourceWeiboId;
    }

    // 发布者id
    private String userId = "";
    // 发布者屏幕名
    private String userName = "";
    // 微博识别符，用于在页面中定位该微博，可以是一个id，也可以是对应的css selector
    private String weiboId = "";
    // 微博类型，正常微博，推荐微博，广告微博等
    private Category category = Category.Normal;
    // 微博内容
    private String content = "";
    // 微博地址
    private String url = "";
    // 发布时间
    private Date time = new Date();
    // 转发数
    private int repostNum = -1;
    // 评论数
    private int commentNum = -1;
    // 点赞数
    private int likeNum = -1;
    // 微博标签信息，用于存放分类等人工信息
    private String tag = "";
    // 是否转发
    private boolean isRepost = false;
    // 转发前继用户id
    private String preUserId = "";
    // 转发前继用户名
    private String preUserName = "";
    // 转发源用户id
    private String sourceUserId = "";
    // 转发源用户名
    private String sourceUserName = "";
    // 转发前继微博id
    private String preWeiboId = "";
    // 转发源微博id
    private String sourceWeiboId = "";
    // 源微博转发数
    private int sourceWeiboRepostNum = -1;
    // 源微博评论数
    private int sourceWeiboCommentNum = -1;
    // 源微博点赞数
    private int sourceWeiboLikeNum = -1;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getRepostNum() {
        return repostNum;
    }

    public void setRepostNum(int repostNum) {
        this.repostNum = repostNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isRepost() {
        return isRepost;
    }

    public void setRepost(boolean repost) {
        isRepost = repost;
    }

    public String getPreUserName() {
        return preUserName;
    }

    public void setPreUserName(String preUserName) {
        this.preUserName = preUserName;
    }

    public String getPreUserId() {
        return preUserId;
    }

    public void setPreUserId(String preUserId) {
        this.preUserId = preUserId;
    }

    public String getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(String sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public String getSourceUserName() {
        return sourceUserName;
    }

    public void setSourceUserName(String sourceUserName) {
        this.sourceUserName = sourceUserName;
    }

    public String getPreWeiboId() {
        return preWeiboId;
    }

    public void setPreWeiboId(String preWeiboId) {
        this.preWeiboId = preWeiboId;
    }

    public String getSourceWeiboId() {
        return sourceWeiboId;
    }

    public void setSourceWeiboId(String sourceWeiboId) {
        this.sourceWeiboId = sourceWeiboId;
    }

    public int getSourceWeiboRepostNum() {
        return sourceWeiboRepostNum;
    }

    public void setSourceWeiboRepostNum(int sourceWeiboRepostNum) {
        this.sourceWeiboRepostNum = sourceWeiboRepostNum;
    }

    public int getSourceWeiboCommentNum() {
        return sourceWeiboCommentNum;
    }

    public void setSourceWeiboCommentNum(int sourceWeiboCommentNum) {
        this.sourceWeiboCommentNum = sourceWeiboCommentNum;
    }

    public int getSourceWeiboLikeNum() {
        return sourceWeiboLikeNum;
    }

    public void setSourceWeiboLikeNum(int sourceWeiboLikeNum) {
        this.sourceWeiboLikeNum = sourceWeiboLikeNum;
    }

    public JsonObjectBuilder toJson() {
        return Json.createObjectBuilder()
                .add("weiboId", weiboId)
                .add("userId", userId)
                .add("userName", userName)
                .add("category", category.name())
                .add("content", content)
                .add("url", url)
                .add("time", time.getTime())
                .add("repostNum", repostNum)
                .add("commentNum", commentNum)
                .add("likeNum", likeNum)
                .add("tag", tag)
                .add("isRepost", isRepost)
                .add("preUserId", preUserId)
                .add("preUserName", preUserName)
                .add("sourceUserId", sourceUserId)
                .add("sourceUserName", sourceUserName)
                .add("preWeiboId", preWeiboId)
                .add("sourceWeiboId", sourceWeiboId)
                .add("sourceWeiboRepostNum", sourceWeiboRepostNum)
                .add("sourceWeiboCommentNum", sourceWeiboCommentNum)
                .add("sourceWeiboLikeNum", sourceWeiboLikeNum);
    }

    public int compareTo(Weibo o) {
        return this.weiboId.compareTo(o.getWeiboId());
    }

    @Override
    public String toString() {
        return toJson().build().toString();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Weibo) && this.weiboId.equals(((Weibo) obj).weiboId);
    }

    @Override
    public int hashCode() {
        return weiboId.hashCode();
    }
}
