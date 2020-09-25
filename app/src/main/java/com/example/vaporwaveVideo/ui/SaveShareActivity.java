package com.example.vaporwaveVideo.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.adapter.SharePlatformAdapter;
import com.example.vaporwaveVideo.entity.VideoInfoEntity;
import com.example.vaporwaveVideo.util.AppUtils;
import com.example.vaporwaveVideo.util.WindowsUtil;

import static com.example.vaporwaveVideo.ui.MainActivity.MBOTTOM_CLIP;
import static com.example.vaporwaveVideo.ui.MainActivity.MBOTTOM_EDIT;

public class SaveShareActivity extends AppCompatActivity implements View.OnClickListener {
     private VideoInfoEntity saveVideo;
     private ImageView mCbBack,mCbRight,mClipBtn,mTemplateBtn,mIvContent;
     private RelativeLayout mLrClipTemplate;
     private RecyclerView mRecyclerSharePlatform;
     private TextView mTextTip;
    private SharePlatformAdapter platformAdapter;

    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowsUtil.setTransparentStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_share);
        saveVideo= (VideoInfoEntity) getIntent().getSerializableExtra("save");
        type=getIntent().getIntExtra("type",100);
        initView();
        initShow();
    }

    private void initShow() {

        if(type==MBOTTOM_CLIP){
            mLrClipTemplate.setVisibility(View.VISIBLE);
            mClipBtn.setImageResource(R.drawable.icon_edit);
            mTextTip.setText(getResources().getString(R.string.edit2));
        }else if(type==MBOTTOM_EDIT){
            mLrClipTemplate.setVisibility(View.VISIBLE);
            mClipBtn.setImageResource(R.drawable.icon_clip);
            mTextTip.setText(getResources().getString(R.string.clip2));
        }
        Glide.with(this).load(saveVideo.getData()).into(mIvContent);
        mIvContent.setAlpha(0.6f);
        mRecyclerSharePlatform.setLayoutManager(new GridLayoutManager(this,4));
        mClipBtn.setOnClickListener(this);
        mTemplateBtn.setOnClickListener(this);
        mCbRight.setOnClickListener(this);
        mCbBack.setOnClickListener(this);
        loadSharePlatform();
    }
    private void installSharePlatform() {
        mRecyclerSharePlatform.setAdapter(platformAdapter);
        platformAdapter.setOnItemClickListener((pos, value) -> {
            AppUtils.shareVideo(this, saveVideo.getData(), value.packageName);
        });
    }
    private void loadSharePlatform() {

        new Thread(() -> {
            platformAdapter = new SharePlatformAdapter();
            platformAdapter.setData(AppUtils.getAllSupportShareImageApp(getApplicationContext()));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  installSharePlatform();
                }
            });
        }).start();
    }
    private void initView() {
        mCbBack=findViewById(R.id.mcb_back);
        mCbRight=findViewById(R.id.mcb_right);
        mLrClipTemplate=findViewById(R.id.lr_clip_template);
        mTemplateBtn=findViewById(R.id.template_btn);
        mClipBtn=findViewById(R.id.clip_btn);
        mRecyclerSharePlatform=findViewById(R.id.recycler_share_platform);
        mTextTip=findViewById(R.id.text_tip);
        mIvContent=findViewById(R.id.iv_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mcb_back:
            case R.id.mcb_right:
            case R.id.template_btn:
                finish();
                break;
            case R.id.clip_btn:
                Intent intent=new Intent(SaveShareActivity.this, SelectVideoActivity.class);
                if(type==MBOTTOM_CLIP){
                    intent.putExtra("maxCount",5);
                }else if(type==MBOTTOM_EDIT){
                    intent.putExtra("maxCount",1);
                }
                intent.putExtra("type",type);
                startActivity(intent);
                finish();
                break;
        }
    }
}