package com.example.runapp.Data;

import android.os.AsyncTask;
import android.util.Log;

import com.example.runapp.entity.User;
import com.example.runapp.util.Singleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
}
