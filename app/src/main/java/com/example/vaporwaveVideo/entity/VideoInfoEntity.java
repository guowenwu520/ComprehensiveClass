package com.example.vaporwaveVideo.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

public class VideoInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    //路径
    private String data;
    //大小
    private long size;
    //名字
    private String displayName;
    //标题
    private String title;
    //视频类型 video/mp4
    private String mimeType;
    //视频时长
    private long duration;
    //视频分辨率 X x Y格式
    private String resolution;

    // 视频缩略图宽度
    private long width;
    // 视频缩略图高度
    private long height;

    /**
     * 是否选择
     */
    private boolean isSelected=false;
    private int selectCount=0;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }

    @Override
    public String toString() {
        return "VideoInfoEntity{" +
                "data='" + data + '\'' +
                ", size=" + size +
                ", displayName='" + displayName + '\'' +
                ", title='" + title + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", duration=" + duration +
                ", resolution='" + resolution + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", isSelected=" + isSelected +
                ", selectCount=" + selectCount +
                '}';
    }
}
