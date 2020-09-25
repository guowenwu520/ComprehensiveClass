package com.example.vaporwaveVideo.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.adapter.VideoSelectAdapter;
import com.example.vaporwaveVideo.data.VideoData;
import com.example.vaporwaveVideo.entity.HomeModeEntity;
import com.example.vaporwaveVideo.entity.VideoInfoEntity;
import com.example.vaporwaveVideo.interfacecall.OnDataCompleteCall;
import com.example.vaporwaveVideo.interfacecall.OnRecycleListerCall;
import com.example.vaporwaveVideo.util.DensityUtil;
import com.example.vaporwaveVideo.util.TimeUitl;
import com.example.vaporwaveVideo.util.WindowsUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.example.vaporwaveVideo.ui.MainActivity.MBOTTOM_CLIP;
import static com.example.vaporwaveVideo.ui.MainActivity.MBOTTOM_EDIT;
import static com.example.vaporwaveVideo.ui.MainActivity.MBOTTOM_MODE;

public class SelectVideoActivity extends AppCompatActivity implements View.OnClickListener, OnRecycleListerCall {
   //最大选择数量
   private int mMaxSelectCount=3;
   //当前选择数量
    private  int mSelectCount=0;
    //当前显示状态；
    private  boolean isSelectAll=true;

    private int mType;

    private  static  final  int CODE_CAMERA_ALL=123;
    private  static  final  int CODE_CAMERA_SELECT=124;
    public   static  final  int RESELECT_CLIP=125;

