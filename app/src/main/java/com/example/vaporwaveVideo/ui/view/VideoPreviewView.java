package com.example.vaporwaveVideo.ui.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Surface;


import androidx.annotation.RequiresApi;

import com.example.vaporwaveVideo.filter.MediaPlayerWrapper;
import com.example.vaporwaveVideo.filter.SlideGpuFilterGroup;
import com.example.vaporwaveVideo.filter.VideoDrawer;
import com.example.vaporwaveVideo.filter.VideoInfo;
import com.example.vaporwaveVideo.filter.WaterMarkFilter;

import java.io.IOException;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by cj on 2017/10/16.
 * desc: 播放视频的view 单个视频循环播放
 */

public class VideoPreviewView extends GLSurfaceView implements GLSurfaceView.Renderer, MediaPlayerWrapper.IMediaCallback {
    private MediaPlayerWrapper mMediaPlayer;
    private VideoDrawer mDrawer;

    /**视频播放状态的回调*/
    private MediaPlayerWrapper.IMediaCallback callback;


    public VideoPreviewView(Context context) {
        super(context,null);
    }

    public VideoPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        setPreserveEGLContextOnPause(false);
        setCameraDistance(100);
        mDrawer = new VideoDrawer(context,getResources());

        //初始化Drawer和VideoPlayer
        mMediaPlayer = new MediaPlayerWrapper();
        mMediaPlayer.setOnCompletionListener(this);
    }
    /**设置视频的播放地址*/
    public void setVideoPath(List<String> paths, boolean b){
         mMediaPlayer.release();
        mMediaPlayer.setDataSource(paths);
            try {
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public  void addLogo(WaterMarkFilter waterMarkFilter){
        mDrawer.addLogo(waterMarkFilter);
    }
    public void removwLogo(WaterMarkFilter waterMarkFilter){
        mDrawer.removwLogo(waterMarkFilter);
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mDrawer.onSurfaceCreated(gl,config);
        SurfaceTexture surfaceTexture = mDrawer.getSurfaceTexture();
        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                requestRender();
            }
        });
        Surface surface = new Surface(surfaceTexture);
        mMediaPlayer.setSurface(surface);
        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        callback.onCreateSufer();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mDrawer.onSurfaceChanged(gl,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mDrawer.onDrawFrame(gl);
    }
    public void onDestroy(){
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
    }
    public void setOnFilterChangeListener(SlideGpuFilterGroup.OnFilterChangeListener listener){
        mDrawer.setOnFilterChangeListener(listener);
    }
    @Override
    public void onVideoPrepare() {
        if (callback!= null){
            callback.onVideoPrepare();
        }
    }

    @Override
    public void onVideoStart() {
        if(callback!=null){
            callback.onVideoStart();
        }
    }

    @Override
    public void onVideoPause() {
        if (callback != null){
            callback.onVideoPause();
        }
    }

    @Override
    public void onCreateSufer() {
        if (callback != null){
            callback.onCreateSufer();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (callback != null){
            callback.onCompletion(mp);
        }
    }

    @Override
    public void onVideoChanged(final VideoInfo info) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                mDrawer.onVideoChanged(info);
            }
        });
        if(callback!=null){
            callback.onVideoChanged(info);
        }
    }
    /**
     * isPlaying now
     * */
    public boolean isPlaying(){
        return mMediaPlayer.isPlaying();
    }
    /**
     * pause play
     * */
    public void pause(){
        mMediaPlayer.pause();
    }
    /**
     * start play video
     * */
    public void start(){
        mMediaPlayer.start();
    }
    /**
     * 跳转到指定的时间点，只能跳到关键帧
     * */
    public void seekTo(int time){
        mMediaPlayer.seekTo(time);
    }
    /**
     * 获取当前视频的长度
     * */
    public int getVideoDuration(){
        return mMediaPlayer.getCurVideoDuration();
    }
    /**
     * 获取当前播放的视频的列表
     * */
    public List<VideoInfo> getVideoInfo(){
        return mMediaPlayer.getVideoInfo();
    }

    /**
     * 切换美颜状态
     * */
    public void switchBeauty(){
        mDrawer.switchBeauty();
    }


    public void setIMediaCallback(MediaPlayerWrapper.IMediaCallback callback){
        this.callback=callback;
    }

    public void setMedioSizeChang(MediaPlayerWrapper.MedioSizeChang medioSizeChang) {
       mMediaPlayer.setMedioSizeChang(medioSizeChang);
    }

    public void setNext(int b) {
        mDrawer.setNext(b);
    }

    public long getCurrentPosition() {
        return  mMediaPlayer.getCurPosition();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public PlaybackParams getPlaybackParams() {
        return mMediaPlayer.getPlaybackParams();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setPlaybackParams(PlaybackParams params) {
        mMediaPlayer.setPlaybackParams(params);
    }
}
