package com.example.vaporwaveVideo.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.interfacecall.OnSizeChangCall;
import com.xiaopo.flying.sticker.StickerView;

public class TailoringSizeView extends FrameLayout {
    private float size = 1;
    private int textColor;
    Bitmap leftbitmap,rightbitmap;
    private OnSizeChangCall onSizeChangCall;
    private int padding=50,radius=20,bitmapRadius,mode=1;
    float leftCircularX,leftCircularY,rightCircularX,rightCircularY;
    private RectF mDstRect = new RectF();
    Paint paint = new Paint();
    float x=0,y = 0,top,bottom,left,right,oldDistance;
    float mWdith,mHeight;
    public TailoringSizeView(Context context) {
        this(context, null);
    }

    public TailoringSizeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TailoringSizeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initSizeParams();
    }

    public void setSizeRatio(float sizeRatio) {
        this.size = sizeRatio;
        if (getWidth() > 0 && getHeight() > 0) {
            initSizeParams();
        }
    }

    public void setPadding(int padding) {
        this.padding = padding;
        initSizeParams();
        invalidate();
    }

    public void setOnSizeChangCall(OnSizeChangCall onSizeChangCall) {
        this.onSizeChangCall = onSizeChangCall;
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                mode=1;
                x=event.getX();
                y=event.getY();
                left=mDstRect.left;
                right=mDstRect.right;
                top=mDstRect.top;
                bottom=mDstRect.bottom;
                if(x>leftCircularX-bitmapRadius&&x<leftCircularX+bitmapRadius&&y>leftCircularY-bitmapRadius&&y<leftCircularY+bitmapRadius){
                    onSizeChangCall.delete();
                }else if(x>rightCircularX-bitmapRadius&&x<rightCircularX+bitmapRadius&&y>rightCircularY-bitmapRadius&&y<rightCircularY+bitmapRadius){
                    onSizeChangCall.chang(mWdith,mHeight,left,top);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode=2;
                float xh = event.getX(0) - event.getX(1);
                float yh= event.getY(0) - event.getY(1);
                oldDistance= (float) Math.sqrt(xh * xh + yh * yh);
                left=mDstRect.left;
                right=mDstRect.right;
                top=mDstRect.top;
                bottom=mDstRect.bottom;
                break;
            case MotionEvent.ACTION_MOVE:
                //一更手指按下
                if(mode==1) {
                    moveSize(event.getX(0) - x, event.getY(0) - y);
                }else {
                    float xh2= event.getX(0) - event.getX(1);
                    float yh2= event.getY(0) - event.getY(1);
                   float newDistance= (float) Math.sqrt(xh2 * xh2 + yh2 * yh2);
                   sizeZoom((newDistance-oldDistance)/2);
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                mode=1;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                x=event.getX(0);
                y=event.getY(0);
                left=mDstRect.left;
                right=mDstRect.right;
                top=mDstRect.top;
                bottom=mDstRect.bottom;
              mode=1;

                break;
        }
        return true;
    }

    private void sizeZoom(float v) {
        float mv;
        mv=v*size;
        if(left-mv<0||right+mv>getWidth()||top-v<0||bottom+v>getHeight())return;
        mDstRect.left=left-mv;
        mDstRect.right=right+mv;
        mDstRect.top=top-v;
        mDstRect.bottom=bottom+v;
        mWdith=mDstRect.right-mDstRect.left;
        mHeight=mDstRect.bottom-mDstRect.top;
        leftCircularX=mDstRect.left;
        leftCircularY=mDstRect.top;
        rightCircularX=mDstRect.right;
        rightCircularY=mDstRect.top;
    }

    private void moveSize(float x, float y) {
        if(left+x<0){
            mDstRect.left=0;
        }else {
            if(right+x>getWidth()){
                mDstRect.left=getWidth()-mWdith;
            }else {
                mDstRect.left = left+x;
            }
        }
        if(right+x>getWidth()){
            mDstRect.right=getWidth();
        }else {
            if(left+x<0){
                mDstRect.right = mWdith;
            }else {
                mDstRect.right = right+x;
            }
        }
        if(top+y<0){
            mDstRect.top =0;
        }else {
            if(bottom+y>getHeight()){
                mDstRect.top =getHeight()-mHeight;
            }else {
                mDstRect.top =top+y;
            }
        }
        if(bottom+y>getHeight()){
            mDstRect.bottom=getHeight();
        }else {
            if(top+y<0){
                mDstRect.bottom =mHeight;
            }else {
                mDstRect.bottom =bottom+y;
            }
        }
        leftCircularX=mDstRect.left;
        leftCircularY=mDstRect.top;
        rightCircularX=mDstRect.right;
        rightCircularY=mDstRect.top;
    }

    private void initSizeParams() {
        if(size == 0)return;
        int drawWidth = getWidth() - padding * 2;
        int drawHeight = getHeight() - padding * 2;
        if (size >= 1) {
            mDstRect.left = padding;
            mDstRect.right = getWidth() - padding;
            float rtHeight = drawWidth / size;
            mDstRect.top = (getHeight() - rtHeight) / 2;
            mDstRect.bottom = mDstRect.top + rtHeight;

        } else {
            mDstRect.top = padding;
            mDstRect.bottom = getHeight() - padding;
            float rtWidth = drawHeight * size;
            mDstRect.left = (getWidth() - rtWidth) / 2;
            mDstRect.right = mDstRect.left + rtWidth;
        }
        leftCircularX=mDstRect.left;
        leftCircularY=mDstRect.top;
        rightCircularX=mDstRect.right;
        rightCircularY=mDstRect.top;
        leftbitmap= BitmapFactory.decodeResource(getResources(), R.drawable.icon_delete);
        rightbitmap= BitmapFactory.decodeResource(getResources(), R.drawable.icon_hook);
        bitmapRadius=rightbitmap.getHeight()/2;
        mWdith=mDstRect.right-mDstRect.left;
        mHeight=mDstRect.bottom-mDstRect.top;
    }

    private void init() {
        textColor= R.color.white;
        paint.setColor(getResources().getColor(textColor));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
    }
//    Paint paint2 = new Paint(){
//        {
//            setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        }
//    };
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        //清空之前画布内容
//        canvas.drawPaint(paint2);

        paint.setStrokeWidth(4);
        canvas.drawRect(mDstRect, paint);
        float gewdith=mWdith/4;
        float geheight=mHeight/4;
        paint.setStrokeWidth(2);
        for (int i=1;i<4;i++){
            canvas.drawLine(mDstRect.left+gewdith*i,mDstRect.top,mDstRect.left+gewdith*i,mDstRect.bottom,paint);
            canvas.drawLine(mDstRect.left,mDstRect.top+geheight*i,mDstRect.right,mDstRect.top+geheight*i,paint);
        }
        canvas.drawBitmap(leftbitmap,leftCircularX-bitmapRadius,leftCircularY-bitmapRadius,paint);
        canvas.drawBitmap(rightbitmap,rightCircularX-bitmapRadius,rightCircularY-bitmapRadius,paint);
    }
}
