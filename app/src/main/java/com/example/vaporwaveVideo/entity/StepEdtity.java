package com.example.vaporwaveVideo.entity;

public class StepEdtity {
    //当前步视频保存位置
    String path;
    //当前步操作类型
    int  type;
    //当前步播放倍速
    float speed;

    //滤镜
    int filkterType;


    public int getFilkterType() {
        return filkterType;
    }

    public void setFilkterType(int filkterType) {
        this.filkterType = filkterType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "StepEdtity{" +
                "path='" + path + '\'' +
                ", type=" + type +
                ", speed=" + speed +
                '}';
    }
}
