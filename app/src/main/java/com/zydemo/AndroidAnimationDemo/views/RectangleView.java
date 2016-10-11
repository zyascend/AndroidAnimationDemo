package com.zydemo.AndroidAnimationDemo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 * Created by zyascend on 2016/9/21.
 */

public class RectangleView extends View {

    private Paint mPaint;

    private String mColor = "#FF5400";
    private float mSize = 80;
    private PointF center;
    private RectF rectF;

    public RectangleView(Context context) {
        super(context);
        init();
    }

    public RectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RectangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }



    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor(mColor));
        // 设置结合处为圆弧
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        // 设置画笔样式为圆弧
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setShadowLayer(5,5,5,Color.GRAY);

        mPaint.setPathEffect(new CornerPathEffect(20));

        center = new PointF();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (mSize * Math.sqrt(2) + 10);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        center.set(w/2,h/2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        rectF = new RectF(-mSize/2,-mSize/2,mSize/2,mSize/2);
        mPaint.setColor(Color.parseColor(mColor));
        canvas.translate(center.x,center.y);
        canvas.rotate(45);
        canvas.drawRoundRect(rectF,5,5,mPaint);
    }

    public void setColor(String color){
        mColor = color;
        invalidate();
    }

    public void setRectangleSize(float size){
        mSize = size;
        invalidate();
    }

    public float getSize() {
        return mSize;
    }
}
