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
import com.example.vaporwaveVideo.util.TimeUitl;

import java.util.ArrayList;

public class ClipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final  static int ONCLICK_ADD =-3;
    public final static int ONCLICK_DELETE=999;

    private Activity context;
    private ArrayList<VideoInfoEntity> videoInfoEntities = new ArrayList<>();
    private OnRecycleListerCall onRecycleListerCall;

    public ClipAdapter(Activity context, ArrayList<VideoInfoEntity> videoInfoEntities) {
        this.context = context;
        this.videoInfoEntities = videoInfoEntities;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = context.getLayoutInflater().inflate(R.layout.item_clip_vedio_view, parent, false);
        return new VideoClipViewHolder(view, onRecycleListerCall);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VideoClipViewHolder videoClipViewHolder = (VideoClipViewHolder) holder;
        VideoInfoEntity videoInfoEntity = videoInfoEntities.get(position);
        if (videoInfoEntity.getData() == null) {
            videoClipViewHolder.mItemClipCloseBnt.setVisibility(View.GONE);
            videoClipViewHolder.mItemClipViewTime.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.icon_add).into(videoClipViewHolder.mItemClipViewBackground);
        } else {
            videoClipViewHolder.mItemClipCloseBnt.setVisibility(View.VISIBLE);
            videoClipViewHolder.mItemClipViewTime.setVisibility(View.VISIBLE);
            videoClipViewHolder.mItemClipViewTime.setText(TimeUitl.getTimeString(videoInfoEntity.getDuration(), "mm:ss"));
                Glide.with(context).load(videoInfoEntity.getData()).into(videoClipViewHolder.mItemClipViewBackground);
              videoClipViewHolder.mItemClipCloseBnt.setBackgroundResource(R.drawable.icon_delete);
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
        this.videoInfoEntities = mVideoInfoEntities;
        notifyDataSetChanged();
    }

    class VideoClipViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnRecycleListerCall onRecycleListerCall;
        ImageView mItemClipViewBackground;
        TextView mItemClipCloseBnt;
        TextView mItemClipViewTime;

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.item_select_view_selectbnt) {
                onRecycleListerCall.onClickItem(this, ONCLICK_DELETE+getAdapterPosition());
            } else if(view.getId()==R.id.item_select_view_background&&videoInfoEntities.get(getAdapterPosition()).getData()==null){
                onRecycleListerCall.onClickItem(this, ONCLICK_ADD);
            }else {
                onRecycleListerCall.onClickItem(this, getAdapterPosition());
            }
        }

        public VideoClipViewHolder(View view, OnRecycleListerCall onRecycleListerCall) {
            super(view);
            this.onRecycleListerCall = onRecycleListerCall;
            mItemClipViewBackground = view.findViewById(R.id.item_select_view_background);
            mItemClipViewTime = view.findViewById(R.id.item_select_view_time);
            mItemClipCloseBnt = view.findViewById(R.id.item_select_view_selectbnt);
            view.setOnClickListener(this);
            mItemClipCloseBnt.setOnClickListener(this);
            mItemClipViewBackground.setOnClickListener(this);
        }
    }
}