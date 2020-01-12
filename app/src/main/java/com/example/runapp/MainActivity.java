package com.example.runapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.runapp.util.Common_Uitl;

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
        String namevalue=name.getText().toString().trim();
        String passvalue=pass.getText().toString().trim();
        if(Common_Uitl.IsEmptyString(namevalue)||Common_Uitl.IsEmptyString(passvalue)){
            Common_Uitl.showToast(this,"输入不能为空");
        }else{
            new AsyncTask<Void,Void,Void>(){
                int fign=0;
                @Override
                protected Void doInBackground(Void... voids) {

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                     Intent intent=new Intent(MainActivity.this,MainHome.class);
                    startActivity(intent);
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
