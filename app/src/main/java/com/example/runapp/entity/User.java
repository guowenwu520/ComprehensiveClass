package com.example.runapp.entity;

/**
 * Created by 18179 on 2020/1/12.
 */

public class User {
     private String id;
     private String myname;
     private  String pass;
     private String nickname;
     private String phone;
     private  String urlimg;
     public User(String name, String pass, String nickname, String phone) {
          this.myname = name;
          this.pass = pass;
          this.nickname = nickname;
          this.phone = phone;
     }

     public String getId() {
          return id;
     }

     public void setId(String id) {
          this.id = id;
     }

     public User(String name, String pass, String nickname, String phone, String urlimg) {
          this.myname = name;
          this.pass = pass;
          this.nickname = nickname;
          this.phone = phone;
          this.urlimg = urlimg;
     }

     public String getUrlimg() {
          return urlimg;
     }

     public void setUrlimg(String urlimg) {
          this.urlimg = urlimg;
     }

     public String getMyname() {
          return myname;
     }

     public void setMyname(String name) {
          this.myname = name;
     }

     public String getPass() {
          return pass;
     }

     public void setPass(String pass) {
          this.pass = pass;
     }

     public String getNickname() {
          return nickname;
     }

     public void setNickname(String nickname) {
          this.nickname = nickname;
     }

     public String getPhone() {
          return phone;
     }

     public void setPhone(String phone) {
          this.phone = phone;
     }

     @Override
     public String toString() {
          return "User{" +
                  "id='" + id + '\'' +
                  ", myname='" + myname + '\'' +
                  ", pass='" + pass + '\'' +
                  ", nickname='" + nickname + '\'' +
                  ", phone='" + phone + '\'' +
                  ", urlimg='" + urlimg + '\'' +
                  '}';
     }
}
