package com.example.runapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.example.runapp.R;

/**
 * Created by 18179 on 2020/1/14.
 */

public class fragment_home extends Fragment {
    RelativeLayout relativeLayout;
    TextView postion;
    RecyclerView recyclerView;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.fragment_home,container,false);
         relativeLayout=view.findViewById(R.id.wenzhi);
         recyclerView=view.findViewById(R.id.recycle_runclass);
         postion=view.findViewById(R.id.position);
         initPostion();
        return view;
    }

    private void initPostion() {

//初始化定位
        mLocationClient = new AMapLocationClient(getActivity());


//异步获取定位结果
        final AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {

                if (amapLocation != null) {
                  //  Log.e("er",amapLocation.getErrorInfo());
                    if (amapLocation.getErrorCode() == 0) {
                        postion.setText(amapLocation.getAddress());
                    }
                }
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
//启动定位
        mLocationClient.startLocation();
    }
}
