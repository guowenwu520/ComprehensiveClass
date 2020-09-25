package com.example.vaporwaveVideo.entity;

public class EditInfoEdtity {
    /**
     * music
     */
    //id
    private int musicId;
    //名字
    private String musicName;
    //大小
    private long musicSize;
    //时长
    private long musicDuration;
    //路径
    private String musicData;

    /**
     * size
     * @return
     */
    //长
   private int sizeWidth;
   //宽
    private int sizeHeight;
    //是否选择中
    private boolean sizeIsSelect=false;
    /**
     * paster
     * @return
     */

    private int pasterId;

    /**
     * filter
     * @return
     */
    private int  filterType;
    private String filterName;
    private String filterThumbnail;
    private int filterbgType;

    public EditInfoEdtity() {
    }

    public EditInfoEdtity(int musicId, String musicName, long musicSize, long musicDuration, String musicData) {
        this.musicId = musicId;
        this.musicName = musicName;
        this.musicSize = musicSize;
        this.musicDuration = musicDuration;
        this.musicData = musicData;
    }

    public EditInfoEdtity(int sizeWidth, int sizeHeight) {
        this.sizeWidth = sizeWidth;
        this.sizeHeight = sizeHeight;
    }

    public EditInfoEdtity(int pasterId) {
        this.pasterId = pasterId;
    }

    public EditInfoEdtity(int filterType, String filterName, String filterThumbnail,int bgColor) {
        this.filterType = filterType;
        this.filterName = filterName;
        this.filterThumbnail = filterThumbnail;
        this.filterbgType=bgColor;
    }

    public int getPasterId() {
        return pasterId;
    }

    public String getFilterThumbnail() {
        return filterThumbnail;
    }

    public void setFilterThumbnail(String filterThumbnail) {
        this.filterThumbnail = filterThumbnail;
    }

    public void setPasterId(int pasterId) {
        this.pasterId = pasterId;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public long getMusicSize() {
        return musicSize;
    }

    public void setMusicSize(long musicSize) {
        this.musicSize = musicSize;
    }

    public boolean isSizeIsSelect() {
        return sizeIsSelect;
    }

    public void setSizeIsSelect(boolean sizeIsSelect) {
        this.sizeIsSelect = sizeIsSelect;
    }

    public long getMusicDuration() {
        return musicDuration;
    }

    public void setMusicDuration(long musicDuration) {
        this.musicDuration = musicDuration;
    }

    public String getMusicData() {
        return musicData;
    }

    public void setMusicData(String musicData) {
        this.musicData = musicData;
    }

    public int getSizeWidth() {
        return sizeWidth;
    }

    public void setSizeWidth(int sizeWidth) {
        this.sizeWidth = sizeWidth;
    }

    public int getSizeHeight() {
        return sizeHeight;
    }

    public void setSizeHeight(int sizeHeight) {
        this.sizeHeight = sizeHeight;
    }

    public int getFilterType() {
        return filterType;
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    public String getFilterName() {
        return filterName;
    }

    public int getFilterbgType() {
        return filterbgType;
    }

    public void setFilterbgType(int filterbgType) {
        this.filterbgType = filterbgType;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }
}
