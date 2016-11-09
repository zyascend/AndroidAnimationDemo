package com.zydemo.AndroidAnimationDemo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.zydemo.AndroidAnimationDemo.R;

import java.util.ArrayList;

/**
 * @author zyascend
 */

public class ConvergeLoader extends View implements PathAnimation.PathAnimationListener {

    private static final int DEFAULT_PATH_RADIUS = 80;
    private static final int DEFAULT_CIRCLE_RADIUS = 18;
    private static final int DEFAULT_COUNT = 7;
    private static final int DEFAULT_DURATION = 2000;


    private int count;
    private Path path;
    private ArrayList<Ball> balls;
    private float pathRadius;
    private long duration;
    private int color;
    private float radius;
    private PathAnimation pathAnimation;
    private Context mContext;


    public ConvergeLoader(Context context) {
        this(context,null);

    }

    public ConvergeLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initAttrs(attrs);

    }

    private void initAttrs(AttributeSet attrs) {

        TypedArray array = mContext.obtainStyledAttributes(attrs,R.styleable.ConvergeLoader);
        radius = array.getDimension(R.styleable.ConvergeLoader_circleRadius,Dp2Px(5));
        color = array.getColor(R.styleable.ConvergeLoader_circleColor, Color.WHITE);
        count = array.getInteger(R.styleable.ConvergeLoader_circleCount,DEFAULT_COUNT);
        if (count <= 1)
            count = DEFAULT_COUNT;
        duration = (long) array.getInteger(R.styleable.ConvergeLoader_duration,DEFAULT_DURATION);
        pathRadius = array.getDimension(R.styleable.ConvergeLoader_pathRadius,Dp2Px(40));
        pathRadius = Math.max(pathRadius,DEFAULT_PATH_RADIUS);
        array.recycle();

        path = new Path();
        balls = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Ball ball = new Ball(radius,color);
            balls.add(ball);
            //可以进行设置
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        path.addCircle(w/2,h/2,pathRadius, Path.Direction.CW);
        pathAnimation = new PathAnimation(path,duration,balls);
        pathAnimation.setPathAnimationListener(this);
        start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = (int) (pathRadius * 2 * 1.5f);
        setMeasuredDimension(width,width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(-90,getMeasuredWidth()/2,getMeasuredHeight()/2);
        for (Ball ball : balls) {
            ball.drawSelf(canvas);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    public void onUpdate() {
        invalidate();
    }

    public void stop() {
        pathAnimation.stop();
    }

    public void start(){
        pathAnimation.start();
    }

    public int Dp2Px(int dpi) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, mContext.getResources().getDisplayMetrics());
    }

    public int Sp2Px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, mContext.getResources().getDisplayMetrics());
    }
}
