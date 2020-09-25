package com.example.vaporwaveVideo.util;

import android.widget.Toast;

import com.example.vaporwaveVideo.app.App;

public class ToastUtil {
    public static void ShowText(String str){
        Toast.makeText(App.getContext(),str,Toast.LENGTH_SHORT).show();
    }
}
