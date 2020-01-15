package com.example.runapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.runapp.entity.User;
import com.example.runapp.util.Common_Uitl;
import com.example.runapp.util.Singleton;

public class Setting_Activity extends AppCompatActivity {
     ImageView imageView;
     EditText nam,nickname,pass,phone;
     String imgurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_);
        imageView=findViewById(R.id.imgchange);
        nam=findViewById(R.id.namechange);
        nickname=findViewById(R.id.nicknange);
        pass=findViewById(R.id.passchange);
        phone=findViewById(R.id.phonename);
        init();
    }

    private void init() {
        imgurl= Singleton.getInstance().getUser().getUrlimg();
        User user=Singleton.getInstance().getUser();
        if(imgurl!=null){
            Glide.with(Setting_Activity.this).load(Singleton.getInstance().getUser().getUrlimg()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
        }else{
            Glide.with(this).load(R.drawable.defulthead).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
        }
        if(!Common_Uitl.IsEmptyString(user.getName())){
            nam.setText(user.getName());
        }
        if(!Common_Uitl.IsEmptyString(user.getPass())){
            pass.setText(user.getPass());
        }
        if(!Common_Uitl.IsEmptyString(user.getPhone())){
            phone.setText(user.getPhone());
        }
        if(!Common_Uitl.IsEmptyString(user.getNickname())){
            nickname.setText(user.getNickname());
        }
        //拍照或者选择
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void Back(View view) {
        finish();
    }

    public void Save(View view) {
        String namev=nam.getText().toString().trim();
        String nicknamev=nickname.getText().toString().trim();
        String phonev=phone.getText().toString().trim();
        String passv=pass.getText().toString().trim();
        Singleton.getInstance().setUser(new User(namev,passv,nicknamev,phonev,imgurl));
        Common_Uitl.showToast(this,getResources().getString(R.string.saveed));
        finish();

    }
}
