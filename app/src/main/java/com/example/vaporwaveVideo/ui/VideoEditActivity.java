package com.example.vaporwaveVideo.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.adapter.EditSelectActionViewPagerAdapter;
import com.example.vaporwaveVideo.data.VideoData;
import com.example.vaporwaveVideo.entity.EditInfoEdtity;
import com.example.vaporwaveVideo.entity.StepEdtity;
import com.example.vaporwaveVideo.entity.VideoInfoEntity;
import com.example.vaporwaveVideo.filter.Constants;
import com.example.vaporwaveVideo.filter.MagicFilterType;
import com.example.vaporwaveVideo.filter.MediaPlayerWrapper;
import com.example.vaporwaveVideo.filter.SlideGpuFilterGroup;
import com.example.vaporwaveVideo.filter.VideoClipper;
import com.example.vaporwaveVideo.filter.VideoInfo;
import com.example.vaporwaveVideo.filter.WaterMarkFilter;
import com.example.vaporwaveVideo.interfacecall.OnDataCompleteCall;
import com.example.vaporwaveVideo.interfacecall.OnEpEditorCall;
import com.example.vaporwaveVideo.interfacecall.OnSizeChangCall;
import com.example.vaporwaveVideo.ui.fragments.FilterFragment;
import com.example.vaporwaveVideo.ui.fragments.MusicFragment;
import com.example.vaporwaveVideo.ui.fragments.PasterFragment;
import com.example.vaporwaveVideo.ui.fragments.SizeFragment;
import com.example.vaporwaveVideo.ui.fragments.SpeedFragment;
import com.example.vaporwaveVideo.ui.view.StickImgView;
import com.example.vaporwaveVideo.ui.view.TailoringSizeView;
import com.example.vaporwaveVideo.ui.view.VideoPreviewView;
import com.example.vaporwaveVideo.util.PathUitl;
import com.example.vaporwaveVideo.util.ShowProgressUitl;
import com.example.vaporwaveVideo.util.TimeUitl;
import com.example.vaporwaveVideo.util.ToastUtil;
import com.example.vaporwaveVideo.util.WindowsUtil;
import com.example.vaporwaveVideo.viedoEditor.EpEditManager;
import com.google.android.material.tabs.TabLayout;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.FlipHorizontallyEvent;
import com.xiaopo.flying.sticker.StickerIconEvent;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT;
import static com.example.vaporwaveVideo.ui.MainActivity.MBOTTOM_EDIT;
import static com.example.vaporwaveVideo.util.PathUitl.LOGO_BITMAP;
import static com.example.vaporwaveVideo.util.PathUitl.MUSIC_VEDIO;

public class VideoEditActivity extends AppCompatActivity implements
        View.OnClickListener,
        StickerIconEvent ,
