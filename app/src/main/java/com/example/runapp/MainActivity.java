package com.example.runapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.runapp.Data.DataAccess;
import com.example.runapp.entity.User;
import com.example.runapp.util.Common_Uitl;
import com.example.runapp.util.Singleton;

import static com.example.runapp.MainHome.ISSINMG;
public class MainActivity extends AppCompatActivity {
    EditText  name,pass;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=findViewById(R.id.name);
        pass=findViewById(R.id.pass);
        imageView=findViewById(R.id.headimg);
        init();
    }

    private void init() {
        Glide.with(this).load(R.drawable.head).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
    }

    //登录
    public void SignIn(View view) {
        final String namevalue=name.getText().toString().trim();
        final String passvalue=pass.getText().toString().trim();
         if(Common_Uitl.IsEmptyString(namevalue)||Common_Uitl.IsEmptyString(passvalue)){
            Common_Uitl.showToast(this,"输入不能为空");
        }else{
            new AsyncTask<Void,Void,Void>(){
                int fign=0;
                @Override
                protected Void doInBackground(Void... voids) {
                   fign= DataAccess.isUser(namevalue,passvalue);
                   if(fign==1) {
                       User user=DataAccess.getUser(namevalue,passvalue);
                       if(user!=null)
                           Singleton.getInstance().setUser(user);
                   }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    if(fign==1) {
                        Intent intent = new Intent(MainActivity.this, MainHome.class);
                        startActivity(intent);
                        Log.e("usr", "onPostExecute: "+Singleton.getInstance().getUser());
                        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("name",namevalue);
                        editor.putString("pass",passvalue);
                        editor.commit();
                        ISSINMG=true;
                        finish();
                    }else{
                        if(fign==2) {
                            Common_Uitl.showToast(MainActivity.this, "账号或者密码错误");
                        }else{
                            Common_Uitl.showToast(MainActivity.this, "请先注册");
                        }
                    }
                }
            }.execute();
        }
    }
    //注册
    public void Register(View view) {
        Intent intent=new Intent(this,Register_Activity.class);
        startActivity(intent);
    }


}
