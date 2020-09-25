package com.example.vaporwaveVideo.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaporwaveVideo.interfacecall.AdapterItemCallback;

import java.util.Arrays;
import java.util.List;


public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> {
    private AdapterItemCallback<T> itemCallback;
    protected int selectPos;
    protected List<T> mDatas;

    public void setOnItemClickListener(AdapterItemCallback<T> listener) {
        this.itemCallback = listener;
    }

    protected void onItemClick(int position, T value) {
        if (itemCallback != null) {
            itemCallback.onItemClick(position, value);
        }
    }

    public void setData(List<T> datas) {
        this.mDatas = datas;
    }

    public void setData(T datas[]) {
        setData(Arrays.asList(datas));
    }

    public void setSelectPos(int pos) {
        this.selectPos = pos;
    }

    public T getItemData(int index) {
        return mDatas.get(index);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        onBindViewHolder(holder, position, isSelected(position));
    }

    public void onBindViewHolder(@NonNull VH holder, int position, boolean isSelect) {

    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    protected boolean isSelected(int position) {
        return selectPos == position;
    }
}
