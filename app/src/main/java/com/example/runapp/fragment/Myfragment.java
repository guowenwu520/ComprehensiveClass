package com.example.runapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.runapp.R;

@SuppressLint("ValidFragment")
public  class Myfragment extends Fragment {
    private String imgurl;
    public Myfragment(String url) {
        imgurl=url;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.img_view2,container,false);
        ImageView imageView=view.findViewById(R.id.imgs);
        Glide.with(getActivity()).load(imgurl).into(imageView);
        return view;
    }
}