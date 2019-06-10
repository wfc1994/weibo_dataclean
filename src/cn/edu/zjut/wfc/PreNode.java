package cn.edu.zjut.wfc;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.Date;

public class PreNode {
    String WeiboId;
    String preWeiboId;
    String sourceWeiboId;
    boolean isRepost;
    int flag;
    String similarity;
    String userId;
    String username;
    String category;
    String url;
    Date time;
    int repostNum;
    int commentNum;
    int likeNum;
    String tag;
    String preUserId;
    String preUserName;
    String sourceUserId;
    String sourceUserName;
    int sourceWeiboRepostNum;
    int sourceWeiboCommentNum;
    int sourceWeiboLikeNum;
    String content;

    public PreNode() {
    }

    public PreNode(String weiboId, String preWeiboId, String sourceWeiboId, boolean isRepost, int flag, String similarity, String userId, String username, String category, String url, Date time, int repostNum, int commentNum, int likeNum, String tag, String preUserId, String preUserName, String sourceUserId, String sourceUserName, int sourceWeiboRepostNum, int sourceWeiboCommentNum, int sourceWeiboLikeNum, String content) {
        WeiboId = weiboId;
        this.preWeiboId = preWeiboId;
        this.sourceWeiboId = sourceWeiboId;
        this.isRepost = isRepost;
        this.flag = flag;
        this.similarity = similarity;
        this.userId = userId;
        this.username = username;
        this.category = category;
        this.url = url;
        this.time = time;
        this.repostNum = repostNum;
        this.commentNum = commentNum;
        this.likeNum = likeNum;
        this.tag = tag;
        this.preUserId = preUserId;
        this.preUserName = preUserName;
        this.sourceUserId = sourceUserId;
        this.sourceUserName = sourceUserName;
        this.sourceWeiboRepostNum = sourceWeiboRepostNum;
        this.sourceWeiboCommentNum = sourceWeiboCommentNum;
        this.sourceWeiboLikeNum = sourceWeiboLikeNum;
        this.content = content;
    }

    public String getWeiboId() {
        return WeiboId;
    }

    public void setWeiboId(String weiboId) {
        WeiboId = weiboId;
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRepost() {
        return isRepost;
    }

    public void setRepost(boolean repost) {
        isRepost = repost;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getPreUserId() {
        return preUserId;
    }

    public void setPreUserId(String preUserId) {
        this.preUserId = preUserId;
    }

    public String getPreUserName() {
        return preUserName;
    }

    public void setPreUserName(String preUserName) {
        this.preUserName = preUserName;
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
                .add("thisWeiboId", WeiboId)
                .add("preWeiboId", preWeiboId)
                .add("sourceWeiboId",sourceWeiboId)
                .add("isRepost", isRepost)
                .add("flag", flag)
                .add("similarity", similarity)
                .add("userId",userId)
                .add("username",username)
                .add("category",category)
                .add("url",url)
                .add("time",time.getTime())
                .add("repostNum", repostNum)
                .add("commentNum", commentNum)
                .add("likeNum", likeNum)
                .add("tag", tag)
                .add("preUserId", preUserId)
                .add("preUserName", preUserName)
                .add("sourceUserId", sourceUserId)
                .add("sourceUserName", sourceUserName)
                .add("sourceWeiboRepostNum", sourceWeiboRepostNum)
                .add("sourceWeiboCommentNum", sourceWeiboCommentNum)
                .add("sourceWeiboLikeNum", sourceWeiboLikeNum)
                .add("content",content);
    }


    @Override
    public String toString() {
        return toJson().build().toString();
    }
}
