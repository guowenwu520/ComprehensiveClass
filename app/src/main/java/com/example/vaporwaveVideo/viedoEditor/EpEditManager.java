package com.example.vaporwaveVideo.viedoEditor;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.vaporwaveVideo.entity.EditInfoEdtity;
import com.example.vaporwaveVideo.entity.VideoInfoEntity;
import com.example.vaporwaveVideo.interfacecall.OnEpEditorCall;

import java.util.ArrayList;

import VideoHandle.EpDraw;
import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;


public class EpEditManager {

    private int SUCCESS=0;
    private int FAILURE=1;

    public EpEditManager() {

    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            OnEpEditorCall onEpEditorCall= (OnEpEditorCall) msg.obj;
            if(msg.what==SUCCESS){
                if(onEpEditorCall!=null)onEpEditorCall.onSuccess();
            }else if(msg.what==FAILURE){
                if(onEpEditorCall!=null)onEpEditorCall.onFailure();
            }
        }
    };
    public void audioAndVideoSeparation(String videoPath, String outfilePath, final OnEpEditorCall onEpEditorCall){
        EpEditor.demuxer(videoPath, outfilePath, EpEditor.Format.MP4, new OnEditorListener() {
            @Override
            public void onSuccess() {
                Message message=new Message();
                message.what=SUCCESS;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure() {
                Message message=new Message();
                message.what=FAILURE;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
            }

            @Override
            public void onProgress(float progress) {
                if(onEpEditorCall!=null)onEpEditorCall.onProgress(progress);
            }
        });
    }

    public void andAudio(String videoPath, String audioPath,String outfilePath, final OnEpEditorCall onEpEditorCall){
        EpEditor.music(videoPath, audioPath, outfilePath, 1, 0.7f, new OnEditorListener() {
            @Override
            public void onSuccess() {
                Message message=new Message();
                message.what=SUCCESS;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure() {
                Message message=new Message();
                message.what=FAILURE;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
            }

            @Override
            public void onProgress(float progress) {
                if(onEpEditorCall!=null)onEpEditorCall.onProgress(progress);
            }
        });
    }

    public void andLogo(String videoPath, String logoPath,String outfilePath,int wdith,int height ,final OnEpEditorCall onEpEditorCall){
        EpVideo epVideo = new EpVideo(videoPath);
        //参数为图片路径,X,Y,图片的宽,高,是否是动图(仅支持png,jpg,gif图片,如果是gif图片,最后一个参数为true)
        EpDraw epDraw = new EpDraw(logoPath,0,0, wdith,height,false);
        epVideo.addDraw(epDraw);
        EpEditor.OutputOption outputOption = new EpEditor.OutputOption(outfilePath);
        outputOption.setHeight(height);
        outputOption.setWidth(wdith);
        EpEditor.exec(epVideo, outputOption, new OnEditorListener() {
            @Override
            public void onSuccess() {
                Message message=new Message();
                message.what=SUCCESS;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure() {
                Message message=new Message();
                message.what=FAILURE;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
                if(onEpEditorCall!=null)onEpEditorCall.onFailure();
            }

            @Override
            public void onProgress(float progress) {
                if(onEpEditorCall!=null)onEpEditorCall.onProgress(progress);
            }
        });
    }
    public void changSize(String videoPath,String outfilePath,int wdith,int height ,int x,int y,final OnEpEditorCall onEpEditorCall){
        EpVideo epVideo = new EpVideo(videoPath);
         epVideo.crop(wdith,height,x,y);
        EpEditor.OutputOption outputOption = new EpEditor.OutputOption(outfilePath);
        outputOption.setHeight(height);
        outputOption.setWidth(wdith);
        EpEditor.exec(epVideo, outputOption, new OnEditorListener() {
            @Override
            public void onSuccess() {
                Message message=new Message();
                message.what=SUCCESS;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure() {
                Message message=new Message();
                message.what=FAILURE;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
            }

            @Override
            public void onProgress(float progress) {
                if(onEpEditorCall!=null)onEpEditorCall.onProgress(progress);
            }
        });
    }

    public void mergeVedio(ArrayList<VideoInfoEntity> videoPaths, String outfilePath, final OnEpEditorCall onEpEditorCall){
        ArrayList<EpVideo> epVideos = new ArrayList<>();
        for (VideoInfoEntity videoInfoEntity:videoPaths){
            epVideos.add(new EpVideo(videoInfoEntity.getData()));
        }
        EpEditor.OutputOption outputOption = new EpEditor.OutputOption(outfilePath);
        EpEditor.merge(epVideos, outputOption,  new OnEditorListener() {
            @Override
            public void onSuccess() {
                Message message=new Message();
                message.what=SUCCESS;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure() {
                Message message=new Message();
                message.what=FAILURE;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
            }

            @Override
            public void onProgress(float progress) {
                if(onEpEditorCall!=null)onEpEditorCall.onProgress(progress);
            }
        });
    }


    public void mergeVedioByLc(Context context, ArrayList<String> videoPaths, String outfilePath, int videoWidth, int videoHeight, final OnEpEditorCall onEpEditorCall){
        ArrayList<EpVideo> epVideos = new ArrayList<>();
        for (String videoInfoEntity:videoPaths){
            epVideos.add(new EpVideo(videoInfoEntity));
        }
        EpEditor.OutputOption outputOption = new EpEditor.OutputOption(outfilePath);
        outputOption.setWidth(videoWidth);
        outputOption.setHeight(videoHeight);
        EpEditor.merge(epVideos, outputOption,  new OnEditorListener() {
            @Override
            public void onSuccess() {
                Message message=new Message();
                message.what=SUCCESS;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure() {
                Message message=new Message();
                message.what=FAILURE;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
            }

            @Override
            public void onProgress(float progress) {
                if(onEpEditorCall!=null)onEpEditorCall.onProgress(progress);
            }
        });
    }

    public void clipVedio(String videoPaths, String outfilePath,float startTime,float timeLength ,final OnEpEditorCall onEpEditorCall){
        EpVideo epVideo =new EpVideo(videoPaths);
         epVideo.clip(startTime,timeLength);
        EpEditor.OutputOption outputOption = new EpEditor.OutputOption(outfilePath);
        EpEditor.exec(epVideo, outputOption,  new OnEditorListener() {
            @Override
            public void onSuccess() {
                Message message=new Message();
                message.what=SUCCESS;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure() {
                Message message=new Message();
                message.what=FAILURE;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
            }

            @Override
            public void onProgress(float progress) {
                if(onEpEditorCall!=null)onEpEditorCall.onProgress(progress);
            }
        });
    }


    public void addFilter(String videoPaths, String outfilePath,String filter ,final OnEpEditorCall onEpEditorCall){
        EpVideo epVideo =new EpVideo(videoPaths);
        epVideo.addFilter(filter);
        EpEditor.OutputOption outputOption = new EpEditor.OutputOption(outfilePath);
        EpEditor.exec(epVideo, outputOption,  new OnEditorListener() {
            @Override
            public void onSuccess() {
                Message message=new Message();
                message.what=SUCCESS;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure() {
                Message message=new Message();
                message.what=FAILURE;
                message.obj=onEpEditorCall;
                handler.sendMessage(message);
            }

            @Override
            public void onProgress(float progress) {
                if(onEpEditorCall!=null)onEpEditorCall.onProgress(progress);
            }
        });
    }
}
