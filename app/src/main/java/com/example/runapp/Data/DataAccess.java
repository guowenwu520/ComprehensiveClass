package com.example.runapp.Data;

import android.os.AsyncTask;
import android.util.Log;

import com.example.runapp.entity.Collection;
import com.example.runapp.entity.Comment;
import com.example.runapp.entity.Imgs;
import com.example.runapp.entity.RunClass;
import com.example.runapp.entity.SportsDetail;
import com.example.runapp.entity.User;
import com.example.runapp.util.Common_Uitl;
import com.example.runapp.util.Singleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 18179 on 2020/1/12.
 */

public class DataAccess {
    public  static  final  String URL_ACCESS="http://192.168.43.162:8080/runapp_war/User/";
    public  static  final  String URL_IMG_ACCESS="http://192.168.43.162:8080/runapp_war/User/Imgs?name=";
    public  static  final  String URL_VEDIO_ACCESS="http://192.168.43.162:8080/runapp_war/User/playMp4?name=";
    private static String get_test(String urls) {
                Log.e("url",urls);
                try {
                         URL url = new URL(urls);
                         HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
                         conn.setRequestMethod("GET");
                         conn.setConnectTimeout(5000);
                         conn.setReadTimeout(5000);
                        if(conn.getResponseCode() == 200){
                                  InputStream is = conn.getInputStream();
                                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                                    String result = br.readLine();
                                  return result;
                           }
                       conn.disconnect();
                   } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                   }
           return null;
    }
    public  static  void insertUser(User user){
        String url=URL_ACCESS+"addUser?id="+user.getId()+"&name="+user.getMyname()+"&pass="+user.getPass()+"&phone="+user.getPhone()+"&nickname="+user.getNickname();
        get_test(url);
    }
    //如果返回1表示用户不存在，2表示密码错误
    public  static  int  isUser(String name,String pass){
        String url=URL_ACCESS+"isUser?name="+name+"&pass="+pass;
        String i=get_test(url);
        return  Integer.parseInt(i);
    }
    // 使用OkHttp上传文件
    public static void uploadFile(File file, String imgname, final String namev, String nicknamev, String phonev, final String passv, String id) {
        Map<String,String> map=new HashMap<>();
        map.put("name",namev);
        map.put("nickname",nicknamev);
        map.put("phone",phonev);
        map.put("pass",passv);
        map.put("id",id);
        OkHttpUtils.post()
                .addHeader("Content-Type","multipart/form-data")
                .addFile("file",imgname,file)
                .url(URL_ACCESS+"updateUser").params(map)
                .build()
                .execute(new com.zhy.http.okhttp.callback.Callback() {
            @Override
            public Object parseNetworkResponse(Response response) throws Exception {
                Log.e("rt", "parseNetworkResponse: "+response.body().string() );
               new Thread(){
                   @Override
                   public void run() {
                       super.run();
                       User user=DataAccess.getUser(namev,passv);
                       if(user!=null)
                           Singleton.getInstance().setUser(user);
                   }
               }.start();

                return null;
            }

            @Override
            public void onError(Call call, Exception e) {
                Log.e("er",e.toString());
            }

            @Override
            public void onResponse(Object response) {
                Log.e("er","34");
            }
        });

    }

    public static User getUser(String name, String pass) {
        String url=URL_ACCESS+"getUser?name="+name+"&pass="+pass;
        String i=get_test(url);
        Gson gson=new Gson();
        if(i.equals("0")){
            return null;
        }else {
            TypeToken<User> typeToken=new TypeToken<User>(){};
            return gson.fromJson(i, typeToken.getType());
        }
    }

    public static ArrayList<RunClass> getRunClass() {
        ArrayList<RunClass> datas=new ArrayList<>();
        String url=URL_ACCESS+"getRunClass";
        String i=get_test(url);
        Gson gson=new Gson();
        if(i==null){
            return datas;
        }else {
            TypeToken<ArrayList<RunClass>> typeToken=new TypeToken<ArrayList<RunClass>>(){};
            return gson.fromJson(i, typeToken.getType());
        }
    }
    public static ArrayList<SportsDetail> getSprtsDetail() {
        ArrayList<SportsDetail> datas=new ArrayList<>();
        String url=URL_ACCESS+"getSportsDetail";
        String i=get_test(url);
        Gson gson=new Gson();
        if(i==null){
            return datas;
        }else {
            TypeToken<ArrayList<SportsDetail>> typeToken=new TypeToken<ArrayList<SportsDetail>>(){};
            return gson.fromJson(i, typeToken.getType());
        }
    }

    public static void updata(ArrayList<String> datas, String timev, String titilev, String addressv, String xiangxv, String classid, String id, double latv, double lonv,String phone) {
        String ids=Common_Uitl.getTimeId();
        Map<String,String> map=new HashMap<>();
        map.put("time",timev);
        map.put("titile",titilev);
        map.put("address",addressv);
        map.put("xiang",xiangxv);
        map.put("classid",classid);
        map.put("userid",id);
        map.put("lat",latv+"");
        map.put("lon",lonv+"");
        map.put("id",ids);
        map.put("phone",phone);
        Map<String,String> map1=new HashMap<>();
        map1.put("id",ids);
                for(int i=0;i<datas.size()-1;i++){
                  File file=new File(datas.get(i));
                  OkHttpUtils.post().addHeader("Content-Type","multipart/form-data").addFile("file","img"+ Common_Uitl.getTimeId()+".png",file).params(map1).url(URL_ACCESS+"addVoide").build().execute(null);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        File file=new File(datas.get(datas.size()-1));
        OkHttpUtils.post().addHeader("Content-Type","multipart/form-data").addFile("file","voide"+ Common_Uitl.getTimeId()+".mp4",file).params(map).url(URL_ACCESS+"addVoide").build().execute(null);

        }

    public static ArrayList<Imgs> getAssecceIMg(String id) {
        String url=URL_ACCESS+"getImgss?id="+id;
        String i=get_test(url);
        Gson gson=new Gson();
        if(i.equals("0")){
            return null;
        }else {
            TypeToken<ArrayList<Imgs>> typeToken=new TypeToken<ArrayList<Imgs>>(){};
            ArrayList<Imgs> imgs=gson.fromJson(i, typeToken.getType());
            ArrayList<Imgs> imgs2=new ArrayList<>();
            for (int j=0;j<imgs.size();j++){
                if(imgs.get(j).getImgname().contains("img")){
                    imgs2.add(imgs.get(j));
                }
            }
            return imgs2;
        }
    }
    public static Imgs getAssecceVoide(String id) {
        String url=URL_ACCESS+"getImgss?id="+id;
        String i=get_test(url);
        Gson gson=new Gson();
        if(i.equals("0")){
            return null;
        }else {
            TypeToken<ArrayList<Imgs>> typeToken=new TypeToken<ArrayList<Imgs>>(){};
            ArrayList<Imgs> imgs=gson.fromJson(i, typeToken.getType());
           for (int j=0;j<imgs.size();j++){
                if(imgs.get(j).getImgname().contains("voide")){
                   return  imgs.get(j);
                }
            }
        }
        return null;
    }

    public static void insertComment(Comment comment) {
        Map<String,String> map=new HashMap<>();
        map.put("id",comment.getId());
        map.put("sprrtsid",comment.getSprrtsid());
        map.put("userid",comment.getUserid());
        map.put("imgurl",comment.getImgurl());
        map.put("name",comment.getNames());
        map.put("time",comment.getTime());
        map.put("msg",comment.getMsg());
        OkHttpUtils.post().addHeader("Content-Type","multipart/form-data").params(map).url(URL_ACCESS+"addCommon").build().execute(null);
    }

    public static ArrayList<Comment> getCommment(String id) {
        String url=URL_ACCESS+"getCommoent?sprrtsid="+id;
        String i=get_test(url);
        Gson gson=new Gson();
        if(i.equals("0")){
            return null;
        }else {
            TypeToken<ArrayList<Comment>> typeToken=new TypeToken<ArrayList<Comment>>(){};
            ArrayList<Comment> imgs=gson.fromJson(i, typeToken.getType());
            return imgs;
        }
    }

    public static ArrayList<SportsDetail> getSprtsDetailUser(String id) {
        ArrayList<SportsDetail> datas=new ArrayList<>();
        String url=URL_ACCESS+"getSportsDetailUser?id="+id;
        String i=get_test(url);
        Gson gson=new Gson();
        if(i==null){
            return datas;
        }else {
            TypeToken<ArrayList<SportsDetail>> typeToken=new TypeToken<ArrayList<SportsDetail>>(){};
            return gson.fromJson(i, typeToken.getType());
        }
    }

    public static void addCollection(Collection collection) {
        Map<String,String> map=new HashMap<>();
        map.put("id",collection.getId());
        map.put("sprrtsid",collection.getSportsid());
        map.put("userid",collection.getUserid());
        OkHttpUtils.post().addHeader("Content-Type","multipart/form-data").params(map).url(URL_ACCESS+"addCollection").build().execute(null);
    }

    public static void remoteCollection(Collection collection) {
        Map<String,String> map=new HashMap<>();
        map.put("id",collection.getId());
        map.put("sprrtsid",collection.getSportsid());
        map.put("userid",collection.getUserid());
        OkHttpUtils.post().addHeader("Content-Type","multipart/form-data").params(map).url(URL_ACCESS+"remoteCollection").build().execute(null);
    }

    public static boolean isCollent(Collection collection) {
        ArrayList<SportsDetail> datas=new ArrayList<>();
        String url=URL_ACCESS+"isCollent?id="+collection.getId()+"&sprrtsid="+collection.getSportsid()+"&userid="+collection.getUserid();
        String i=get_test(url);
        if(i.equals("0")){
            return false;
        }else {
          return true;
        }
    }

    public static ArrayList<SportsDetail> getCollteions(String id) {
        ArrayList<SportsDetail> datas=new ArrayList<>();
        String url=URL_ACCESS+"getCollteions?id="+id;
        String i=get_test(url);
        Gson gson=new Gson();
        if(i==null){
            return datas;
        }else {
            TypeToken<ArrayList<SportsDetail>> typeToken=new TypeToken<ArrayList<SportsDetail>>(){};
            return gson.fromJson(i, typeToken.getType());
        }
    }

    public static ArrayList<SportsDetail> getSprtsDetailLike(String str) {
        ArrayList<SportsDetail> datas=new ArrayList<>();
        String url=URL_ACCESS+"getSprtsDetailLike?str="+str;
        String i=get_test(url);
        Gson gson=new Gson();
        if(i==null){
            return datas;
        }else {
            TypeToken<ArrayList<SportsDetail>> typeToken=new TypeToken<ArrayList<SportsDetail>>(){};
            return gson.fromJson(i, typeToken.getType());
        }
    }
}
