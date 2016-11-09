package com.zydemo.AndroidAnimationDemo.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * @author zyascend
 */

public class Ball {

    private static final int DEFAULT_RADIUS = 18;
    private static final int DEFAULT_COLOR = Color.WHITE;

    private float radius;
    private int color;
    private Paint paint;
    private float x;
    private float y;
    private float baseRadius;


    public Ball() {
        this.color = DEFAULT_COLOR;
        this.baseRadius = DEFAULT_RADIUS;
        this.radius = baseRadius;
        initPaint();
    }

    public Ball(float radius, int color) {
        this.baseRadius = Math.max(DEFAULT_RADIUS,radius);
        this.color = color;
        this.radius = baseRadius;
        initPaint();
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
    }

    public void setPosition(float x,float y){
        this.x = x;
        this.y = y;

    }

    public void drawSelf(Canvas canvas){
        canvas.drawCircle(x,y,radius,paint);
        Log.d(TAG, "drawSelf: x= "+ x +" y = "+y);
    }

    public void setRadius(float value, int i) {
        if (i == 0){
            //从大到小
            radius = baseRadius * (1.5f - 0.5f * value);
        }else {
            //从小到大
            radius = baseRadius * (1.0f + 0.5f * value);
        }
    }

}
