package com.example.vaporwaveVideo.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.adapter.CardPagerAdapter;
import com.example.vaporwaveVideo.dialog.ShowDialogSet;
import com.example.vaporwaveVideo.entity.HomeModeEntity;
import com.example.vaporwaveVideo.interfacecall.OnHomeViewpagerCall;
import com.example.vaporwaveVideo.util.DensityUtil;
import com.example.vaporwaveVideo.util.WindowsUtil;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;


public class MainActivity extends AppCompatActivity implements View.OnClickListener ,EasyPermissions.PermissionCallbacks{
    private static final float SCALE = 0.9f;
    public static final int MBOTTOM_EDIT =12;
    public static final int MBOTTOM_CLIP =13;
    public static final int MBOTTOM_MODE = 14;
    private static int  mTemplateWhide;
    private  RadioGroup mBottomMenuRadio;
    private  RadioButton mRadioButtonClip;
    private  RadioButton mRadioButtonEdit;
    private  RadioButton mRadioButtonSet;
    private RecyclerView mTemplateRecycle;
    private  SurfaceView mShowHomeVideoBg;
    private ImageView mTemplateShowImg;
    private  CardPagerAdapter mCardAdapter;

    String PERMISSION_STORAGE_MSG = "请授予权限，否则影响部分使用功能";
    int PERMISSION_STORAGE_CODE = 10001;
    String[] PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};


    private  ShadowTransformer mCardShadowTransformer;
    private ArrayList<HomeModeEntity> homeModeEntities=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowsUtil.setTransparentStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        permissionCheck();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,this);
    }

    private void permissionCheck() {
        if (EasyPermissions.hasPermissions(this, PERMS)) {
            // 已经申请过权限，做想做的事
            initData();
            initShow();
        } else {
            // 没有申请过权限，现在去申请
            EasyPermissions.requestPermissions(this, PERMISSION_STORAGE_MSG, PERMISSION_STORAGE_CODE, PERMS);
        }
    }

    private void initData() {
        homeModeEntities.add(new HomeModeEntity(R.drawable.template1_small,1,true));
        homeModeEntities.add(new HomeModeEntity(R.drawable.template2_small,2,false));
        homeModeEntities.add(new HomeModeEntity(R.drawable.template3_small,3,false));
        homeModeEntities.add(new HomeModeEntity(R.drawable.template4_small,4,false));
        homeModeEntities.add(new HomeModeEntity(R.drawable.template5_small,5,false));
    }

    private void initView() {
        mBottomMenuRadio=findViewById(R.id.bottom_meun_radio);
        mRadioButtonClip=findViewById(R.id.radio_button_clip);
        mRadioButtonEdit=findViewById(R.id.radio_button_edit);
        mRadioButtonSet=findViewById(R.id.radio_button_set);
        mTemplateRecycle=findViewById(R.id.Template_RecycleView);
       mShowHomeVideoBg=findViewById(R.id.show_home_video_bg);
       mTemplateShowImg=findViewById(R.id.template_show_img);
    }

    private void initShow() {
        setDrawableSize(mRadioButtonClip,R.drawable.home_icon_clip);
        setDrawableSize(mRadioButtonEdit,R.drawable.home_icon_edit);
        setDrawableSize(mRadioButtonSet,R.drawable.home_icon_set);
        mShowHomeVideoBg.setBackground(getResources().getDrawable(R.drawable.home_pic));
        mTemplateShowImg.setBackground(getResources().getDrawable(R.drawable.template1));

        mRadioButtonClip.setOnClickListener(this);
        mRadioButtonEdit.setOnClickListener(this);
        mRadioButtonSet.setOnClickListener(this);

        mCardAdapter=new CardPagerAdapter(this,homeModeEntities);
        mCardShadowTransformer=new ShadowTransformer();
        mTemplateRecycle.setAdapter(mCardAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
       mTemplateRecycle.setLayoutManager(linearLayoutManager);
        mCardAdapter.setOnHomeViewpagerCall(new OnHomeViewpagerCall() {
            @Override
            public void onSelectedTemapter(HomeModeEntity homeModeEntity) {
                Intent intent=new Intent(MainActivity.this, SelectVideoActivity.class);
                        intent.putExtra("maxCount",1);
                        intent.putExtra("type",MBOTTOM_MODE);
                        intent.putExtra("data",homeModeEntity);
                        startActivity(intent);
            }
        });

        mTemplateRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
           @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                   LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int firstCompletelyVisibleItemPosition = l.findFirstVisibleItemPosition();
                    solerToNow(firstCompletelyVisibleItemPosition % homeModeEntities.size());

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==SCROLL_STATE_IDLE){
                    SlidingEdgeAdjustment();
                 }
            }
        });

    }

    private void SlidingEdgeAdjustment() {
        FrameLayout frameLayout= (FrameLayout) mTemplateRecycle.getChildAt(0);
//        Log.e("TAG", "onScrolled:   x="+frameLayout.getX()+"  y="+frameLayout.getY()+"  w="+frameLayout.getWidth()+" h="+frameLayout.getHeight() );
        if(frameLayout.getWidth()/2+frameLayout.getWidth()/4<Math.abs(frameLayout.getX())){
            mTemplateRecycle.scrollBy((int)(frameLayout.getWidth()+frameLayout.getX()),0);
        }else {
            mTemplateRecycle.scrollBy((int) frameLayout.getX(), 0);
        }
    }

    //设置图片尺寸
    private  void setDrawableSize(RadioButton radioButton,int id){
        Drawable drawable=getResources().getDrawable(id);
        drawable.setBounds(0,0, DensityUtil.dip2px(this,30),DensityUtil.dip2px(this,30));
        radioButton.setCompoundDrawables(null,drawable,null,null);
    }

    class  ShadowTransformer implements ViewPager.PageTransformer{
        @Override
        public void transformPage(@NonNull View page, float position) {
            if(position>=-1&&position<=1){
                float scale=SCALE+(1-SCALE)*(1-Math.abs(position));
                page.setScaleX(scale);
                page.setScaleY(scale);
            }else {
                page.setScaleX(SCALE);
                page.setScaleY(SCALE);
            }
        }

    }
    //滑动到当前位置
    public void solerToNow(int position){
        HomeModeEntity homeModeEntity=homeModeEntities.get(position);
        for (int i=0;i<homeModeEntities.size();i++){
            homeModeEntities.get(i).setSelect(i==position);
        }
        mTemplateShowImg.setBackground(getResources().getDrawable(getResources().getIdentifier("template"+homeModeEntity.getType(),"drawable",MainActivity.this.getPackageName())));
        mCardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(MainActivity.this, SelectVideoActivity.class);
        switch (view.getId()){
            case R.id.radio_button_clip:
                intent.putExtra("maxCount",5);
                intent.putExtra("type",MBOTTOM_CLIP);
                startActivity(intent);
                break;
            case R.id.radio_button_edit:
                intent.putExtra("maxCount",1);
                intent.putExtra("type",MBOTTOM_EDIT);
                startActivity(intent);
                break;
            case R.id.radio_button_set:
                ShowDialogSet showDialogSet=new ShowDialogSet(MainActivity.this);
                showDialogSet.initDialog(R.layout.dialog_set_view);
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        initData();
        initShow();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //拒绝后再次申请
        String[] perms2=new String[perms.size()];
        for (int i=0;i<perms.size();i++)perms2[i]=perms.get(i);
        EasyPermissions.requestPermissions(MainActivity.this, PERMISSION_STORAGE_MSG, PERMISSION_STORAGE_CODE, perms2);
    }
}