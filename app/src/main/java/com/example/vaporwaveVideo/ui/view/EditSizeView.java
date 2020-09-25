package com.example.vaporwaveVideo.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.vaporwaveVideo.R;

public class EditSizeView extends View {
    private float size = 1;
    private int textColor;
    private boolean isSelect=false;
    private RectF mDstRect = new RectF();
    Paint paint = new Paint();

    public EditSizeView(Context context) {
        this(context, null);
    }

    public EditSizeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditSizeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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


    private void initSizeParams() {
        if(size == 0)return;
        int padding = 20;
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
    }

    private void init() {
        if(isSelect){
            textColor=R.color.white;
        }else {
          textColor=R.color.grey600;
        }
        paint.setColor(getResources().getColor(textColor));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(mDstRect, paint);
    }

    public void setSelect(boolean select) {
        isSelect = select;
        if(isSelect){
            textColor=R.color.white;
        }else {
            textColor=R.color.grey600;
        }
        paint.setColor(getResources().getColor(textColor));
        invalidate();
    }
}
