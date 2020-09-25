package com.example.vaporwaveVideo.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.util.AppUtils;


public class SharePlatformAdapter extends BaseAdapter<SharePlatformViewHolder, AppUtils.ShareItem> {
    @NonNull
    @Override
    public SharePlatformViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SharePlatformViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SharePlatformViewHolder holder, int position) {
        AppUtils.ShareItem item = getItemData(position);
        holder.bindData(item.icon, item.name);
        holder.itemView.setOnClickListener(v -> onItemClick(position, item));
    }


}
