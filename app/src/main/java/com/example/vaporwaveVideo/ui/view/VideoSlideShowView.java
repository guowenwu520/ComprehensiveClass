package com.example.vaporwaveVideo.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.interfacecall.OnSizeChangCall;
import com.example.vaporwaveVideo.interfacecall.OnSlideTimeCall;

import java.util.ArrayList;

public class VideoSlideShowView  extends View {
    private int textColor,sdileWidth=20;
    private   int distanceLeft=0,distanceRight=0,leftX1,leftX2,rightX1,rightX2;
    private final  int CLICK_LEFT=2,CLICK_RIGHT=0,CLICK_NONE=9;
    float mWdith,mHeight,biwdith;
    private OnSlideTimeCall onSlideTimeCall;
    long allTime;
    ArrayList<Bitmap> bitmaps=new ArrayList<>();
    Paint paint = new Paint();
    int  OnClickStatus;
    float x;
    Rect rect,leftRect,rightRect;
    public VideoSlideShowView(Context context) {
        this(context, null);
    }

    public VideoSlideShowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoSlideShowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initSizeParams(0,0);
    }

    private void initSizeParams(int left,int right) {
      mHeight=getHeight();
      mWdith=getWidth();

      rect=new Rect(sdileWidth+left,2,(int)(mWdith-sdileWidth)-right,(int)(mHeight-2));
      rightRect=new Rect((int)mWdith-right-sdileWidth,0,(int)mWdith-right,(int)(mHeight));
        leftRect=new Rect(left,0,sdileWidth+left,(int)(mHeight));
        leftX1=left;
        leftX2=25+left;
        rightX1=(int)mWdith-25-right;
        rightX2=(int)mWdith-right;
    }

    public void setOnSlideTimeCall(OnSlideTimeCall onSlideTimeCall) {
        this.onSlideTimeCall = onSlideTimeCall;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(bitmaps.size()<0)return;
        paint.setStyle(Paint.Style.STROKE);
        biwdith=(mWdith-sdileWidth*2)/bitmaps.size();
        for (int i=0;i<bitmaps.size();i++){
            Rect rect =new Rect(0,0,bitmaps.get(i).getWidth(),bitmaps.get(i).getHeight());
            Rect rectw =new Rect((int)(i*biwdith)+sdileWidth,0,(int)((i+1)*biwdith)+sdileWidth,(int)mHeight);
            canvas.drawBitmap(bitmaps.get(i),rect,rectw,paint);
        }
        canvas.drawRect(rect,paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(leftRect,paint);
        canvas.drawRect(rightRect,paint);
    }

    public void setBitmaps(ArrayList<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        invalidate();
    }

    public void setAllTime(long allTime) {
        this.allTime = allTime;
    }

    private void init() {
        textColor= R.color.yellow;
        paint.setColor(getResources().getColor(textColor));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
    }

    public void onTouch(MotionEvent event){
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
               x=event.getX();
               OnClickStatus=CLICK_NONE;
               if(x>=leftX1&&x<=leftX2){
                   OnClickStatus=CLICK_LEFT;
               }
               if(x>=rightX1&&x<=rightX2){
                   OnClickStatus=CLICK_RIGHT;
               }
                break;
            case MotionEvent.ACTION_MOVE:
                float nowX=event.getX();
                if(OnClickStatus==CLICK_RIGHT){
                    //边界或者交叉
                    if(distanceLeft<0||mWdith-(int)(distanceRight+(x-nowX))>mWdith||distanceLeft+distanceRight+(x-nowX)>mWdith-sdileWidth*2)return;
                        initSizeParams(distanceLeft,(int)(distanceRight+(x-nowX)));
                        onSlideTimeCall.endTime((long)(allTime*((mWdith-(distanceRight+(x-nowX)))/mWdith)));
                }else if(OnClickStatus==CLICK_LEFT){
                    if((int)(distanceLeft+(nowX-x))<0||distanceRight>mWdith||(distanceLeft+(nowX-x))+distanceRight>mWdith-sdileWidth*2)return;
                    initSizeParams((int)(distanceLeft+(nowX-x)),distanceRight);
                    onSlideTimeCall.startTime((long)(allTime*((distanceLeft+(nowX-x))/mWdith)));
                }else return;
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                float endX=event.getX();
                if(OnClickStatus==CLICK_RIGHT){
                    distanceRight+=(int) (x-endX);
                }else if(OnClickStatus==CLICK_LEFT) {
                    distanceLeft+=(int) (endX-x);
                }
                break;
        }
    }
}
