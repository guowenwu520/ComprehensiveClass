package com.example.vaporwaveVideo.util;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUitl {
    static  long time=0;
    public static  String getTimeString(long timelong,String type){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(type);
        Date date=new Date(timelong);
       return simpleDateFormat.format(date);
    }
    public static  void start(){
          time=new Date().getTime();
    }
    public static  void end(){
        Log.e("耗时 ",(new Date().getTime()-time)/1000f+"s");
    }
}