MediaPlayerWrapper.IMediaCallback,
        SlideGpuFilterGroup.OnFilterChangeListener{
    private static final String TAG = VideoEditActivity.class.getName();

    public static final int TYPE_SDEEP = 12;
    public static final int TYPE_MUSIC = 13;
    public static final int TYPE_PASTER = 14;
    public static final int TYPE_SIZE = 15;
    public static final int TYPE_FILTER = 17;

    private static final int STOP_TIME_SHOW = 1;
    private static final int START_TIME_SHOW = 2;
    private static final int FRIST_BITMAP = 4;
    static final int VIDEO_PREPARE = 10;
    static final int VIDEO_START = 21;
    static final int VIDEO_UPDATE = 32;
    static final int VIDEO_PAUSE = 43;
    static final int VIDEO_CUT_FINISH = 54;
    //用于切换状态
    private static int TIME_STATUS = 3;
    ArrayList<VideoInfoEntity> mVideoDatas = new ArrayList<>();
    ArrayList<Fragment> mFragments = new ArrayList<>();

    private TextView mEditSaveBtn;
    private ImageView mImgBack;

    private VideoPreviewView mEditShowEffectView;
//    private MediaPlayer mMediaPlayer;
//    private SurfaceHolder mSurfaceHolder;

    private TextView mEditShowNowTime;
    private ImageView mEditShowPlayStatus;
    private ImageView mEditShowBackStep;
    private ImageView mEditShowNextStep;
    private TabLayout mEditShowActionMeunTablayout;
    private ViewPager mEditShowActionSelectViewpager;
    private StickImgView mStickImgView;
    private TailoringSizeView mTailoringSizeView;
    private EpEditManager mEpEditManager;

    private MagicFilterType filterType = MagicFilterType.NONE;
    private boolean mIsPlay = false;
    private boolean isDestroy=false;
    private String alltime;
    //临时处理的URL
    public String mVdeioHandleUrl;
    //存操作步骤
    private ArrayList<StepEdtity> steps = new ArrayList<>();
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
                if (mEditShowEffectView == null) return;
                String nowtime = TimeUitl.getTimeString(mEditShowEffectView.getCurrentPosition(), "mm:ss");
                mEditShowNowTime.setText(nowtime + "/" + alltime);
                mHander.sendEmptyMessageDelayed(START_TIME_SHOW, 1000);
                mEditShowEffectView.setBackground(null);
            } else if (msg.what == FRIST_BITMAP) {
                mEditShowEffectView.setBackground(new BitmapDrawable((Bitmap) msg.obj));
                ShowProgressUitl.cancelProgress();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowsUtil.setTransparentStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video__edit);   Constants.init(this);

        initView();
        initData();
        initShow();
    }


    private void initData() {
        mVideoDatas = (ArrayList<VideoInfoEntity>) getIntent().getSerializableExtra("selectvideos");
        mFragments.add(new MusicFragment());
            mFragments.add(new FilterFragment(mVideoDatas.get(0).getData()));
         mFragments.add(new PasterFragment());
        mFragments.add(new SizeFragment());
        mFragments.add(new SpeedFragment());
        //初始化一个数据
        mVdeioHandleUrl = mVideoDatas.get(0).getData();
        ArrayList<String> srcList = new ArrayList<>();
        srcList.add(mVdeioHandleUrl);
        mEditShowEffectView.setVideoPath(srcList,true);
        StepEdtity stepEdtity=new StepEdtity();
        stepEdtity.setPath(mVdeioHandleUrl);
        stepEdtity.setType(TYPE_PASTER);
        steps.add(stepEdtity);
        nowIndex++;
        //加载第一fan
        new Thread() {
            @Override
            public void run() {
                super.run();
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(mVdeioHandleUrl);
                Bitmap bitmap = media.getFrameAtTime();
                Message message = Message.obtain();
                message.what = FRIST_BITMAP;
                message.obj = bitmap;
                mHander.sendMessage(message);
            }
        }.start();  pathOrig=mVdeioHandleUrl;
        //  //初始化sticker
        BitmapStickerIcon closeIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.icon_delete), BitmapStickerIcon.LEFT_TOP);
        closeIcon.setIconEvent(new DeleteIconEvent());
        BitmapStickerIcon confirmIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.icon_hook), BitmapStickerIcon.RIGHT_TOP);
        confirmIcon.setIconEvent(this);
        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.icon_drag), BitmapStickerIcon.LEFT_BOTTOM);
        flipIcon.setIconEvent(new FlipHorizontallyEvent());
        BitmapStickerIcon scaleIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.icon_rotate), BitmapStickerIcon.RIGHT_BOTOM);
        scaleIcon.setIconEvent(new ZoomIconEvent());
        mStickImgView.setIcons(Arrays.asList(closeIcon, flipIcon,confirmIcon, scaleIcon));

        mEpEditManager=new EpEditManager();
    }

    private void initShow() {
        if (mVideoDatas.size() < 0) return;

//        mMediaPlayer = new MediaPlayer();
//        mSurfaceHolder = mEditShowEffectView.getHolder();
//        mSurfaceHolder.addCallback(this);



        mEditShowBackStep.setEnabled(false);
        mEditShowNextStep.setEnabled(false);
        EditSelectActionViewPagerAdapter editSelectActionViewPagerAdapter = new EditSelectActionViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_SET_USER_VISIBLE_HINT, mFragments, this);
        mEditShowActionSelectViewpager.setAdapter(editSelectActionViewPagerAdapter);
        mEditShowActionSelectViewpager.setOffscreenPageLimit(1);
        mEditShowActionMeunTablayout.setupWithViewPager(mEditShowActionSelectViewpager);


        mImgBack.setOnClickListener(VideoEditActivity.this);
        mEditSaveBtn.setOnClickListener(VideoEditActivity.this);
        mEditShowPlayStatus.setOnClickListener(VideoEditActivity.this);
        mEditShowNextStep.setOnClickListener(VideoEditActivity.this);
        mEditShowBackStep.setOnClickListener(VideoEditActivity.this);
        mEditShowEffectView.setOnFilterChangeListener(VideoEditActivity.this);
        mEditShowEffectView.setIMediaCallback(this);
        mEditShowEffectView.setMedioSizeChang(new MediaPlayerWrapper.MedioSizeChang() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                changeVideoSize(width,height);
            }
        });
    }

    public void changeVideoSize(int videoWidth,int videoHeight) {
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


    private void initView() {
        mEditSaveBtn = findViewById(R.id.edit_save_btn);
        mEditShowActionMeunTablayout = findViewById(R.id.edit_show_action_meun_tablayout);
        mEditShowBackStep = findViewById(R.id.edit_show_back_step);
        mEditShowNextStep = findViewById(R.id.edit_show_next_step);
        mEditShowNowTime = findViewById(R.id.edit_show_now_time);
        mEditShowPlayStatus = findViewById(R.id.edit_show_play_status);
        mEditShowActionSelectViewpager = findViewById(R.id.edit_show_action_select_viewpager);
        mImgBack = findViewById(R.id.img_back);
        mEditShowEffectView = findViewById(R.id.edit_show_effect_view);
        mStickImgView = findViewById(R.id.edit_stickimg_view);
        mTailoringSizeView=findViewById(R.id.tailoringSizeView);
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
                if (mIsPlay && mEditShowEffectView.isPlaying()) {
                    stopPlay();
                } else {
                    startPlay();
                }
                break;
            case R.id.edit_save_btn:
                ShowProgressUitl.showProgress(this,getResources().getString(R.string.action_tip),false);mEditShowEffectView.pause();
                VideoClipper clipper = new VideoClipper();
                clipper.showBeauty();
                clipper.setInputVideoPath(mVdeioHandleUrl);
                String outputPath =PathUitl.createVdeioUrl(MUSIC_VEDIO);
                clipper.setFilterType(filterType);
                clipper.setOutputVideoPath(outputPath);
                clipper.setOnVideoCutFinishListener(new VideoClipper.OnVideoCutFinishListener() {
                    @Override
                    public void onFinish() {
                        VideoData.VideoSingData(VideoEditActivity.this, outputPath, new OnDataCompleteCall<VideoInfoEntity>() {
                            @Override
                            public void DataFinish(Context context, VideoInfoEntity videoInfoEntity) {
                                ShowProgressUitl.cancelProgress();
                                Intent intent=new Intent(VideoEditActivity.this, SaveShareActivity.class);
                                intent.putExtra("save",videoInfoEntity);
                                intent.putExtra("type",MBOTTOM_EDIT);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
                try {
                    Log.e("hero","-----PreviewActivity---clipVideo");
                    clipper.clipVideo(0,mEditShowEffectView.getVideoDuration()*1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onActionDown(final StickerView stickerView, MotionEvent event) {

        Bitmap bitmap=stickerView.createBitmap();
        String logoPath=PathUitl.createLogoUrl(bitmap,LOGO_BITMAP);

//        WaterMarkFilter waterMarkFilter = new WaterMarkFilter(getResources());
//        waterMarkFilter.setWaterMark(BitmapFactory.decodeFile(logoPath));
//
//        waterMarkFilter.setPosition(0,0,0,0);
//        mEditShowEffectView.addLogo(waterMarkFilter);
        final String outfilePath = PathUitl.createVdeioUrl(MUSIC_VEDIO);
        ShowProgressUitl.showProgress(VideoEditActivity.this,getResources().getString(R.string.action_tip),true);
        mEpEditManager.andLogo(mVdeioHandleUrl, logoPath, outfilePath,bitmap.getWidth(),bitmap.getHeight(), new OnEpEditorCall() {
            @Override
            public void onSuccess() {
                        ShowProgressUitl.cancelProgress();
                        stickerView.removeAllStickers();
                        StepEdtity stepEdtity=new StepEdtity();
                        stepEdtity.setPath(outfilePath);
                        stepEdtity.setType(TYPE_PASTER);
                        addStep(stepEdtity);
                        videoSubstitution(stepEdtity);
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
    }



    @Override
    public void onActionMove(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionUp(StickerView stickerView, MotionEvent event) {

    }

    private void startPlay() {
        if(mEditShowEffectView==null)return;
        mEditShowEffectView.start();
        mEditShowPlayStatus.setImageResource(R.drawable.icon_pause);
        mIsPlay = true;
        TIME_STATUS = START_TIME_SHOW;
        mHander.sendEmptyMessageDelayed(START_TIME_SHOW, 1000);
    }

    public void stopPlay() {
        if (mEditShowEffectView.isPlaying()) {
            mEditShowEffectView.pause();//停止播放
            mIsPlay = false;
            mEditShowPlayStatus.setImageResource(R.drawable.icon_play);
            TIME_STATUS = STOP_TIME_SHOW;
            mHander.sendEmptyMessageDelayed(STOP_TIME_SHOW, 1000);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopPlay();
        playInit();startPlay();
    }

    @Override
    protected void onDestroy() {
        isDestroy = true;
        TIME_STATUS = STOP_TIME_SHOW;
        mHander.sendEmptyMessage(STOP_TIME_SHOW);
        mEditShowEffectView.onDestroy();
        super.onDestroy();
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

    //初始化
    private void playInit() {
        try {
            if (mVideoDatas.size() > 0) {
                ArrayList<String> srcList = new ArrayList<>();
                srcList.add(mVdeioHandleUrl);
                mEditShowEffectView.setVideoPath(srcList, false);
                String nowtime = TimeUitl.getTimeString(mEditShowEffectView.getCurrentPosition(), "mm:ss");
                alltime = TimeUitl.getTimeString(mEditShowEffectView.getVideoDuration(), "mm:ss");
                mEditShowNowTime.setText(nowtime + "/" + alltime);

                mIsPlay = false;
                mEditShowPlayStatus.setImageResource(R.drawable.icon_play);
                TIME_STATUS = STOP_TIME_SHOW;
                mHander.sendEmptyMessageDelayed(STOP_TIME_SHOW, 1000);
                ShowProgressUitl.cancelProgress();
            startPlay();
            }

        } catch (Exception e) {
            e.printStackTrace();
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

  private String  pathOrig;
    //具体操作
    private void videoSubstitution(StepEdtity stepEdtity) {
        Log.e(TAG, "videoSubstitution: "+stepEdtity.getPath()+ "  "+pathOrig);
        mVdeioHandleUrl = stepEdtity.getPath();
        switch (stepEdtity.getType()) {
            case TYPE_SDEEP:
                if(stepEdtity.getPath().equals(pathOrig)) {
                    videoAddChangSpeed(stepEdtity.getSpeed(), false);
                    ShowProgressUitl.cancelProgress();
                }else {
                    playInit();
                }
                break;
            case TYPE_FILTER:
                if(stepEdtity.getPath().equals(pathOrig)) {
                    mEditShowEffectView.setNext(stepEdtity.getFilkterType());
                    ShowProgressUitl.cancelProgress();
                }else {
                    playInit();
                }
                break;
            case TYPE_SIZE:
            case TYPE_PASTER:
            case TYPE_MUSIC:
            default:
                playInit();
                break;
        }
        pathOrig=stepEdtity.getPath();
    }

    //操作
    public void videoAddPaster(int img_id) {
        DrawableSticker sticker = new DrawableSticker(ContextCompat.getDrawable(this, img_id));
        mStickImgView.addSticker(sticker);
    }

    public void videoAddSize(EditInfoEdtity editInfoEdtity) {
        if(mTailoringSizeView.getVisibility()==View.INVISIBLE) mTailoringSizeView.setVisibility(View.VISIBLE);
         mTailoringSizeView.setSizeRatio(editInfoEdtity.getSizeWidth()/(float)editInfoEdtity.getSizeHeight());
        mTailoringSizeView.setPadding(200);
        mTailoringSizeView.setOnSizeChangCall(new OnSizeChangCall() {
            @Override
            public void delete() {
                mTailoringSizeView.setVisibility(View.INVISIBLE);
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void chang(float wdith, float height, float x2, float y2) {
                ShowProgressUitl.showProgress(VideoEditActivity.this,getResources().getString(R.string.action_tip),true);
                final String outfilePath = PathUitl.createVdeioUrl(MUSIC_VEDIO);
                mEpEditManager.changSize(mVdeioHandleUrl, outfilePath, (int)wdith,  (int)height,  (int)x2, (int) y2, new OnEpEditorCall() {
                    @Override
                    public void onSuccess() {
                        ShowProgressUitl.cancelProgress();
                        StepEdtity stepEdtity = new StepEdtity();
                        stepEdtity.setPath(outfilePath);
                        stepEdtity.setType(TYPE_SIZE);
                        addStep(stepEdtity);
                        mTailoringSizeView.setVisibility(View.INVISIBLE);
                        videoSubstitution(stepEdtity);
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
            }
        });
        mTailoringSizeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mTailoringSizeView.onTouch(v,event);
                return true;
            }
        });
    }

    public void videoAddChangSpeed(float chang, boolean isNewAdd) {
        if (isNewAdd) {
            StepEdtity stepEdtity = new StepEdtity();
            stepEdtity.setType(TYPE_SDEEP);
            stepEdtity.setSpeed(chang);
            stepEdtity.setPath(mVdeioHandleUrl);
            addStep(stepEdtity);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PlaybackParams params = mEditShowEffectView.getPlaybackParams();
            params.setSpeed(chang);
            mIsPlay = true;
            mEditShowPlayStatus.setImageResource(R.drawable.icon_pause);
            TIME_STATUS = START_TIME_SHOW;
            mHander.sendEmptyMessageDelayed(START_TIME_SHOW, 1000);
            mEditShowEffectView.setPlaybackParams(params);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void videoAddFilter(EditInfoEdtity filter) {
        StepEdtity stepEdtity = new StepEdtity();
        stepEdtity.setFilkterType(filter.getFilterType());
        stepEdtity.setType(TYPE_FILTER);
        stepEdtity.setPath(mVdeioHandleUrl);
        addStep(stepEdtity);
        videoSubstitution(stepEdtity);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TYPE_MUSIC) {
            if (data != null) {
                addBackgroundMusic(data.getData());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void addBackgroundMusic(Uri data) {
        Cursor cursor = getContentResolver().query(data, null, null,
                null, null);
        cursor.moveToFirst();
        final String v_path = PathUitl.getPath(this, data); // 图片文件路径
        String v_name = cursor.getString(2); // 图片文件名
        //更新音乐布局
        ArrayList<EditInfoEdtity> editInfoEdtities = new ArrayList<>();
        EditInfoEdtity editInfoEdtity = new EditInfoEdtity();
        editInfoEdtity.setMusicName(v_name);
        editInfoEdtity.setMusicData(v_path);
        editInfoEdtities.add(editInfoEdtity);
        editInfoEdtities.add(new EditInfoEdtity());
        ((MusicFragment) mFragments.get(0)).setData(editInfoEdtities);

        final String outfilePathlingshi = PathUitl.createVdeioUrl(MUSIC_VEDIO);
        ShowProgressUitl.showProgress(this, getResources().getString(R.string.action_tip),true);

        //分离原来的音乐
        mEpEditManager.audioAndVideoSeparation(mVdeioHandleUrl, outfilePathlingshi, new OnEpEditorCall() {
            @Override
            public void onSuccess() {
                //添加选择的音乐
                final String outfilePath = PathUitl.createVdeioUrl(MUSIC_VEDIO);
                mEpEditManager.andAudio(outfilePathlingshi, v_path, outfilePath, new OnEpEditorCall() {
                    @Override
                    public void onSuccess() {
                        ShowProgressUitl.cancelProgress();
                        StepEdtity stepEdtity = new StepEdtity();
                        stepEdtity.setPath(outfilePath);
                        stepEdtity.setType(TYPE_MUSIC);
                        addStep(stepEdtity);
                        videoSubstitution(stepEdtity);
                    }

                    @Override
                    public void onFailure() {
                                ToastUtil.ShowText(getResources().getString(R.string.fail_tip));
                                ShowProgressUitl.cancelProgress();
                    }

                    @Override
                    public void onProgress(float progress) {
                        ShowProgressUitl.UpdateProgress((int) (progress * 100.0 / 2) + 50);
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
                ShowProgressUitl.UpdateProgress((int) (progress * 100.0 / 2));
            }
        });
    }
    @Override
    public void onFilterChange(final MagicFilterType type) {
        this.filterType = type;

    }

    @Override
    public void onVideoPrepare() {
//        startPlay();
    }

    @Override
    public void onVideoStart() {
        mHander.sendEmptyMessage(VIDEO_START);
    }

    @Override
    public void onVideoPause() {
        mHander.sendEmptyMessage(VIDEO_PAUSE);
    }

    @Override
    public void onCreateSufer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playInit();
            }
        });
    }

    @Override
    public void onVideoChanged(VideoInfo info) {

    }

}