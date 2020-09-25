package com.example.vaporwaveVideo.util;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.app.App;

public class ShowProgressUitl {
    private static AlertDialog mProgressDialog;
    private static ProgressBar progressBar,progressBar2;
    private static TextView textShow;
    private static Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            progressBar.setProgress(msg.arg1);
            textShow.setText(msg.arg1 + "%");
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static  void showProgress(Context context,String tip,boolean  isHorizontal) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_load, null);
        progressBar=view.findViewById(R.id.progressbar);
        progressBar2=view.findViewById(R.id.progressbar2);
        textShow=view.findViewById(R.id.text_show_progress);
        if(isHorizontal){
            progressBar2.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            textShow.setVisibility(View.VISIBLE);
        }else {
            progressBar2.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            textShow.setVisibility(View.INVISIBLE);
        }
      TextView  textTip=view.findViewById(R.id.text_tip);
      textTip.setText(tip);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
       builder.setView(view);
        builder.setCancelable(false);
        mProgressDialog=builder.create();
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mProgressDialog.show();
    }

    public static void UpdateProgress(int p){
        if (mProgressDialog != null) {
            Message message=new Message();
            message.what=0;
            message.arg1=p;
            handler.sendMessage(message);
        }
    }

    public static void cancelProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
    }
}
