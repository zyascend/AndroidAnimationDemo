package com.zydemo.AndroidAnimationDemo.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

/**
 *
 * Created by zyascend on 2016/9/21.
 */

public class RectangleAnimation extends RelativeLayout {

    private static final String COLOR_RIGHT = "#FF1956";
    private static final String COLOR_LEFT = "#FF890E";

    private RectangleView[] rectangles;
    private PointF[] positions ;
    private float mSize;
    private PointF center;

    private ValueAnimator timeAnim;
    private RectangleView t;

    public RectangleAnimation(Context context) {
        super(context);
        init(context);
    }

    public RectangleAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RectangleAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        rectangles = new RectangleView[6];
        positions = new PointF[7];
        for (int i = 0; i < 6; i++) {
            rectangles[i] = new RectangleView(context);
            if (i<= 2 && i>=0){
                rectangles[i].setColor(COLOR_LEFT);
            }else {
                rectangles[i].setColor(COLOR_RIGHT);
            }
        }
        for (int i = 0; i < 7; i++) {
            positions[i] = new PointF();
        }
        center = new PointF();

        mSize = rectangles[0].getSize();

        timeAnim = ValueAnimator.ofFloat(0,8);
        timeAnim.setInterpolator(new LinearInterpolator());
        timeAnim.setDuration(2400);
        timeAnim.setRepeatMode(ValueAnimator.RESTART);
        timeAnim.setRepeatCount(ValueAnimator.INFINITE);
        timeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentTime = (float) animation.getAnimatedValue();
                if (0 <= currentTime && currentTime <= 1){
                    //第1段，方块0移向点0
                    moveToPosition(0,0);
                } else if (1 < currentTime && currentTime <= 2){
                    //第2段，方块1移向点1
                    moveToPosition(1,1);
                }else if (2 < currentTime && currentTime <= 3){
                    //第3段，方块2移向点2
                    moveToPosition(2,2);
                }else if (3 < currentTime && currentTime <= 4){
                    //第4段，方块0移向点3
                    moveToPosition(0,3);
                }else if (4 < currentTime && currentTime <= 5){
                    //第5段，方块3移向点0
                    moveToPosition(3,0);
                }else if (5 < currentTime && currentTime <= 6){
                    //第6段，方块4移向点4
                    moveToPosition(4,4);
                }else if (6 < currentTime && currentTime <= 7){
                    //第7段，方块5移向点5
                    moveToPosition(5,5);
                }else if (7 < currentTime && currentTime <= 8){
                    //第8段，方块3移向点6
                    moveToPosition(3,6);
                }
            }
        });

        timeAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
                //在动画重复时为每个点重新标号
                t = rectangles[0];
                rectangles[0] = rectangles[1];
                rectangles[1] = rectangles[2];
                rectangles[2] = t;

                t = rectangles[3];
                rectangles[3] = rectangles[4];
                rectangles[4] = rectangles[5];
                rectangles[5] = t;

            }
        });
    }

    private void moveToPosition(final int view, final int target) {

        //方块移动的动画
        ObjectAnimator animX = ObjectAnimator.ofFloat(rectangles[view],"x",positions[target].x);
        ObjectAnimator animY = ObjectAnimator.ofFloat(rectangles[view],"y",positions[target].y);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.playTogether(animX,animY);
        set.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        center.x = w/2;
        center.y = h/2;

        float viewOffset = 20;
        float pathLength = (float) ((mSize + viewOffset)/Math.sqrt(2));

        //配置每一个点
        positions[0] = center;

        positions[1].set(center.x - pathLength,center.y-pathLength);
        positions[3].set(center.x - pathLength,center.y+pathLength);
        positions[2].set(center.x - pathLength*2,center.y);

        positions[4].set(center.x + pathLength,center.y-pathLength);
        positions[6].set(center.x + pathLength,center.y+pathLength);
        positions[5].set(center.x + pathLength*2,center.y);

        //为每一个方块设置初始位置
        for (int i = 0; i < 6; i++) {
            rectangles[i].setX(positions[i+1].x);
            rectangles[i].setY(positions[i+1].y);
            addView(rectangles[i]);
        }

    }

    public void startAnim(){
        if (timeAnim != null){
            timeAnim.start();
        }
    }

    public void stopAnim() {
        if (timeAnim != null){
            timeAnim.cancel();
        }
    }
}
