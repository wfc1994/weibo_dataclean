package cn.edu.zjut.wfc;

import cn.edu.zjut.myong.com.weibo.Weibo;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.Date;

public class WeiboCodec implements Codec<Weibo> {

    @Override
    public Weibo decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Weibo weibo = new Weibo();
        bsonReader.readStartDocument();
        bsonReader.readObjectId();
        while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = bsonReader.readName();
            switch (name) {
                case "userId": {
                    weibo.setUserId(bsonReader.readString());
                    break;
                }
                case "userName": {
                    weibo.setUserName(bsonReader.readString());
                    break;
                }
                case "weiboId": {
                    weibo.setWeiboId(bsonReader.readString());
                    break;
                }
                case "category": {
                    weibo.setCategory(Weibo.Category.valueOf(bsonReader.readString()));
                    break;
                }
                case "content": {
                    weibo.setContent(bsonReader.readString());
                    break;
                }
                case "url": {
                    weibo.setUrl(bsonReader.readString());
                    break;
                }
                case "time": {
                    weibo.setTime(new Date(bsonReader.readDateTime()));
                    break;
                }
                case "repostNum": {
                    weibo.setRepostNum(bsonReader.readInt32());
                    break;
                }
                case "commentNum": {
                    weibo.setCommentNum(bsonReader.readInt32());
                    break;
                }
                case "likeNum": {
                    weibo.setLikeNum(bsonReader.readInt32());
                    break;
                }
                case "tag": {
                    weibo.setTag(bsonReader.readString());
                    break;
                }
                case "isRepost": {
                    weibo.setRepost(bsonReader.readBoolean());
                    break;
                }
                case "preUserId": {
                    weibo.setPreUserId(bsonReader.readString());
                    break;
                }
                case "preUserName": {
                    weibo.setPreUserName(bsonReader.readString());
                    break;
                }
                case "sourceUserId": {
                    weibo.setSourceUserId(bsonReader.readString());
                    break;
                }
                case "sourceUserName": {
                    weibo.setSourceUserName(bsonReader.readString());
                    break;
                }
                case "preWeiboId": {
                    weibo.setPreWeiboId(bsonReader.readString());
                    break;
                }
                case "sourceWeiboId": {
                    weibo.setSourceWeiboId(bsonReader.readString());
                    break;
                }
                case "sourceWeiboRepostNum": {
                    weibo.setSourceWeiboRepostNum(bsonReader.readInt32());
                    break;
                }
                case "sourceWeiboCommentNum": {
                    weibo.setSourceWeiboCommentNum(bsonReader.readInt32());
                    break;
                }
                case "sourceWeiboLikeNum": {
                    weibo.setSourceWeiboLikeNum(bsonReader.readInt32());
                    break;
                }
            }
        }
        bsonReader.readEndDocument();
        return weibo;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Weibo weibo, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        {
            bsonWriter.writeString("userId", weibo.getUserId());
            bsonWriter.writeString("userName", weibo.getUserName());
            bsonWriter.writeString("weiboId", weibo.getWeiboId());
            bsonWriter.writeString("category", weibo.getCategory().name());
            bsonWriter.writeString("content", weibo.getContent());
            bsonWriter.writeString("url", weibo.getUrl());
            bsonWriter.writeDateTime("time", weibo.getTime().getTime());
            bsonWriter.writeInt32("repostNum", weibo.getRepostNum());
            bsonWriter.writeInt32("commentNum", weibo.getCommentNum());
            bsonWriter.writeInt32("likeNum", weibo.getLikeNum());
            bsonWriter.writeString("tag", weibo.getTag());
            bsonWriter.writeBoolean("isRepost", weibo.isRepost());
            bsonWriter.writeString("preUserId", weibo.getPreUserId());
            bsonWriter.writeString("preUserName", weibo.getPreUserName());
            bsonWriter.writeString("sourceUserId", weibo.getSourceUserId());
            bsonWriter.writeString("sourceUserName", weibo.getSourceUserName());
            bsonWriter.writeString("preWeiboId", weibo.getPreWeiboId());
            bsonWriter.writeString("sourceWeiboId", weibo.getSourceWeiboId());
            bsonWriter.writeInt32("sourceWeiboRepostNum", weibo.getSourceWeiboRepostNum());
            bsonWriter.writeInt32("sourceWeiboCommentNum",weibo.getSourceWeiboCommentNum() );
            bsonWriter.writeInt32("sourceWeiboLikeNum", weibo.getSourceWeiboLikeNum());
        }
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<Weibo> getEncoderClass() {
        return Weibo.class;
    }
}
