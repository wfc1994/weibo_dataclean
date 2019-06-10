package cn.edu.zjut.wfc;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class NodeResult {
    String sourceUserId;
    int count;
    int flag0;
    double f0;
    int flag1;
    double f1;
    int flag2;
    double f2;
    int flag3;
    double f3;
    int flag4;
    double f4;
    int flag5;
    double f5;

    public NodeResult() {
    }

    public NodeResult(String sourceUserId, int count, int flag0, double f0, int flag1, double f1, int flag2, double f2, int flag3, double f3, int flag4, double f4, int flag5, double f5) {
        this.sourceUserId = sourceUserId;
        this.count = count;
        this.flag0 = flag0;
        this.f0 = f0;
        this.flag1 = flag1;
        this.f1 = f1;
        this.flag2 = flag2;
        this.f2 = f2;
        this.flag3 = flag3;
        this.f3 = f3;
        this.flag4 = flag4;
        this.f4 = f4;
        this.flag5 = flag5;
        this.f5 = f5;
    }

    public String getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(String sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFlag0() {
        return flag0;
    }

    public void setFlag0(int flag0) {
        this.flag0 = flag0;
    }

    public double getF0() {
        return f0;
    }

    public void setF0(double f0) {
        this.f0 = f0;
    }

    public int getFlag1() {
        return flag1;
    }

    public void setFlag1(int flag1) {
        this.flag1 = flag1;
    }

    public double getF1() {
        return f1;
    }

    public void setF1(double f1) {
        this.f1 = f1;
    }

    public int getFlag2() {
        return flag2;
    }

    public void setFlag2(int flag2) {
        this.flag2 = flag2;
    }

    public double getF2() {
        return f2;
    }

    public void setF2(double f2) {
        this.f2 = f2;
    }

    public int getFlag3() {
        return flag3;
    }

    public void setFlag3(int flag3) {
        this.flag3 = flag3;
    }

    public double getF3() {
        return f3;
    }

    public void setF3(double f3) {
        this.f3 = f3;
    }

    public int getFlag4() {
        return flag4;
    }

    public void setFlag4(int flag4) {
        this.flag4 = flag4;
    }

    public double getF4() {
        return f4;
    }

    public void setF4(double f4) {
        this.f4 = f4;
    }

    public int getFlag5() {
        return flag5;
    }

    public void setFlag5(int flag5) {
        this.flag5 = flag5;
    }

    public double getF5() {
        return f5;
    }

    public void setF5(double f5) {
        this.f5 = f5;
    }

    public JsonObjectBuilder toJson() {
        return Json.createObjectBuilder()
                .add("sourceUserId", sourceUserId)
                .add("count", count)
                .add("flag0",flag0)
                .add("f0", f0)
                .add("flag1",flag1)
                .add("f1", f1)
                .add("flag2",flag2)
                .add("f2", f2)
                .add("flag03",flag3)
                .add("f3", f3)
                .add("flag4",flag4)
                .add("f4",f4)
                .add("flag5",flag5)
                .add("f5", f5);
    }

    @Override
    public String toString() {
        return toJson().build().toString();
    }

}
