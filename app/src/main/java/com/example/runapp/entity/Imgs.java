package com.example.runapp.entity;

public class Imgs {
    String id;
    String imgname;
    String sprotsdetail_id;

    public Imgs(String id, String imgname, String sprotsdetail_id) {
        this.id = id;
        this.imgname = imgname;
        this.sprotsdetail_id = sprotsdetail_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgname() {
        return imgname;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
    }

    public String getSprotsdetail_id() {
        return sprotsdetail_id;
    }

    public void setSprotsdetail_id(String sprotsdetail_id) {
        this.sprotsdetail_id = sprotsdetail_id;
    }
}
