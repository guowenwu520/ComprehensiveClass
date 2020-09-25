package com.example.vaporwaveVideo.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.adapter.ClipAdapter;
import com.example.vaporwaveVideo.data.VideoData;
import com.example.vaporwaveVideo.entity.StepEdtity;
import com.example.vaporwaveVideo.entity.VideoInfoEntity;
import com.example.vaporwaveVideo.interfacecall.OnDataCompleteCall;
import com.example.vaporwaveVideo.interfacecall.OnEpEditorCall;
import com.example.vaporwaveVideo.interfacecall.OnRecycleListerCall;
import com.example.vaporwaveVideo.interfacecall.OnSlideTimeCall;
import com.example.vaporwaveVideo.ui.view.StickImgView;
import com.example.vaporwaveVideo.ui.view.TailoringSizeView;
import com.example.vaporwaveVideo.ui.view.VideoSlideShowView;
import com.example.vaporwaveVideo.util.PathUitl;
import com.example.vaporwaveVideo.util.ShowProgressUitl;
import com.example.vaporwaveVideo.util.TimeUitl;
import com.example.vaporwaveVideo.util.ToastUtil;
import com.example.vaporwaveVideo.util.WindowsUtil;
import com.example.vaporwaveVideo.viedoEditor.EpEditManager;

import java.util.ArrayList;

import static com.example.vaporwaveVideo.adapter.ClipAdapter.ONCLICK_ADD;
import static com.example.vaporwaveVideo.adapter.ClipAdapter.ONCLICK_DELETE;
import static com.example.vaporwaveVideo.ui.MainActivity.MBOTTOM_CLIP;
import static com.example.vaporwaveVideo.ui.SelectVideoActivity.RESELECT_CLIP;
import static com.example.vaporwaveVideo.util.PathUitl.MUSIC_VEDIO;

