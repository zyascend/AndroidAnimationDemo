package com.zydemo.AndroidAnimationDemo.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 *
 * Created by zyascend on 2016/9/24.
 */

public class DropAnimation extends View implements View.OnClickListener {


    private static final double PI = Math.PI;

    //箭矢贝塞尔曲线起始点，控制点，终止点
    private PointF center,start,end1,end2,contro1,contro2;

    //箭矢起始点与终止点的中点，用于确定控制点
    private PointF center_se1;
    private PointF center_se2;

    // √ 的三个点
    private PointF checkPoint1,checkPoint2,checkPoint3;

    //绘制箭头的角度
    private double startAngle = getPI(42);
    private double endAngle = getPI(75);
    private double b = getPI(15);
    private double currentAngle;
    private double getPI(float i) {
        return i * PI / 180;
    }

    private float radius = 100;
    //箭头下移距离
    private float maxCenterOff;
    //勾便宜中线的距离
    private float checkLeftOffset;
    //勾的直角边长度
    //见图片：http://p1.bpimg.com/4851/0905ffab90613329.png
    private float checkOffset;
    //动画执行进度
    private float time;
    // × 的直角边长度
    private float failOffset;
    //下载进度
    private float progress;
    //左右箭矢长度
    private float arcLength;
    //控制点的设置变量
    private float offsetX;
    private float offsetY;
    private float offSetControl;
    private float maxOffsetControl;
    //是否下载成功
    private boolean drawSuccess = true;
    //是否正在下载中
    private boolean isDownLoading;

    private Paint linePaint;
    private Paint fillPaint;
    private Paint arcPaint;

    private RectF rectF;

    private int width;
    private int height;

    //左右箭矢路径
    private Path leftPath,rightPath;
    private ValueAnimator animator;


    public DropAnimation(Context context) {
        super(context);
        init();
    }

    public DropAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DropAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnClickListener(this);
        isDownLoading = false;

        center = new PointF();
        start = new PointF();
        end1 = new PointF();
        end2 = new PointF();
        contro1 = new PointF();
        contro2 = new PointF();

        center_se1 = new PointF();
        center_se2 = new PointF();

        checkPoint1 = new PointF();
        checkPoint2 = new PointF();
        checkPoint3 = new PointF();

