package com.zydemo.AndroidAnimationDemo.views;

import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;

/**
 *
 * @author zyascend
 */

public class PathAnimation implements ValueAnimator.AnimatorUpdateListener {
    private int ballSize;
    private Path path;
    private PathMeasure pathMeasure;
    private long duration;
    private ValueAnimator valueAnimator;
    private ArrayList<Ball> balls;
    private float lenth;

    public void setPathAnimationListener(PathAnimationListener pathAnimationListener) {
        this.pathAnimationListener = pathAnimationListener;
    }

    private PathAnimationListener pathAnimationListener;


    public PathAnimation(Path path, long duration,ArrayList<Ball> ballArrayList) {
        this.path = path;
        this.duration = duration;
        this.balls = ballArrayList;
        ballSize = ballArrayList.size();

        init();
    }

    private void init() {
        pathMeasure = new PathMeasure(path,false);
        lenth = pathMeasure.getLength();
        valueAnimator = ValueAnimator.ofFloat(0,2f);
        valueAnimator.setDuration(duration);
//        valueAnimator.setStartDelay(300);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(this);

    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float value = (float) animation.getAnimatedValue();
//

//        if (value>= 1f){
//            balls.get(0).resetRadius();
//            balls.get(ballSize - 1).setRadius(value-1,1);
//        }else {
//            balls.get(ballSize - 1).resetRadius();
//            balls.get(0).setRadius(value,0);
//        }
//        if (0 <= value && value < 1f) {
//            balls.get(0).setRadius(value, 0);
//        }
//        }else {
//            value = value-1;
//            balls.get(ballSize-1).setRadius(value,1);
//        }

        for (int i = 0; i < ballSize; i++) {
            updateBalls(balls.get(i),value,i);

        }
        if (pathAnimationListener != null) {
            pathAnimationListener.onUpdate();
        }
    }

    private void updateBalls(Ball ball, float value, int i) {
        float[] pointF = new float[2];
        if (0 <= value && value < 1f){
            ball.setRadius(value,0);
            pathMeasure.getPosTan(lenth*value*i/ballSize, pointF, null);
        }else {
            value = value-1;
            ball.setRadius(value,1);
            pathMeasure.getPosTan(getDistance(value,i), pointF, null);
        }
        ball.setPosition(pointF[0],pointF[1]);
    }

    private float getDistance(float value, int i) {

        float singleDistance = lenth/ballSize;
        float startDistance = i*singleDistance;
        return startDistance + (lenth - startDistance) * value;
    }

    public void start() {
        valueAnimator.start();
    }

    public void stop(){
        valueAnimator.end();
        valueAnimator.cancel();

    }


    interface PathAnimationListener{
        void onUpdate();
    }
}
