package com.example.runapp.entity;

/**
 * Created by 18179 on 2020/1/15.
 */

public class SportsDetail {
    private  String id;
    private  String classid;
    private String userid;
    private String address;
    private String tiem;
    private  String [] imgurls;
    private String  videourl;
    //简介
    private String briefIntroduction;
   private  String title;
   //详细信息
   private  String detailmesg;
   private  String phone;
  private  String lat,lon;
    public SportsDetail() {
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public SportsDetail(String id, String classid, String userid, String address, String tiem, String[] imgurls, String videourl, String briefIntroduction, String title, String detailmesg, String phone) {
        this.id = id;
        this.classid = classid;
        this.userid = userid;
        this.address = address;
        this.tiem = tiem;
        this.imgurls = imgurls;
        this.videourl = videourl;
        this.briefIntroduction = briefIntroduction;
        this.title = title;
        this.detailmesg = detailmesg;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTiem() {
        return tiem;
    }

    public void setTiem(String tiem) {
        this.tiem = tiem;
    }

    public String[] getImgurls() {
        return imgurls;
    }

    public void setImgurls(String[] imgurls) {
        this.imgurls = imgurls;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getBriefIntroduction() {
        return briefIntroduction;
    }

    public void setBriefIntroduction(String briefIntroduction) {
        this.briefIntroduction = briefIntroduction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailmesg() {
        return detailmesg;
    }

    public void setDetailmesg(String detailmesg) {
        this.detailmesg = detailmesg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
