package com.example.vaporwaveVideo.entity;

import java.io.Serializable;

public class HomeModeEntity implements Serializable {
    private  int id;
    private int type;
    private boolean isSelect;

    public HomeModeEntity(int id, int type, boolean isSelect) {
        this.id = id;
        this.type = type;
        this.isSelect = isSelect;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
