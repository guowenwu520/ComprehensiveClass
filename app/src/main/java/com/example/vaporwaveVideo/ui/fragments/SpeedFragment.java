package com.example.vaporwaveVideo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.interfacecall.OnSpeedChangCall;
import com.example.vaporwaveVideo.ui.VideoEditActivity;
import com.example.vaporwaveVideo.ui.view.SpeedView;


public class SpeedFragment extends Fragment implements OnSpeedChangCall {
    SpeedView speedView;
    VideoEditActivity video_edit_activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_speed,container,false);
        initView(view);
        initData();
        initShow();
        return view;
    }

    private void initShow() {
        speedView.setOnSpeedChangCall(this);
        video_edit_activity= (VideoEditActivity) getActivity();
    }

    private void initData() {

    }

    private void initView(View view) {
        speedView=view.findViewById(R.id.speed_view);
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//         if(v.getId()==R.id.speed_view){
//             speedView.onTouch(v,event);
//         }
//        return false;
//    }

    @Override
    public void speedChang(float chang) {
        /**
         * true是否新加
         */
        video_edit_activity.videoAddChangSpeed(chang,true);
    }
}
