package com.example.vaporwaveVideo.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.entity.EditInfoEdtity;
import com.example.vaporwaveVideo.interfacecall.OnRecycleListerCall;
import com.example.vaporwaveVideo.ui.view.EditSizeView;

import java.util.ArrayList;

public class EditMenuSelectAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int MUSIC_MENU=0;
    public static final int PASTER_MENU=1;
    public static final int SIZE_MENU=2;
    public static final int FILTER_MENU=3;
    //布局方式
    private int mType;
    private Activity mContext;
    private int mLayoutId;
    private ArrayList<EditInfoEdtity> mDatas=new ArrayList<>();
    private OnRecycleListerCall mOnRecycleListerCall;

    public EditMenuSelectAdpater(int mType, Activity mContext, int mLayoutId, ArrayList<EditInfoEdtity> mDatas) {
        this.mType = mType;
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mContext.getLayoutInflater().inflate(mLayoutId,parent,false);
        return new EditMenuViewHolder(view,mOnRecycleListerCall);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
           EditInfoEdtity editInfoEdtity=mDatas.get(position);
           EditMenuViewHolder editMenuViewHolder= (EditMenuViewHolder) holder;
        switch (mType){
            case MUSIC_MENU:
                if(position==0) {
                    if(editInfoEdtity.getMusicData()==null){
                        editMenuViewHolder.mItemMusicBg.setBackgroundResource(R.drawable.icon_nomusic);
                    }else {
                        editMenuViewHolder.mItemMusicBg.setBackground(null);
                        editMenuViewHolder.mItemMusicBg.setText(editInfoEdtity.getMusicName());
                    }
                }else{
                    editMenuViewHolder.mItemMusicBg.setBackgroundResource(R.drawable.icon_upload);
                }
                break;
            case SIZE_MENU:
                editMenuViewHolder.mItemSizeText.setText(editInfoEdtity.getSizeWidth()+":"+editInfoEdtity.getSizeHeight());
                editMenuViewHolder.mItemSizeImg.setSizeRatio(editInfoEdtity.getSizeWidth()/(float)editInfoEdtity.getSizeHeight());
                editMenuViewHolder.mItemSizeImg.setSelect(editInfoEdtity.isSizeIsSelect());
                if(editInfoEdtity.isSizeIsSelect()){
                    editMenuViewHolder.mItemSizeText.setTextColor(mContext.getResources().getColor(R.color.white));
                }else {
                    editMenuViewHolder.mItemSizeText.setTextColor(mContext.getResources().getColor(R.color.grey600));
                }
                break;
            case PASTER_MENU:
                 editMenuViewHolder.mItemPasterImg.setImageResource(editInfoEdtity.getPasterId());
                break;
            case FILTER_MENU:
                editMenuViewHolder.mItemFilterText.setText(editInfoEdtity.getFilterName());
                Glide.with(mContext).load(editInfoEdtity.getFilterThumbnail()).into(editMenuViewHolder.mItemFilterImg);
                editMenuViewHolder.mItemFilterImg.setColorFilter(editInfoEdtity.getFilterbgType(), PorterDuff.Mode.LIGHTEN);
        }
    }

    public void setOnRecycleListerCall(OnRecycleListerCall mOnRecycleListerCall) {
        this.mOnRecycleListerCall = mOnRecycleListerCall;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setDataRefresh(ArrayList<EditInfoEdtity> mDatas) {
        this.mDatas=mDatas;
        notifyDataSetChanged();
    }

    class EditMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnRecycleListerCall onRecycleListerCall;

        TextView mItemMusicBg;

        TextView mItemSizeText;
        EditSizeView mItemSizeImg;

        ImageView mItemPasterImg;

        ImageView mItemFilterImg;
        TextView mItemFilterText;
        @Override
        public void onClick(View view) {
            if(onRecycleListerCall!=null){
                onRecycleListerCall.onClickItem(this,getAdapterPosition());
            }
        }

        public EditMenuViewHolder(@NonNull View itemView, OnRecycleListerCall mOnRecycleListerCall) {
            super(itemView);
            onRecycleListerCall=mOnRecycleListerCall;
            itemView.setOnClickListener(this);
            switch (mType){
                case MUSIC_MENU:
                    mItemMusicBg=itemView.findViewById(R.id.item_music_bg);
                    break;
                case SIZE_MENU:
                    mItemSizeText=itemView.findViewById(R.id.item_size_text);
                    mItemSizeImg=itemView.findViewById(R.id.item_size_img);
                    break;
                case PASTER_MENU:
                    mItemPasterImg=itemView.findViewById(R.id.item_paster_img);
                    break;
                case FILTER_MENU:
                    mItemFilterImg=itemView.findViewById(R.id.item_filter_img);
                    mItemFilterText=itemView.findViewById(R.id.item_filter_text);
                    break;
            }
        }
    }

}
