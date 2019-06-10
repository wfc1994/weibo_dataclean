package cn.edu.zjut.wfc;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class RootCount {
    String sourceWeiboId;
    int count;

    public RootCount() {
    }

    public RootCount(String sourceWeiboId, int count) {
        this.sourceWeiboId = sourceWeiboId;
        this.count = count;
    }

    public String getSourceWeiboId() {
        return sourceWeiboId;
    }

    public void setSourceWeiboId(String sourceWeiboId) {
        this.sourceWeiboId = sourceWeiboId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public JsonObjectBuilder toJson() {
        return Json.createObjectBuilder()
                .add("sourceWeiboId", sourceWeiboId)
                .add("count", count);
    }



    @Override
    public String toString() {
        return toJson().build().toString();
    }
}
