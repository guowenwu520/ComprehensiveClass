package com.example.runapp.entity;

/**
 * Created by 18179 on 2020/1/27.
 */

public class Collection {
    private  String id;
    private  String userid;
    private String sportsid;

    public Collection() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSportsid() {
        return sportsid;
    }

    public void setSportsid(String sportsid) {
        this.sportsid = sportsid;
    }
}
