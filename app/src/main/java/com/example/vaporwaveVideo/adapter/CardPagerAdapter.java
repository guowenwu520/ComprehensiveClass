package com.example.vaporwaveVideo.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.entity.HomeModeEntity;
import com.example.vaporwaveVideo.interfacecall.OnHomeViewpagerCall;

import java.util.ArrayList;


public class CardPagerAdapter extends RecyclerView.Adapter {
    ArrayList<HomeModeEntity> datas=new ArrayList<>();
   Activity context;
   OnHomeViewpagerCall onHomeViewpagerCall;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=context.getLayoutInflater().inflate(R.layout.item_home_viewpager_view,parent,false);
        return new HomeHolder(view,onHomeViewpagerCall);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HomeHolder homeHolder= (HomeHolder) holder;
        homeHolder.item_bg_viewpager_img.setImageResource(datas.get(position%datas.size()).getId());
        if(datas.get(position%datas.size()).isSelect()){
            homeHolder.relativeLayout.setVisibility(View.VISIBLE);
        }else {
            homeHolder.relativeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public CardPagerAdapter(Activity context, ArrayList<HomeModeEntity> datas) {
        this.datas = datas;
        this.context=context;
    }

    public void setOnHomeViewpagerCall(OnHomeViewpagerCall onHomeViewpagerCall) {
        this.onHomeViewpagerCall = onHomeViewpagerCall;
    }

  class HomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
      ImageView item_bg_viewpager_img;
      RelativeLayout relativeLayout;
      OnHomeViewpagerCall onHomeViewpagerCall;

      @Override
      public void onClick(View v) {
          onHomeViewpagerCall.onSelectedTemapter(datas.get(getPosition()%datas.size()));
      }

      public HomeHolder(@NonNull View view, OnHomeViewpagerCall onHomeViewpagerCall) {
          super(view);
          this.onHomeViewpagerCall=onHomeViewpagerCall;
          view.setOnClickListener(this::onClick);
          item_bg_viewpager_img=view.findViewById(R.id.show_item_home_img);
          relativeLayout=view.findViewById(R.id.show_item_home);
      }

  }

}
