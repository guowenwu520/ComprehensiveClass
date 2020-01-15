package com.example.runapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.runapp.fragment.fragment_home;
import com.example.runapp.fragment.fragment_my;
import com.example.runapp.fragment.fragment_search;

public class MainHome extends AppCompatActivity {
    RadioGroup radioGroup;
    static  TextView top;
    //登录状态
    public  static boolean ISSINMG=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        radioGroup=findViewById(R.id.mnue);
        top=findViewById(R.id.top);
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
    public static void  isShowTop(boolean bool){
        if(bool){
            top.setVisibility(View.VISIBLE);
        }else{
            top.setVisibility(View.GONE);
        }
    }
}
