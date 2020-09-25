package com.example.vaporwaveVideo.filter;

import android.opengl.GLES20;
import android.view.MotionEvent;
import android.widget.Scroller;


import com.example.vaporwaveVideo.util.EasyGlUtils;

import static com.example.vaporwaveVideo.app.App.getContext;


/**
 * Created by cj on 2017/7/20 0020.
 * 滑动切换滤镜的控制类
 */

public class SlideGpuFilterGroup {
    private MagicFilterType[] types = new MagicFilterType[]{
            MagicFilterType.NONE,
            MagicFilterType.WARM,
            MagicFilterType.ANTIQUE,
            MagicFilterType.INKWELL,
            MagicFilterType.BRANNAN,
            MagicFilterType.N1977,
            MagicFilterType.FREUD,
            MagicFilterType.HEFE,
            MagicFilterType.HUDSON,
            MagicFilterType.NASHVILLE,
            MagicFilterType.COOL
    };
    private GPUImageFilter curFilter;
    private GPUImageFilter leftFilter;
    private int width, height;
    private int[] fFrame = new int[1];
    private int[] fTexture = new int[1];
    private int curIndex = 0;
    private OnFilterChangeListener mListener;

    public SlideGpuFilterGroup() {
        initFilter();
    }

    private void initFilter() {
        curFilter = getFilter(getCurIndex());
        leftFilter = getFilter(getCurIndex());
    }

    private GPUImageFilter getFilter(int index) {
        GPUImageFilter filter = MagicFilterFactory.initFilters(types[index%types.length]);
        if (filter == null) {
            filter = new GPUImageFilter();
        }
        return filter;
    }

    public void init() {
        curFilter.init();
        leftFilter.init();
    }

    public void onSizeChanged(int width, int height) {
        this.width = width;
        this.height = height;
        GLES20.glGenFramebuffers(1, fFrame, 0);
        EasyGlUtils.genTexturesWithParameter(1, fTexture, 0, GLES20.GL_RGBA, width, height);
        onFilterSizeChanged(width, height);
    }

    private void onFilterSizeChanged(int width, int height) {
        curFilter.onInputSizeChanged(width, height);
        leftFilter.onInputSizeChanged(width, height);
        curFilter.onDisplaySizeChanged(width, height);
        leftFilter.onDisplaySizeChanged(width, height);
    }

    public int getOutputTexture() {
        return fTexture[0];
    }

    public void onDrawFrame(int textureId) {
        EasyGlUtils.bindFrameTexture(fFrame[0], fTexture[0]);
        curFilter.onDrawFrame(textureId);
        onDrawSlideLeft(textureId);
        EasyGlUtils.unBindFrameBuffer();
    }

    private void onDrawSlideLeft(int textureId) {

                if (needSwitch) {
                    reCreateRightFilter();
                    if (mListener != null) {
                        mListener.onFilterChange(types[curIndex]);
                    }
                }

    }




    private void reCreateRightFilter() {
        leftFilter = getFilter(getCurIndex());
        leftFilter.init();
        leftFilter.onDisplaySizeChanged(width, height);
        leftFilter.onInputSizeChanged(width, height);
        needSwitch = false;
        curFilter = leftFilter;
    }



    private int getCurIndex() {
        return curIndex;
    }

    boolean needSwitch;


    public void setOnFilterChangeListener(OnFilterChangeListener listener) {
        this.mListener = listener;
    }

    public void setNext(int  b) {
        needSwitch = true;
        curIndex=b;

    }

    public interface OnFilterChangeListener {
        void onFilterChange(MagicFilterType type);
    }
}
