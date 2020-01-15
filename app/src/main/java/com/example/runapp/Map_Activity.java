package com.example.runapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Map_Activity extends AppCompatActivity {
   double lat,lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_);
        lat=getIntent().getDoubleExtra("lat",0.0);
        lon=getIntent().getDoubleExtra("lon",0.0);
    }
}
