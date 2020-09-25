package com.example.vaporwaveVideo.interfacecall;

import android.os.Message;

import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.util.ShowProgressUitl;
import com.example.vaporwaveVideo.util.ToastUtil;

public interface OnEpEditorCall {
    public void onSuccess();


    public void onFailure();

    public void onProgress(float progress);
}
