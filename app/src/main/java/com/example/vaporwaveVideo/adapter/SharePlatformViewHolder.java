package com.example.vaporwaveVideo.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaporwaveVideo.R;


public class SharePlatformViewHolder extends RecyclerView.ViewHolder {
    private ImageView mIvContent;
    private TextView mTvMessage;

    public SharePlatformViewHolder(@NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share_platform, parent, false));
        mIvContent = itemView.findViewById(R.id.iv_content);
        mTvMessage = itemView.findViewById(R.id.tv_message);
    }

    public void bindData(Drawable drawable, String name) {
        mIvContent.setImageDrawable(drawable);
        mTvMessage.setText(name);
    }
}
