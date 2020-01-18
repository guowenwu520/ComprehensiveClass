package com.example.runapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.runapp.CollAndRel_Actvity;
import com.example.runapp.MainActivity;
import com.example.runapp.MainHome;
import com.example.runapp.R;
import com.example.runapp.Setting_Activity;
import com.example.runapp.util.Common_Uitl;
import com.example.runapp.util.Singleton;

import static com.example.runapp.Data.DataAccess.URL_IMG_ACCESS;
import static com.example.runapp.MainHome.ISSINMG;

/**
 * Created by 18179 on 2020/1/14.
 */

public class fragment_my extends Fragment {
    ImageView head;
    TextView sign,name,CollectionNumber,ReleaseNumber;
    RelativeLayout Collection,Release,Setting;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        head=view.findViewById(R.id.head);
        name=view.findViewById(R.id.name);
        sign=view.findViewById(R.id.sing);
        Collection=view.findViewById(R.id.Collection);
        Release=view.findViewById(R.id.Release);
        Setting=view.findViewById(R.id.Setting);
        CollectionNumber=view.findViewById(R.id.CollectionNumber);
        ReleaseNumber=view.findViewById(R.id.ReleaseNumber);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        MainHome.isShowTop(false);
        if(ISSINMG){
            sign.setText(getResources().getString(R.string.editsing));
            name.setText(Singleton.getInstance().getUser().getMyname());
            if(Singleton.getInstance().getUser().getUrlimg().equals("0")){
                Glide.with(getActivity()).load(R.drawable.defulthead).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(head);
                name.setText(getResources().getString(R.string.defaultname));
                CollectionNumber.setText("0");
                ReleaseNumber.setText("0");
            }else {
                Glide.with(getActivity()).load(URL_IMG_ACCESS+Singleton.getInstance().getUser().getUrlimg()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(head);
            }
        }else{
            sign.setText(getResources().getString(R.string.nusing));
            name.setText(getResources().getString(R.string.defaultname));
            CollectionNumber.setText("0");
            ReleaseNumber.setText("0");
            Glide.with(getActivity()).load(R.drawable.defulthead).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(head);
        }
        Collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ISSINMG){
                    Intent intent=new Intent(getContext(), CollAndRel_Actvity.class);
                    intent.putExtra("index",1);
                    startActivity(intent);
                }else{
                    Common_Uitl.showToast(getContext(),"请先登录");
                }
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        Release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ISSINMG){
                    Intent intent=new Intent(getContext(), CollAndRel_Actvity.class);
                    intent.putExtra("index",2);
                    startActivity(intent);
                }else{
                    Common_Uitl.showToast(getContext(),"请先登录");
                }
            }
        });
        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ISSINMG){
                    Intent intent=new Intent(getContext(), Setting_Activity.class);
                    startActivity(intent);
                }else{
                    Common_Uitl.showToast(getContext(),"请先登录");
                }
            }
        });
    }
}
