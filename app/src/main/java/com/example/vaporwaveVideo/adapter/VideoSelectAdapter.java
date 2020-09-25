package com.example.vaporwaveVideo.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.entity.VideoInfoEntity;
import com.example.vaporwaveVideo.interfacecall.OnRecycleListerCall;
import com.example.vaporwaveVideo.util.DensityUtil;
import com.example.vaporwaveVideo.util.TimeUitl;

import java.util.ArrayList;

public class VideoSelectAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

   private Activity context;
   private ArrayList<VideoInfoEntity> videoInfoEntities=new ArrayList<>();
   private OnRecycleListerCall onRecycleListerCall;

    public VideoSelectAdapter(Activity context, ArrayList<VideoInfoEntity> videoInfoEntities) {
        this.context = context;
        this.videoInfoEntities = videoInfoEntities;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=context.getLayoutInflater().inflate(R.layout.item_select_vedio_view,parent,false);
        return new VideoSelectViewHolder(view,onRecycleListerCall);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
         VideoSelectViewHolder videoSelectViewHolder= (VideoSelectViewHolder) holder;
         VideoInfoEntity videoInfoEntity=videoInfoEntities.get(position);
         if(position==0){
             videoSelectViewHolder.mItemSlectViewSelectBnt.setVisibility(View.GONE);
             videoSelectViewHolder.mItemSelectViewTime.setVisibility(View.GONE);
             videoSelectViewHolder.mItemSelectViewBackground.setImageResource(R.drawable.icon_camera);
             videoSelectViewHolder.mItemSelectViewBackground.setPadding(DensityUtil.dip2px(context,30),DensityUtil.dip2px(context,30),DensityUtil.dip2px(context,30),DensityUtil.dip2px(context,30));
         }else {
             videoSelectViewHolder.mItemSlectViewSelectBnt.setVisibility(View.VISIBLE);
             videoSelectViewHolder.mItemSelectViewTime.setVisibility(View.VISIBLE);
             videoSelectViewHolder.mItemSelectViewTime.setText(TimeUitl.getTimeString(videoInfoEntity.getDuration(),"mm:ss"));
                  Glide.with(context).load(videoInfoEntity.getData()).into(videoSelectViewHolder.mItemSelectViewBackground);
               if(videoInfoEntity.isSelected()){
                 videoSelectViewHolder.mItemSlectViewSelectBnt.setBackgroundResource(R.drawable.is_select);
                 videoSelectViewHolder.mItemSlectViewSelectBnt.setText(videoInfoEntity.getSelectCount()+"");
             }else {
                 videoSelectViewHolder.mItemSlectViewSelectBnt.setText("");
                 videoSelectViewHolder.mItemSlectViewSelectBnt.setBackgroundResource(R.drawable.no_select);
             }
         }
    }

    @Override
    public int getItemCount() {
        return videoInfoEntities.size();
    }

    public void setOnRecycleListerCall(OnRecycleListerCall onRecycleListerCall) {
        this.onRecycleListerCall = onRecycleListerCall;
    }

    public void setData(ArrayList<VideoInfoEntity> mVideoInfoEntities) {
        this.videoInfoEntities=mVideoInfoEntities;
        notifyDataSetChanged();
    }

    class  VideoSelectViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        OnRecycleListerCall onRecycleListerCall;
        ImageView mItemSelectViewBackground;
        TextView mItemSlectViewSelectBnt;
        TextView mItemSelectViewTime;

        @Override
        public void onClick(View view) {
            onRecycleListerCall.onClickItemAndData(this,getAdapterPosition(),videoInfoEntities);
        }

        public VideoSelectViewHolder(View view, OnRecycleListerCall onRecycleListerCall) {
            super(view);
            this.onRecycleListerCall=onRecycleListerCall;
            view.setOnClickListener(this);
            mItemSelectViewBackground=view.findViewById(R.id.item_select_view_background);
            mItemSelectViewTime=view.findViewById(R.id.item_select_view_time);
            mItemSlectViewSelectBnt=view.findViewById(R.id.item_select_view_selectbnt);
        }
    }
}
