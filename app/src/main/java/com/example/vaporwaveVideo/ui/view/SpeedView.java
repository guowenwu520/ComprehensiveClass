package com.example.vaporwaveVideo.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.interfacecall.OnSpeedChangCall;
import com.example.vaporwaveVideo.util.DensityUtil;

public class SpeedView extends View  {
    private Paint paint;
    private int textColor,lineColor,circularColor;
    private float nowSpeed=1.0f;
    private int wdith,height;
    private int [] verticalPosition,lineLists;
    private float textSize=12;
    private OnSpeedChangCall onSpeedChangCall;
    //控制滑动块长度
    private int Ratio=4;
    //圆的大小
    private int adius=15;
    //圆的位置
    private int circularPostion=0;

    public SpeedView(Context context) {
        this(context,null);
    }

    public SpeedView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SpeedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initSizeParams();
    }

    private void initSizeParams() {
         wdith=getWidth();
         height=getHeight();
        verticalPosition=new int[]{
                height/4,height/2,height-height/4
        };
        int startWdith=wdith/Ratio;
        int intervalWidth=wdith/(Ratio*10);
        lineLists=new int[]{
            startWdith/2,intervalWidth,verticalPosition[1],25,15
        };
    }

    public void setOnSpeedChangCall(OnSpeedChangCall onSpeedChangCall) {
        this.onSpeedChangCall = onSpeedChangCall;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(lineColor));
        paint.setTextSize(DensityUtil.dip2px(getContext(),textSize));
        canvas.drawText(nowSpeed+"X",wdith/2-25,verticalPosition[0],paint);
        for (int i=0;i<=30;i++){
            if(i==0||i==10||i==20||i==30){
                paint.setColor(getResources().getColor(textColor));
                canvas.drawLine(lineLists[0]+(lineLists[1]*i),lineLists[2]-lineLists[3],lineLists[0]+(lineLists[1]*i),lineLists[2]+lineLists[3],paint);
                float showtext=0.5f;
                switch (i){
                    case 10:
                        showtext=1.0f;
                        break;
                    case 20:
                        showtext=1.5f;
                        break;
                    case 30:
                        showtext=2.0f;
                        break;
                }
                canvas.drawText(showtext+"X",lineLists[0]+(lineLists[1]*i)-25,verticalPosition[2],paint);
            }else {
                paint.setColor(getResources().getColor(lineColor));
                canvas.drawLine(lineLists[0]+(lineLists[1]*i),lineLists[2]-lineLists[4],lineLists[0]+(lineLists[1]*i),lineLists[2]+lineLists[4],paint);
            }
        }
        paint.setColor(getResources().getColor(circularColor));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        if(circularPostion==0) {
            canvas.drawCircle(lineLists[0]+lineLists[1]*10, lineLists[2], adius, paint);
        }else {
            canvas.drawCircle(circularPostion, lineLists[2], adius, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return onTouch(this,event);
    }



    public boolean onTouch(View v, MotionEvent event) {
        float wdith=event.getX();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //禁止父控件拦截
                getParent().requestDisallowInterceptTouchEvent(true);
                if(wdith>lineLists[0]&&wdith<lineLists[0]+lineLists[1]*30){
                    circularPostion= (int) wdith;
                   double bilei=(circularPostion-lineLists[0])*1.0/lineLists[1];
                   nowSpeed =Float.parseFloat(String.format("%.2f",bilei *0.05+0.5));
                   if(onSpeedChangCall!=null)onSpeedChangCall.speedChang(nowSpeed);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }


    private void init() {
        textColor= R.color.white;
        lineColor=R.color.grey600;
        circularColor=R.color.colorAccent;
        paint=new Paint();
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
    }
}
