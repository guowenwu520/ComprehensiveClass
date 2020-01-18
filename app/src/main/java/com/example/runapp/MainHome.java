package com.example.runapp;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.runapp.Data.DataAccess;
import com.example.runapp.entity.User;
import com.example.runapp.fragment.fragment_home;
import com.example.runapp.fragment.fragment_my;
import com.example.runapp.fragment.fragment_search;
import com.example.runapp.util.Singleton;

import static com.example.runapp.Add_Run_Dialog_Activity.REQUEST_PERMISSIONS;

public class MainHome extends AppCompatActivity {
    RadioGroup radioGroup;
    static  TextView top;
    //登录状态
    public  static boolean ISSINMG=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        radioGroup=findViewById(R.id.mnue);
        top=findViewById(R.id.top);
        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        final String name=sharedPreferences.getString("name","0");
        final String pass=sharedPreferences.getString("pass","0");
        //获取数据
        if(!name.equals("0")){
            ISSINMG=true;
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    User user=DataAccess.getUser(name,pass);
                    if(user!=null)
                    Singleton.getInstance().setUser(user);
                    else ISSINMG=false;
                }
            }.start();
        }
        FragmentManager manager= getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.mainFramet,new fragment_home()).commit();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager manager= getSupportFragmentManager();
                     switch (checkedId){
                         case R.id.home:
                             top.setText(getResources().getString(R.string.home));
                             manager.beginTransaction().replace(R.id.mainFramet,new fragment_home()).commit();
                             break;
                         case R.id.search:
                             top.setText(getResources().getString(R.string.search));
                             manager.beginTransaction().replace(R.id.mainFramet,new fragment_search()).commit();
                             break;
                         case R.id.my:
                             manager.beginTransaction().replace(R.id.mainFramet,new fragment_my()).commit();
                             break;
                     }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initPermission();
    }

    public static void  isShowTop(boolean bool){
        if(bool){
            top.setVisibility(View.VISIBLE);
        }else{
            top.setVisibility(View.GONE);
        }
    }
    /**
     * 初始化相机相关权限
     * 适配6.0+手机的运行时权限
     */
    private void initPermission() {
        String[] permissions = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        //检查权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 之前拒绝了权限，但没有点击 不再询问 这个时候让它继续请求权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this, "用户曾拒绝打开相机权限", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
            } else {
                //注册相机权限
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
            }
        }
    }
}
