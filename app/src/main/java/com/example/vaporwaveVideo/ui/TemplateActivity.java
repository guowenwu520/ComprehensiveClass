package com.example.vaporwaveVideo.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.vaporwaveVideo.entity.HomeModeEntity;
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
import static com.example.vaporwaveVideo.ui.MainActivity.MBOTTOM_MODE;
import static com.example.vaporwaveVideo.ui.SelectVideoActivity.RESELECT_CLIP;
import static com.example.vaporwaveVideo.util.PathUitl.LOGO_BITMAP;

public class TemplateActivity  extends AppCompatActivity implements
        View.OnClickListener,
        MediaPlayer.OnCompletionListener,
        SurfaceHolder.Callback{


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
    private StickImgView mStickImgView;
    private TailoringSizeView mTailoringSizeView;
    private EpEditManager mEpEditManager;

    private TextView mEditSaveBtn;
    private ImageView mImgBack;
    private SurfaceView mEditShowEffectView;
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder mSurfaceHolder;
    private MediaMetadataRetriever media;
    private TextView mTextTip;

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

    HomeModeEntity homeModeEntity;
    ArrayList<VideoInfoEntity> videoInfoEntitys;
    int type;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowsUtil.setTransparentStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templater);
        homeModeEntity= (HomeModeEntity) getIntent().getSerializableExtra("data");
        videoInfoEntitys= (ArrayList<VideoInfoEntity>) getIntent().getSerializableExtra("selectvideos");
        type=getIntent().getIntExtra("type",0);
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
//        startPlay();
    }

    private void initShow() {
        if (mVideoDatas.size() < 0) return;
        mTextTip.setText(getString(R.string.select_max_count,1,1));

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
        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//                Log.e("sizechang","is");
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
        mVdeioHandleUrl =mVideoDatas.get(0).getData();
        ShowProgressUitl.cancelProgress();
        StepEdtity stepEdtity = new StepEdtity();
        stepEdtity.setPath(mVdeioHandleUrl);
        steps.add(stepEdtity);

        if(mVideoDatas.size()<5)
            mClipVideoInfoEntities.add(new VideoInfoEntity());


        mEpEditManager = new EpEditManager();

    }

    private void initView() {
        mEditSaveBtn = findViewById(R.id.save_determine);
        mEditShowBackStep = findViewById(R.id.edit_show_back_step);
        mEditShowNextStep = findViewById(R.id.edit_show_next_step);
        mEditShowNowTime = findViewById(R.id.edit_show_now_time);
        mEditShowPlayStatus = findViewById(R.id.edit_show_play_status);
        mImgBack = findViewById(R.id.img_back);
        mTextTip=findViewById(R.id.text_tip);
        mEditShowEffectView = findViewById(R.id.edit_show_effect_view);
        mStickImgView = findViewById(R.id.edit_stickimg_view);
        mTailoringSizeView = findViewById(R.id.tailoringSizeView);
        mEditShowVideoSlideView=findViewById(R.id.edit_show_video_slide_view);
    }

    public void changeVideoSize(int videoWidth,int videoHeight) {
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
            case R.id.save_determine:
                ShowProgressUitl.showProgress(this,getResources().getString(R.string.action_tip),true);
                final String path = PathUitl.createVdeioUrl(PathUitl.MUSIC_VEDIO,"fristveido");
                clipVideo(path,startTime/1000f,(endTime - startTime)/1000f);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void clipVideo(final String outPath, float startTime, float  lastTime) {
        mEpEditManager.clipVedio(mVdeioHandleUrl, outPath, startTime,lastTime , new OnEpEditorCall() {
            @Override
            public void onSuccess() {
                ShowProgressUitl.showProgress(TemplateActivity.this,getResources().getString(R.string.action_tip),true);
              Bitmap bitmap=  BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("template"+homeModeEntity.getType(),"drawable",TemplateActivity.this.getPackageName()));
                String logoPath=PathUitl.createLogoUrl(bitmap,LOGO_BITMAP);
                final String path = PathUitl.createVdeioUrl(PathUitl.MUSIC_VEDIO,"fristveido");
                mEpEditManager.andLogo(outPath, logoPath, path, bitmap.getWidth(), bitmap.getHeight(), new OnEpEditorCall() {
                    @Override
                    public void onSuccess() {
                        //添加剪切视频
                        VideoData.VideoSingData(TemplateActivity.this, path, new OnDataCompleteCall<VideoInfoEntity>() {
                            @Override
                            public void DataFinish(Context context, VideoInfoEntity videoInfoEntity) {
                                ShowProgressUitl.cancelProgress();
                                Intent intent = new Intent(TemplateActivity.this, SaveShareActivity.class);
                                intent.putExtra("save", videoInfoEntity);
                                intent.putExtra("type", MBOTTOM_MODE);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        ToastUtil.ShowText(getResources().getString(R.string.fail_tip));
                        ShowProgressUitl.cancelProgress();
                    }

                    @Override
                    public void onProgress(float progress) {
                        ShowProgressUitl.UpdateProgress((int) (progress*100)/2+50);
                    }
                });

            }

            @Override
            public void onFailure() {
                ToastUtil.ShowText(getResources().getString(R.string.fail_tip));
                ShowProgressUitl.cancelProgress();
            }

            @Override
            public void onProgress(float progress) {
                    ShowProgressUitl.UpdateProgress((int) (progress*100)/2);
            }
        });
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
}