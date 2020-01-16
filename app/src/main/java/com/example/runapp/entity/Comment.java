package com.example.runapp.entity;

/**
 * Created by 18179 on 2020/1/16.
 */

public class Comment {
    private String id;
    private String sprrtsid;
    private String userid;
    private String msg;
    private String time;
    private String name;
    private String imgurl;

    public Comment(String id, String sprrtsid, String userid, String msg, String time, String name, String imgurl) {
        this.id = id;
        this.sprrtsid = sprrtsid;
        this.userid = userid;
        this.msg = msg;
        this.time = time;
        this.name = name;
        this.imgurl = imgurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSprrtsid() {
        return sprrtsid;
    }

    public void setSprrtsid(String sprrtsid) {
        this.sprrtsid = sprrtsid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