        rectF = new RectF();

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.WHITE);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(10);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeCap(Paint.Cap.ROUND);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        fillPaint.setStrokeWidth(10);
        fillPaint.setColor(Color.WHITE);
        fillPaint.setStrokeJoin(Paint.Join.ROUND);
        fillPaint.setStrokeCap(Paint.Cap.ROUND);


        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setColor(Color.WHITE);
        arcPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        arcPaint.setStrokeWidth(10);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeCap(Paint.Cap.ROUND);

        rightPath = new Path();
        leftPath = new Path();

        animator = ValueAnimator.ofFloat(0,5);
        animator.setDuration(3000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                time = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isDownLoading = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isDownLoading = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });



    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;

        maxCenterOff = radius * 1/3;
        arcLength = (float) (radius * PI / 6);
        maxOffsetControl = (float) (0.5 * arcLength * Math.tan(b));
        checkLeftOffset = radius/8;
        checkOffset = checkLeftOffset * 2;

        failOffset = radius/3;

        checkPoint1.set(w/2 - checkLeftOffset - checkOffset,h/2 + checkLeftOffset);
        checkPoint2.set(w/2 - checkLeftOffset,h/2 + checkOffset+ checkLeftOffset);
        checkPoint3.set(w/2 + 2*checkOffset-checkLeftOffset,h/2 - checkOffset+ checkLeftOffset);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (time <= 1){
            //第一阶段绘制箭头
            drawArrow(canvas);
        }else if (1 < time && time <= 2 ){
            //第二阶段绘制圆圈
            drawBorderArc(canvas);
        }else if (2 < time && time <= 4 ){
            //第三阶段绘制扇形
            canvas.drawCircle(center.x,center.y,radius,linePaint);
            canvas.drawArc(rectF,90, 180 * (time - 2),true,fillPaint);
        }else {
            //第四阶段绘制勾或者×
            linePaint.setColor(Color.BLUE);
            if (drawSuccess){

                drawComplete(canvas);
            }else {
                drawFail(canvas);
            }
            linePaint.setColor(Color.WHITE);
        }
    }

    private void drawFail(Canvas canvas) {
        canvas.drawCircle(center.x,center.y,radius,arcPaint);
        canvas.drawLine(center.x,center.y,center.x - (time - 4) * failOffset,center.y - (time - 4) * failOffset,linePaint);
        canvas.drawLine(center.x,center.y,center.x + (time - 4) * failOffset,center.y + (time - 4) * failOffset,linePaint);
        canvas.drawLine(center.x,center.y,center.x - (time - 4) * failOffset,center.y + (time - 4) * failOffset,linePaint);
        canvas.drawLine(center.x,center.y,center.x + (time - 4) * failOffset,center.y - (time - 4) * failOffset,linePaint);
    }

    private void drawComplete(Canvas canvas) {

        canvas.drawCircle(center.x,center.y,radius,arcPaint);
        if (time - 4 <= 0.4){
            canvas.drawLine(checkPoint1.x,checkPoint1.y
                    ,(float) (2.5 * (time - 4) * checkOffset + checkPoint1.x)
                    ,(float) (2.5 * (time - 4) * checkOffset + checkPoint1.y),linePaint);
        }else {
            canvas.drawLine(checkPoint1.x,checkPoint1.y,checkPoint2.x,checkPoint2.y,linePaint);
            canvas.drawLine(checkPoint2.x,checkPoint2.y
                    ,(float) (5/3 * (time - 4.4) * checkOffset*4 + checkPoint2.x)
                    ,(float) (checkPoint2.y - 5/3 * (time - 4.4) * checkOffset*4),linePaint);

        }

    }

    private void drawBorderArc(Canvas canvas) {

        float sweepAngle = (time - 1) * 150;
        rectF.set(center.x - radius,center.y - radius,center.x + radius,center.y + radius);
        canvas.drawLine(center.x,center.y,start.x,start.y,linePaint);
        canvas.drawArc(rectF,60,60,false,linePaint);
        canvas.drawArc(rectF,120, sweepAngle,false,linePaint);
        canvas.drawArc(rectF,60,-sweepAngle,false,linePaint);
    }

    private void drawArrow(Canvas canvas) {

        //设置起始点
        center.set(width/2,height/2 - maxCenterOff*(1-time));
        start.set(center.x,center.y + radius);
        //当前转过的角度
        currentAngle = time * (endAngle - startAngle) + startAngle;
        offsetX = (float) (arcLength * Math.sin(currentAngle));
        offsetY = (float) (arcLength * Math.cos(currentAngle));
        //设置终止点
        end1.set(start.x - offsetX,start.y - offsetY);
        end2.set(start.x + offsetX,start.y - offsetY);

        offSetControl = maxOffsetControl * time;
        center_se1.set((start.x + end1.x)/2,(end1.y + start.y)/2);
        center_se2.set((end2.x + start.x)/2,(start.y + end2.y)/2);
        //设置控制点
        contro1.set((float) (center_se1.x - offSetControl * Math.cos(currentAngle))
                ,(float)(center_se1.y + offSetControl * Math.sin(currentAngle)));
        contro2.set((float) (center_se2.x + offSetControl * Math.cos(currentAngle))
                ,(float)(center_se2.y + offSetControl * Math.sin(currentAngle)));

        //设置左边箭矢path
        leftPath.reset();
        leftPath.moveTo(start.x,start.y);
        leftPath.quadTo(contro1.x,contro1.y,end1.x,end1.y);
        //设置右边箭矢path
        rightPath.reset();
        rightPath.moveTo(start.x,start.y);
        rightPath.quadTo(contro2.x,contro2.y,end2.x,end2.y);

        //画path
        canvas.drawLine(center.x,center.y,start.x,start.y,linePaint);
        canvas.drawPath(leftPath,linePaint);
        canvas.drawPath(rightPath,linePaint);
    }

    public void startAnim(boolean isComplete){
        drawSuccess = isComplete;
        if (animator != null) {
            animator.start();
        }
    }

    @Override
    public void onClick(View v) {
        if (! isDownLoading){
            startAnim(true);
        }
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }
}
