package com.example.vaporwaveVideo.interfacecall;

import android.view.View;

import com.example.vaporwaveVideo.entity.VideoInfoEntity;

import java.util.ArrayList;

public interface OnRecycleListerCall {
    public void onClickItemAndData(View.OnClickListener onClickListener, int postion, ArrayList<VideoInfoEntity> datas);
    public void onClickItem(View.OnClickListener onClickListener,int postion);
}