public class VideoClipActivity extends AppCompatActivity implements
        View.OnClickListener,
        MediaPlayer.OnCompletionListener,
        SurfaceHolder.Callback,
        OnRecycleListerCall {


    private static final int STOP_TIME_SHOW = 1;
    private static final int START_TIME_SHOW = 2;
    private static int TIME_STATUS = 3;
    private static final int FRIST_BITMAP = 4;
    private static final  int START_WENZHI=5;

    ArrayList<VideoInfoEntity> mVideoDatas = new ArrayList<>();
    private TextView mEditShowNowTime;
    private ImageView mEditShowPlayStatus;
    private ImageView mEditShowBackStep;
    private ImageView mEditShowNextStep;
    private VideoSlideShowView mEditShowVideoSlideView;
    private ImageView mEditShowCropButton;
    private StickImgView mStickImgView;
    private TailoringSizeView mTailoringSizeView;
    private EpEditManager mEpEditManager;

    private TextView mEditSaveBtn;
    private ImageView mImgBack;
    private SurfaceView mEditShowEffectView;
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder mSurfaceHolder;
    private  MediaMetadataRetriever media;
    private  RecyclerView mResultsDisplayRecycleview;
    private ClipAdapter mClipAdpater;

    private boolean mIsPlay = false;
    private String alltime;
     //展示图片张数
    private int count=5;
    private int mSelectCount;
    //剪切视频开始结束时间
    private long startTime,endTime;
    //临时处理的URL
    public String mVdeioHandleUrl;
    //存操作步骤
    private ArrayList<StepEdtity> steps = new ArrayList<>();
    //裁剪的视频
    private ArrayList<VideoInfoEntity> mClipVideoInfoEntities =new ArrayList<>();
    //当前下标
    private int nowIndex = 0;

    private Handler mHander = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == STOP_TIME_SHOW && TIME_STATUS == STOP_TIME_SHOW) {
                TIME_STATUS = STOP_TIME_SHOW;
            } else if (msg.what == START_TIME_SHOW && TIME_STATUS == START_TIME_SHOW) {
                if (mMediaPlayer == null) return;
                String nowtime = TimeUitl.getTimeString(mMediaPlayer.getCurrentPosition(), "mm:ss");
                mEditShowNowTime.setText(nowtime + "/" + alltime);
                mHander.sendEmptyMessageDelayed(START_TIME_SHOW, 1000);
                mEditShowEffectView.setBackground(null);
            }else if (msg.what == FRIST_BITMAP) {
                ArrayList<Bitmap> bitmaps= (ArrayList<Bitmap>) msg.obj;
                mEditShowEffectView.setBackground(new BitmapDrawable(bitmaps.get(0)));
                ShowProgressUitl.cancelProgress();
                if(mEditShowVideoSlideView!=null){
                    mEditShowVideoSlideView.setBitmaps(bitmaps);
                    mEditShowVideoSlideView.setAllTime(mMediaPlayer.getDuration());
                }
            }else if(msg.what==START_WENZHI){
                //初始化一个数据
                mVdeioHandleUrl = (String) msg.obj;
                ShowProgressUitl.cancelProgress();
                StepEdtity stepEdtity = new StepEdtity();
                stepEdtity.setPath(mVdeioHandleUrl);
                steps.add(stepEdtity);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowsUtil.setTransparentStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_clip);
        initView();
        initData();
        initShow();
    }
    @Override
    protected void onStop() {
        super.onStop();
        stopPlay();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startPlay();
    }

    private void initShow() {
        if (mVideoDatas.size() < 0) return;

        mMediaPlayer = new MediaPlayer();
        mSurfaceHolder = mEditShowEffectView.getHolder();
        mSurfaceHolder.addCallback(this);

        mEditShowBackStep.setEnabled(false);
        mEditShowNextStep.setEnabled(false);

        mImgBack.setOnClickListener(this);
        mEditSaveBtn.setOnClickListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mEditShowPlayStatus.setOnClickListener(this);
        mEditShowNextStep.setOnClickListener(this);
        mEditShowBackStep.setOnClickListener(this);
        mEditShowCropButton.setOnClickListener(this);
        mClipAdpater.setOnRecycleListerCall(this);
        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                changeVideoSize(width,height);
            }
        });
        mEditShowVideoSlideView.setOnSlideTimeCall(new OnSlideTimeCall() {
            @Override
            public void startTime(long start) {
                Bitmap bitmap = media.getFrameAtTime((long) (start*1000),MediaMetadataRetriever.OPTION_CLOSEST);
                mEditShowEffectView.setBackground(new BitmapDrawable(bitmap));
                startTime=start;
            }

            @Override
            public void endTime(long end) {
                Bitmap bitmap = media.getFrameAtTime((long) (end*1000),MediaMetadataRetriever.OPTION_CLOSEST);
                mEditShowEffectView.setBackground(new BitmapDrawable(bitmap));
                endTime=end;
            }
        });
        mEditShowVideoSlideView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mEditShowVideoSlideView.onTouch(event);
                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initData() {
        mVideoDatas = (ArrayList<VideoInfoEntity>) getIntent().getSerializableExtra("selectvideos");

        if(mVideoDatas.size()<5)
            mClipVideoInfoEntities.add(new VideoInfoEntity());


        mEpEditManager = new EpEditManager();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mResultsDisplayRecycleview.setLayoutManager(linearLayoutManager);
        mClipAdpater=new ClipAdapter(this, mClipVideoInfoEntities);
        mResultsDisplayRecycleview.setAdapter(mClipAdpater);

        final String outPath= PathUitl.createVdeioUrl(MUSIC_VEDIO);
        ShowProgressUitl.showProgress(this,getResources().getString(R.string.action_tip),true);
        if(mVideoDatas.size()>=2){
            mEpEditManager.mergeVedio(mVideoDatas, outPath, new OnEpEditorCall() {
                @Override
                public void onSuccess() {
                    //初始化一个数据
                    mVdeioHandleUrl =outPath;
                    ShowProgressUitl.cancelProgress();
                    StepEdtity stepEdtity = new StepEdtity();
                    stepEdtity.setPath(mVdeioHandleUrl);
                    steps.add(stepEdtity);
                    playInit();
                }

                @Override
                public void onFailure() {
                    ToastUtil.ShowText(getResources().getString(R.string.fail_tip));
                    ShowProgressUitl.cancelProgress();
                }

                @Override
                public void onProgress(float progress) {
                    ShowProgressUitl.UpdateProgress((int) (progress*100));
                }
            });
        }else {
            ShowProgressUitl.cancelProgress();
            mVdeioHandleUrl =mVideoDatas.get(0).getData();
            ShowProgressUitl.cancelProgress();
            StepEdtity stepEdtity = new StepEdtity();
            stepEdtity.setPath(mVdeioHandleUrl);
            steps.add(stepEdtity);
        }

    }

    private void initView() {
        mEditSaveBtn = findViewById(R.id.edit_save_btn);
        mEditShowBackStep = findViewById(R.id.edit_show_back_step);
        mEditShowNextStep = findViewById(R.id.edit_show_next_step);
        mEditShowNowTime = findViewById(R.id.edit_show_now_time);
        mEditShowPlayStatus = findViewById(R.id.edit_show_play_status);
        mImgBack = findViewById(R.id.img_back);
        mEditShowEffectView = findViewById(R.id.edit_show_effect_view);
        mStickImgView = findViewById(R.id.edit_stickimg_view);
        mTailoringSizeView = findViewById(R.id.tailoringSizeView);
        mEditShowVideoSlideView=findViewById(R.id.edit_show_video_slide_view);
        mEditShowCropButton=findViewById(R.id.edit_show_crop_button);
        mResultsDisplayRecycleview=findViewById(R.id.results_display_recycleview);
    }

    public void changeVideoSize(int videoWidth,int videoHeight) {
//        int videoWidth = mMediaPlayer.getVideoWidth();
//        int videoHeight = mMediaPlayer.getVideoHeight();

        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
        ViewGroup parent = (ViewGroup) mEditShowEffectView.getParent();
        int viewWidth = parent.getWidth();
        int viewHeight = parent.getHeight();

        if(videoWidth / (float)videoHeight > viewWidth /(float)viewHeight){
            videoHeight = viewWidth * videoHeight / videoWidth;
            videoWidth = viewWidth;
        }else{
            videoWidth = viewHeight * videoWidth / videoHeight;
            videoHeight = viewHeight;
        }

        ViewGroup.LayoutParams vlp = mEditShowEffectView.getLayoutParams();
        vlp.width = videoWidth;
        vlp.height = videoHeight;
        mEditShowEffectView.setLayoutParams(vlp);

        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。
//        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mEditShowEffectView.getLayoutParams();
//        layoutParams.bottomToBottom =ConstraintLayout.LayoutParams.PARENT_ID;
//        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
//        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
//        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
//        layoutParams.width = videoWidth;
//        layoutParams.height = videoHeight;
//        mEditShowEffectView.setLayoutParams(layoutParams);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playInit();
    }

    @Override
    protected void onDestroy() {
        TIME_STATUS = STOP_TIME_SHOW;
        mHander.sendEmptyMessage(STOP_TIME_SHOW);
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        playInit();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.edit_show_back_step:
                videoBack();
                break;
            case R.id.edit_show_next_step:
                videoNext();
                break;
            case R.id.edit_show_play_status:
                if (mIsPlay && mMediaPlayer.isPlaying()) {
                    stopPlay();
                } else {
                    startPlay();
                }
                break;
            case R.id.edit_save_btn:
                ShowProgressUitl.showProgress(this,getResources().getString(R.string.action_tip),true);
                if(mClipVideoInfoEntities.size()>1){
                    ArrayList<String> strings=new ArrayList<>();
                    for (VideoInfoEntity videoInfoEntity:mClipVideoInfoEntities){
                        if (videoInfoEntity.getData()==null)continue;
                        strings.add(videoInfoEntity.getData());
                    }
                    addVedio(strings,true);
                }
                break;
            case R.id.edit_show_crop_button:
                final String outPath= PathUitl.createVdeioUrl(MUSIC_VEDIO,"jiandeshiping");
                ShowProgressUitl.showProgress(this,getResources().getString(R.string.clip_tip),true);
                clipVideo(outPath,null,null);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void clipVideo(final String outPath, final String fristPath, final String lastPath) {
       float sTime=0,eTime=0;
        String out=outPath;
        if(fristPath==null){
            sTime=startTime/1000f;
            eTime=(endTime - startTime)/1000f;
        }else if(fristPath!=null){
            sTime=0;
            eTime=startTime/1000f;
            out=fristPath;
            if(lastPath!=null){
                out=lastPath;
                sTime=(endTime - startTime)/1000f;
                eTime=mMediaPlayer.getDuration()/1000f;
            }
        }
        final String finalOut = out;
//        Log.e("TAG", "clipVideo: "+out+"  "+fristPath+" "+lastPath+" "+sTime+"  "+eTime);
        mEpEditManager.clipVedio(mVdeioHandleUrl, out, sTime,eTime , new OnEpEditorCall() {
            @Override
            public void onSuccess() {
              if(fristPath==null) {
                  //添加剪切视频
                  VideoData.VideoSingData(VideoClipActivity.this, finalOut, new OnDataCompleteCall<VideoInfoEntity>() {
                      @Override
                      public void DataFinish(Context context, VideoInfoEntity videoInfoEntity) {
                          mClipVideoInfoEntities.add(0, videoInfoEntity);
                          mClipAdpater.notifyDataSetChanged();
                      }
                  });
                  final String path = PathUitl.createVdeioUrl(MUSIC_VEDIO,"fristveido");
                  clipVideo(outPath, path, null);
              }else if(fristPath!=null){
                  final String path = PathUitl.createVdeioUrl(MUSIC_VEDIO,"lastvedio");
                  if(lastPath!=null){
                      ArrayList<String> strings=new ArrayList<>();
                      strings.add(fristPath);
                      strings.add(lastPath);
                      addVedio(strings,false);
                  }else {
                      clipVideo(outPath, finalOut, path);
                  }
              }
            }

            @Override
            public void onFailure() {
                ToastUtil.ShowText(getResources().getString(R.string.fail_tip));
                ShowProgressUitl.cancelProgress();
            }

            @Override
            public void onProgress(float progress) {
                if(fristPath==null) {
                    ShowProgressUitl.UpdateProgress((int) (progress*100)/4);
                }else if(fristPath!=null){
                    if(lastPath!=null){
                        ShowProgressUitl.UpdateProgress((int) (progress*100)/4+50);
                    }else {
                        ShowProgressUitl.UpdateProgress((int) (progress*100)/4+25);
                    }
                }
            }
        });
    }

    private void addVedio(ArrayList<String> strings,boolean isSave) {
        if(strings.size()<=1){
            if (isSave) {
                //添加剪切视频
                VideoData.VideoSingData(VideoClipActivity.this, strings.get(0), new OnDataCompleteCall<VideoInfoEntity>() {
                    @Override
                    public void DataFinish(Context context, VideoInfoEntity videoInfoEntity) {
                        ShowProgressUitl.cancelProgress();
                        Intent intent = new Intent(VideoClipActivity.this, SaveShareActivity.class);
                        intent.putExtra("save", videoInfoEntity);
                        intent.putExtra("type", MBOTTOM_CLIP);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }else {
            final String outPath = PathUitl.createVdeioUrl(MUSIC_VEDIO, "hechengvedio");
            mEpEditManager.mergeVedioByLc(this, strings, outPath, mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight(), new OnEpEditorCall() {
                @Override
                public void onSuccess() {
                    if (isSave) {
                        //添加剪切视频
                        VideoData.VideoSingData(VideoClipActivity.this, outPath, new OnDataCompleteCall<VideoInfoEntity>() {
                            @Override
                            public void DataFinish(Context context, VideoInfoEntity videoInfoEntity) {
                                ShowProgressUitl.cancelProgress();
                                Intent intent = new Intent(VideoClipActivity.this, SaveShareActivity.class);
                                intent.putExtra("save", videoInfoEntity);
                                intent.putExtra("type", MBOTTOM_CLIP);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        ShowProgressUitl.cancelProgress();
                        StepEdtity stepEdtity = new StepEdtity();
                        stepEdtity.setPath(outPath);
                        addStep(stepEdtity);
                        videoSubstitution(stepEdtity);
                    }
                }

                @Override
                public void onFailure() {
                    ToastUtil.ShowText(getResources().getString(R.string.fail_tip));
                    ShowProgressUitl.cancelProgress();
                }

                @Override
                public void onProgress(float progress) {
                    if (!isSave) {
                        ShowProgressUitl.UpdateProgress((int) (progress * 100) / 4 + 75);
                    }else {
                        ShowProgressUitl.UpdateProgress((int) (progress * 100));
                    }
                }
            });
        }
    }

    private void addStep(StepEdtity stepEdtity) {
        //清除回退过的操作
        int length=steps.size();
        for (int i=nowIndex+1;i<length;i++){
            steps.remove(steps.size()-1);
        }
        mEditShowNextStep.setEnabled(false);
        mEditShowNextStep.setImageResource(R.drawable.icon_undo_right_disable);
        mEditShowBackStep.setImageResource(R.drawable.icon_undo_left);
        mEditShowBackStep.setEnabled(true);
        steps.add(stepEdtity);
        nowIndex++;
    }
    private void clearStep(StepEdtity stepEdtity) {
        if(steps.size()<=1)return;
        //删除步骤
        for (int i=0;i<steps.size();i++){
            if(steps.get(i).getPath().equals(stepEdtity.getPath())){
                steps.remove(i);
                i--;
                //如果删除视频当前正在展示
                if(mVdeioHandleUrl==stepEdtity.getPath()){
                    mVdeioHandleUrl=steps.get(0).getPath();
                    playInit();
                }

            }
        }
        if(steps.size()<=1){
            mEditShowNextStep.setEnabled(false);
            mEditShowNextStep.setImageResource(R.drawable.icon_undo_right_disable);
            mEditShowBackStep.setImageResource(R.drawable.icon_undo_left_disable);
            mEditShowBackStep.setEnabled(false);
        }
        nowIndex--;
    }
    private void startPlay() {
        if (mMediaPlayer == null) return;
        mMediaPlayer.start();//继续播放
        mEditShowPlayStatus.setImageResource(R.drawable.icon_pause);
        mIsPlay = true;
        TIME_STATUS = START_TIME_SHOW;
        mHander.sendEmptyMessageDelayed(START_TIME_SHOW, 1000);
    }

    public void stopPlay() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();//停止播放
            mIsPlay = false;
            mEditShowPlayStatus.setImageResource(R.drawable.icon_play);
            TIME_STATUS = STOP_TIME_SHOW;
            mHander.sendEmptyMessageDelayed(STOP_TIME_SHOW, 1000);
        }
    }

    //初始化
    private void playInit() {
        mMediaPlayer.reset();//重置MediaPlayer
        mMediaPlayer.setDisplay(mSurfaceHolder);//把视频画面输出到SurfaceView中
        try {
            if (mVideoDatas.size() > 0) {
                mMediaPlayer.setDataSource(mVdeioHandleUrl);//设置要播放的内容在根目录下的位置
                mMediaPlayer.prepare();//预加载
                String nowtime = TimeUitl.getTimeString(mMediaPlayer.getCurrentPosition(), "mm:ss");
                alltime = TimeUitl.getTimeString(mMediaPlayer.getDuration(), "mm:ss");
                mEditShowNowTime.setText(nowtime + "/" + alltime);

                mIsPlay = false;
                mEditShowPlayStatus.setImageResource(R.drawable.icon_play);
                TIME_STATUS = STOP_TIME_SHOW;
                mHander.sendEmptyMessageDelayed(STOP_TIME_SHOW, 1000);

                //加载第一fan
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                       media= new MediaMetadataRetriever();
                        media.setDataSource(mVdeioHandleUrl);
                        ArrayList<Bitmap> bitmaps=new ArrayList<>();
                        double duan=mMediaPlayer.getDuration()*1.0/count;
                        for (int i=0;i<count;i++) {
                            Bitmap bitmap = media.getFrameAtTime((long) (duan*1000*i));
                            bitmaps.add(bitmap);
                        }
                        Message message = Message.obtain();
                        message.what = FRIST_BITMAP;
                        message.obj = bitmaps;
                        mHander.sendMessage(message);
                    }
                }.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //执行一次操作
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void videoNext() {
        if ((nowIndex + 1) < steps.size()) {
            ShowProgressUitl.showProgress(this,getResources().getString(R.string.chang_tip),false);
            videoSubstitution(steps.get(++nowIndex));
            mEditShowBackStep.setImageResource(R.drawable.icon_undo_left);
            mEditShowBackStep.setEnabled(true);
            if (nowIndex + 1 >= steps.size()) {
                mEditShowNextStep.setEnabled(false);
                mEditShowNextStep.setImageResource(R.drawable.icon_undo_right_disable);
            }
        }
    }

    //回退一次操作
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void videoBack() {
        if (nowIndex - 1 >= 0) {
            ShowProgressUitl.showProgress(this,getResources().getString(R.string.chang_tip),false);
            videoSubstitution(steps.get(--nowIndex));
            mEditShowNextStep.setEnabled(true);
            mEditShowNextStep.setImageResource(R.drawable.icon_undo_right);
            if (nowIndex - 1 < 0) {
                mEditShowBackStep.setImageResource(R.drawable.icon_undo_left_disable);
                mEditShowBackStep.setEnabled(false);
            }
        }
    }

    //具体操作
    private void videoSubstitution(StepEdtity stepEdtity) {
        mVdeioHandleUrl = stepEdtity.getPath();
        playInit();
    }

    @Override
    public void onClickItemAndData(View.OnClickListener onClickListener, int postion, ArrayList<VideoInfoEntity> datas) {

    }

    @Override
    public void onClickItem(View.OnClickListener onClickListener, int postion) {
        switch (postion){
            case ONCLICK_ADD:
                Intent intent = new Intent(this,SelectVideoActivity.class);
                intent.putExtra("selectvideos", mVideoDatas);
                intent.putExtra("maxCount", 5-mVideoDatas.size());
                intent.putExtra("type",RESELECT_CLIP);
                startActivity(intent);
                finish();
                break;
            default:
                if(postion>=ONCLICK_DELETE){
                    mClipVideoInfoEntities.remove(postion-ONCLICK_DELETE);
                    mClipAdpater.notifyDataSetChanged();
//                    clearStep(steps.get(postion-ONCLICK_DELETE+1));
                }else {
//                    mVdeioHandleUrl = mClipVideoInfoEntities.get(postion).getData();
//                    playInit();
                }
                break;
        }
    }
}