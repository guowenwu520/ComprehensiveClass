package com.example.runapp.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 18179 on 2020/1/12.
 */

public class Common_Uitl {
     public  static  boolean  IsEmptyString(String str){
         if(str!=null&&!str.equals(""))return  false;
         return  true;
     }
    public  static  void   showToast(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_LONG).show();
    }

}
