package com.example.runapp.util;

import com.example.runapp.entity.SportsDetail;
import com.example.runapp.entity.User;

/**
 * Created by 18179 on 2020/1/15.
 */

public class Singleton {
    private static Singleton uniqueInstance = null;
    private User user=new User("hh","89900","sdsds","232343","http://img1.imgtn.bdimg.com/it/u=3675415932,4054970339&fm=26&gp=0.jpg");
    private SportsDetail sportsDetail;
    private Singleton(){
    }
    public static synchronized Singleton getInstance(){
        //判断存储实例的变量是否有值
        if(uniqueInstance == null){
            //如果没有，就创建一个类实例，并把值赋值给存储类实例的变量
            uniqueInstance = new Singleton();
        }
        //如果有值，那就直接使用
        return uniqueInstance;
    }

    public SportsDetail getSportsDetail() {
        return sportsDetail;
    }

    public void setSportsDetail(SportsDetail sportsDetail) {
        this.sportsDetail = sportsDetail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
