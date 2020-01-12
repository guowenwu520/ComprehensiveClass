package com.example.runapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.runapp.util.Common_Uitl;

public class Register_Activity extends AppCompatActivity {
    EditText name,pass,Confirmpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=findViewById(R.id.name);
        pass=findViewById(R.id.pass);
        Confirmpass=findViewById(R.id.Confirmpass);
    }

    public void Back(View view) {
        finish();
    }

    public void Register(View view) {
        String namevalue=name.getText().toString().trim();
        String passvalue=pass.getText().toString().trim();
        String Confirmpassvalue=Confirmpass.getText().toString().trim();
        if(Common_Uitl.IsEmptyString(namevalue)||Common_Uitl.IsEmptyString(passvalue)||Common_Uitl.IsEmptyString(Confirmpassvalue)){
            Common_Uitl.showToast(this,"输入不能为空");
        }else if(!passvalue.equals(Confirmpassvalue)){
            Common_Uitl.showToast(this,"密码不一致");
        }else{
            new AsyncTask<Void,Void,Void>(){
                int fign=0;
                @Override
                protected Void doInBackground(Void... voids) {

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Common_Uitl.showToast(Register_Activity.this,"注册成功");
                    finish();
                }
            }.execute();
        }
    }
}