   private ImageView mImgBack;
   private TextView mSelectSingTitle;
   private LinearLayout mSelectMoreTitle;
   private TextView mSelectMoreAll;
   private TextView mSelectMoreSelected;
   private RecyclerView mSelectVideoRecycleview;
   private TextView mTextTip;
   //确定选择
   private TextView mSelectDetermine;
   private ArrayList<VideoInfoEntity> mVideoInfoEntities =new ArrayList<>();
   //选中的视频
    private ArrayList<VideoInfoEntity> mSelectedVideoInfoEntities=new ArrayList<>();
   private VideoSelectAdapter mVideoSelectAdapter;
    private String videoUri="";
   //模板
    private HomeModeEntity homeModeEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowsUtil.setTransparentStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__video);
        mMaxSelectCount=getIntent().getIntExtra("maxCount",1);
        //接收多次选择处理
        mSelectedVideoInfoEntities=(ArrayList<VideoInfoEntity>) getIntent().getSerializableExtra("selectvideos");
        if(mSelectedVideoInfoEntities==null){
            mSelectedVideoInfoEntities=new ArrayList<>();
        }
        mType=getIntent().getIntExtra("type",MBOTTOM_CLIP);
        homeModeEntity= (HomeModeEntity) getIntent().getSerializableExtra("data");
        initView();
        initData();
    }

    private void initData() {
        mSelectCount=0;
        VideoData.initVideoData(this, new OnDataCompleteCall<ArrayList<VideoInfoEntity>>() {
            @Override
            public void DataFinish(Context context,ArrayList<VideoInfoEntity> videoInfoEntities) {
                //空用于拍照站位
                videoInfoEntities.add(0,new VideoInfoEntity());
               SelectVideoActivity.this.mVideoInfoEntities =videoInfoEntities;
               //如果有默认选中去除掉
               if(mSelectedVideoInfoEntities.size()>0){
                   for (int i=1;i<mVideoInfoEntities.size();i++){
                       for (int j=0;j<mSelectedVideoInfoEntities.size();j++){
                           if(mVideoInfoEntities.get(i).getData().equals(mSelectedVideoInfoEntities.get(j).getData())){
                               mVideoInfoEntities.remove(i);
                               i--;
                               break;
                           }
                       }
                   }
               }
                initShow();
            }
        });

    }

    private void initShow() {
        mTextTip.setText(getString(R.string.select_max_count,mSelectCount,mMaxSelectCount));

        mVideoSelectAdapter=new VideoSelectAdapter(this,mVideoInfoEntities);
        mSelectVideoRecycleview.setAdapter(mVideoSelectAdapter);
        mSelectVideoRecycleview.setLayoutManager(new GridLayoutManager(this,3));

        mVideoSelectAdapter.setOnRecycleListerCall(this);
        mImgBack.setOnClickListener(this);
        mSelectMoreAll.setOnClickListener(this);
        mSelectMoreSelected.setOnClickListener(this);
        mSelectDetermine.setOnClickListener(this);

        if(mMaxSelectCount>1){
            mSelectSingTitle.setVisibility(View.GONE);
            mSelectMoreTitle.setVisibility(View.VISIBLE);
            setSelectStatus(isSelectAll);
        }else {
            mSelectMoreTitle.setVisibility(View.GONE);
            mSelectSingTitle.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        mImgBack=findViewById(R.id.img_back);
        mSelectSingTitle=findViewById(R.id.select_sing_title);
        mSelectMoreTitle=findViewById(R.id.select_more_title);
        mSelectMoreAll=findViewById(R.id.select_more_all);
        mSelectMoreSelected=findViewById(R.id.select_more_selected);
        mSelectVideoRecycleview=findViewById(R.id.select_video_recycleview);
        mTextTip=findViewById(R.id.text_tip);
        mSelectDetermine=findViewById(R.id.select_determine);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.select_more_all:
                isSelectAll=true;
                setSelectStatus(isSelectAll);
                break;
            case R.id.select_more_selected:
                isSelectAll=false;
                setSelectStatus(isSelectAll);
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.select_determine:
                if(mSelectedVideoInfoEntities.size()>0) {
                    if(mType==MBOTTOM_EDIT) {
                        Intent intent = new Intent(this, VideoEditActivity.class);
                        intent.putExtra("selectvideos", mSelectedVideoInfoEntities);
                        startActivity(intent);
                    }else if(mType==MBOTTOM_CLIP||mType==RESELECT_CLIP){
                        Intent intent = new Intent(this, VideoClipActivity.class);
                        intent.putExtra("selectvideos", mSelectedVideoInfoEntities);
                        startActivity(intent);
                    }else if(mType==MBOTTOM_MODE){
                        Intent intent=new Intent(this, TemplateActivity.class);
                        intent.putExtra("type",MBOTTOM_MODE);
                        intent.putExtra("data",homeModeEntity);
                        intent.putExtra("selectvideos", mSelectedVideoInfoEntities);
                        startActivity(intent);
                    }
                    finish();
                }
                break;
        }
    }

    @Override
    public void onClickItemAndData(View.OnClickListener onClickListener, int postion, ArrayList<VideoInfoEntity> datas) {
        //如果是相机
        if(postion==0){
            if(mSelectCount>=mMaxSelectCount)return;
             openCamera();
        }else {
            if (datas.get(postion).isSelected()) {
                //改变大于取消值得提示
                changMaxSelectTip(datas.get(postion).getSelectCount(),1);
                mSelectCount--;
                datas.get(postion).setSelected(false);
                mSelectedVideoInfoEntities.remove(datas.get(postion).getSelectCount()-1);
            } else {
                if(mSelectCount>=mMaxSelectCount)return;
                mSelectCount++;
                datas.get(postion).setSelected(true);
                datas.get(postion).setSelectCount(mSelectCount);
                mSelectedVideoInfoEntities.add(datas.get(postion));
            }
            mTextTip.setText(getString(R.string.select_max_count, mSelectCount, mMaxSelectCount));
            mVideoSelectAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClickItem(View.OnClickListener onClickListener, int postion) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            if (requestCode == CODE_CAMERA_ALL) {

                VideoData.VideoSingData(SelectVideoActivity.this, videoUri, new OnDataCompleteCall<VideoInfoEntity>() {
                    @Override
                    public void DataFinish(Context context, VideoInfoEntity videoInfoEntity) {
                        if (videoInfoEntity != null) {
                            mSelectCount++;
                            mSelectedVideoInfoEntities.add(videoInfoEntity);
                            videoInfoEntity.setSelectCount(mSelectCount);
                            videoInfoEntity.setSelected(true);
                            mVideoInfoEntities.add(videoInfoEntity);
                            mVideoSelectAdapter.setData(mVideoInfoEntities);
                        }
                    }
                });
            }else  if(requestCode==CODE_CAMERA_SELECT){
                VideoData.VideoSingData(SelectVideoActivity.this, videoUri, new OnDataCompleteCall<VideoInfoEntity>() {
                    @Override
                    public void DataFinish(Context context, VideoInfoEntity videoInfoEntity) {
                        if (videoInfoEntity != null) {
                            mSelectCount++;
                            mSelectedVideoInfoEntities.add(videoInfoEntity);
                            videoInfoEntity.setSelectCount(mSelectCount);
                            videoInfoEntity.setSelected(true);
                            mVideoInfoEntities.add(videoInfoEntity);
                            //加载数据
                            ArrayList<VideoInfoEntity> videoInfoEntities=new ArrayList<>();
                            videoInfoEntities.add(new VideoInfoEntity());
                            for (VideoInfoEntity videoInfoEntity2:mSelectedVideoInfoEntities){
                                videoInfoEntities.add(videoInfoEntity2);
                            }
                            mVideoSelectAdapter.setData(videoInfoEntities);
                        }
                    }
                });
            }
        }
    }

    private void changMaxSelectTip(int selectCount, int i) {
        for (VideoInfoEntity videoInfoEntity:mVideoInfoEntities){
            if(videoInfoEntity.isSelected()){
                if(videoInfoEntity.getSelectCount()>selectCount){
                    videoInfoEntity.setSelectCount(videoInfoEntity.getSelectCount()-i);
                }
            }
        }
    }

    //设置图片尺寸
    private  void setDrawableSize(TextView radioButton, int id, boolean b){
        if(b) {
            Drawable drawable = getResources().getDrawable(id);
            drawable.setBounds(0, 0, DensityUtil.dip2px(this, 15), DensityUtil.dip2px(this, 15));
            radioButton.setCompoundDrawables(null, null, drawable, null);
        }else {
            radioButton.setCompoundDrawables(null,null,null,null);
        }
    }

    private void setSelectStatus(boolean isSelectAll){
        if(isSelectAll){
            mSelectMoreAll.setTextColor(getResources().getColor(R.color.white));
            setDrawableSize(mSelectMoreAll,R.drawable.icon_arrow_down,true);
            mSelectMoreSelected.setTextColor(getResources().getColor(R.color.grey600));
            setDrawableSize(mSelectMoreSelected,R.drawable.icon_arrow_down,false);
            mVideoSelectAdapter.setData(mVideoInfoEntities);
        }else {
            mSelectMoreAll.setTextColor(getResources().getColor(R.color.grey600));
            setDrawableSize(mSelectMoreAll,R.drawable.icon_arrow_down,false);
            mSelectMoreSelected.setTextColor(getResources().getColor(R.color.white));
            setDrawableSize(mSelectMoreSelected,R.drawable.icon_arrow_down,true);
            //添加第一组临时数据
            ArrayList<VideoInfoEntity> videoInfoEntities=new ArrayList<>();
            videoInfoEntities.add(new VideoInfoEntity());
            for (VideoInfoEntity videoInfoEntity:mSelectedVideoInfoEntities){
                videoInfoEntities.add(videoInfoEntity);
            }
            mVideoSelectAdapter.setData(videoInfoEntities);
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                fileUri = FileProvider.getUriForFile(
                        this,
                        getPackageName() + ".fileprovider",
                        createMediaFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            try {
                fileUri = Uri.fromFile(createMediaFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if(isSelectAll)
        startActivityForResult(intent, CODE_CAMERA_ALL);
        else
        startActivityForResult(intent, CODE_CAMERA_SELECT);
    }

    private File createMediaFile() throws IOException {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "CameraDemo");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = TimeUitl.getTimeString(new Date().getTime(),"yyyyMMdd_HHmmss");
        String imageFileName = "VID_" + timeStamp;
        String suffix = ".mp4";
        videoUri=mediaStorageDir + File.separator + imageFileName + suffix;
        File mediaFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
        return mediaFile;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(mType==RESELECT_CLIP){
            Intent intent = new Intent(this, VideoClipActivity.class);
            intent.putExtra("selectvideos", mSelectedVideoInfoEntities);
            startActivity(intent);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}