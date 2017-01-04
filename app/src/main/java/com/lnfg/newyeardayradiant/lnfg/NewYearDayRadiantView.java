package com.lnfg.newyeardayradiant.lnfg;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.lnfg.newyeardayradiant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lnfg on 2017/1/2.
 * 描述：
 */
public class NewYearDayRadiantView extends View{

    private Paint mPaint;//画笔
    private Bitmap mLanternBitmap;//灯笼位图
    private Bitmap mRedEnvelopeBitmap;//红包位图
    private Bitmap mRedHeartBitmap;//红心位图
    private Bitmap mFirecrackerBitmap;//鞭炮位图

    private Matrix matrixLantern;//矩阵

    private int mWidth;//画布宽度
    private int mHeight;//画布高度

    private int mLanternBitmapWidth;//灯笼位图的宽度
    private int mLanternBitmapHeight;//灯笼位图的高度

    private int mRedEnvelopeBitmapWidth;//红包位图的宽度
    private int mRedEnvelopeBitmapHeight;//红包位图的高度

    private int mRedHeartBitmapWidth;//红心位图的宽度
    private int mRedHeartBitmapHeight;//红心位图的高度

    private float mLenternScaleX = 0.0f;//灯笼X轴方向的缩放比例系数
    private float mLenternScaleY = 0.0f;//灯笼Y轴方向的缩放比例系数

    private float scaleNewYear = 0.0f;//XY轴缩放比例系数
    private float translateNewYear = dp2px(86);//XY轴移动

    private int mFirecrackerWidth;
    private int mFirecrackerHeight;
    private int mRotate = 0;//旋转角度

    private boolean isLanternScale = false;//灯笼缩放动画
    private boolean isredEnvelopeTraslate = false;//红包移动动画
    private boolean isRotate = false;//是否开始旋转
    private boolean isRedHeart = false;//开始红心动画
    private boolean isFirecracker;//开始鞭炮动画
    private int[] newYearColors ={0xFF00A6FF};

    public NewYearDayRadiantView(Context context) {
        this(context,null);
    }

    public NewYearDayRadiantView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NewYearDayRadiantView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        /*初始化 画笔*/
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);

        /*初始化矩阵*/
        matrixLantern =new Matrix();//矩阵

        /*获取灯笼位图*/
        mLanternBitmap =  BitmapFactory.decodeResource(getResources(),R.drawable.icon_denglong);
        mLanternBitmapWidth = mLanternBitmap.getWidth();//灯笼位图的宽度
        mLanternBitmapHeight = mLanternBitmap.getHeight();//灯笼位图的高度
        /*获取红包位图*/
        mRedEnvelopeBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_hongbao);
        mRedEnvelopeBitmapWidth = mRedEnvelopeBitmap.getWidth();//红包位图的宽度
        mRedEnvelopeBitmapHeight = mRedEnvelopeBitmap.getHeight();//红包位图的高度

        /*获取红心位图*/
        mRedHeartBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_hongxin);
        mRedHeartBitmapWidth = mRedHeartBitmap.getWidth();
        mRedHeartBitmapHeight = mRedHeartBitmap.getHeight();

        /*获取鞭炮位图*/
        mFirecrackerBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_bianpao);
        mFirecrackerWidth = mFirecrackerBitmap.getWidth();
        mFirecrackerHeight = mFirecrackerBitmap.getHeight();

        typeFace =Typeface.createFromAsset(getResources().getAssets(), "huawenhupo.ttf");

    } Typeface typeFace;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        /*解决wrap_content 无效的问题*/
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(300,300);
        }else if (widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(300,heightSpecSize);
        }else if (heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize,300);
        }
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth*0.5f,mHeight*0.5f);//将画布坐标系移动到画布中央
        canvas.save();//保存画布初始状态
        /*灯笼*/
        if (isLanternScale) {
            lantern(canvas);
        }
        /*四个红包*/
        if (isredEnvelopeTraslate){
            redEnvelope(canvas);
        }
        /*四个红心*/
        if (isRedHeart){
            redHeart(canvas);
        }
        /*四个鞭炮*/
        if (isFirecracker){
            firecracker(canvas);
        }
        if (isRotate){
            canvas.save();
            canvas.rotate(mRotate);
            canvas.restore();
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startLenternAnimator();//开始第一个动画【灯笼】
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    /*绘制灯笼*/
    private void lantern(Canvas canvas){
            matrixLantern.reset();
            matrixLantern.postScale(mLenternScaleX, mLenternScaleY);
            matrixLantern.preTranslate(-mLanternBitmapWidth, -mLanternBitmapHeight);
            matrixLantern.postTranslate(mLanternBitmapWidth, mLanternBitmapHeight);
            canvas.drawBitmap(mLanternBitmap, matrixLantern, mPaint);
    }
    /*绘制四个红包的位置、与动画*/
    private void redEnvelope(Canvas canvas){
            /*重置画笔*/
        mPaint.reset();
        mPaint.setColor(Color.YELLOW);
         /*东边*/
        canvas.save();
        Rect srcredEnvelope = new Rect(0, 0, mRedEnvelopeBitmapWidth, mRedEnvelopeBitmapHeight);// 指定图片绘制区域
        Rect dstredEnvelope = new Rect(-mRedEnvelopeBitmapWidth, -mRedEnvelopeBitmapWidth, mRedEnvelopeBitmapWidth, mRedEnvelopeBitmapWidth);// 指定图片在屏幕上显示的区域
        canvas.scale(scaleNewYear,scaleNewYear,0,0);//原点进行缩放
        canvas.translate(0,-translateNewYear);//移动
        canvas.drawBitmap(mRedEnvelopeBitmap, srcredEnvelope, dstredEnvelope, mPaint);
        canvas.restore();
        /*西边*/
        canvas.save();
        canvas.scale(scaleNewYear,scaleNewYear,0,0);//原点进行缩放
        canvas.translate(-translateNewYear,0);//移动
        canvas.rotate(-90);
        canvas.drawBitmap(mRedEnvelopeBitmap, srcredEnvelope, dstredEnvelope, mPaint);
        canvas.restore();
        /*北边*/
        canvas.save();
        canvas.scale(scaleNewYear,scaleNewYear,0,0);//原点进行缩放
        canvas.translate(0,translateNewYear);//移动
        canvas.drawBitmap(mRedEnvelopeBitmap, srcredEnvelope, dstredEnvelope, mPaint);
        canvas.restore();
        /*南边*/
        canvas.save();
        canvas.scale(scaleNewYear,scaleNewYear,0,0);//原点进行缩放
        canvas.translate(translateNewYear,0);//移动
        canvas.rotate(90);
        canvas.drawBitmap(mRedEnvelopeBitmap, srcredEnvelope, dstredEnvelope, mPaint);
        canvas.restore();

    }

   /*绘制四个红心的位置、与动画*/
    private void redHeart(Canvas canvas){
            /*重置画笔*/
            mPaint.reset();
            mPaint.setColor(Color.GREEN);
            /*东北*/
            canvas.save();
            Rect srcredHeart = new Rect(0, 0, mRedHeartBitmapWidth, mRedHeartBitmapHeight);// 指定图片绘制区域
            Rect dstredHeart = new Rect(-mRedHeartBitmapWidth, -mRedHeartBitmapWidth, mRedHeartBitmapWidth, mRedHeartBitmapWidth); // 指定图片在屏幕上显示的区域
            canvas.scale(scaleNewYear*0.6f,scaleNewYear*0.6f,0,0);//原点进行缩放
            canvas.translate(translateNewYear/2,-translateNewYear/2);//移动
            canvas.rotate(45);
            canvas.drawCircle(-translateNewYear/2,translateNewYear/2,10,mPaint);
            canvas.drawCircle(-translateNewYear/2,-translateNewYear/5,15,mPaint);
            canvas.drawBitmap(mRedHeartBitmap, srcredHeart, dstredHeart,mPaint);
            canvas.restore();
             /*东南*/
            canvas.save();
            canvas.scale(scaleNewYear*0.6f,scaleNewYear*0.6f,0,0);//原点进行缩放
            canvas.translate(translateNewYear/2,translateNewYear/2);//移动
            canvas.rotate(135);
            canvas.drawCircle(-translateNewYear/2,translateNewYear/2,10,mPaint);
            canvas.drawCircle(-translateNewYear/2,-translateNewYear/5,15,mPaint);
            canvas.drawBitmap(mRedHeartBitmap, srcredHeart, dstredHeart,mPaint);
            canvas.restore();
            /*西北*/
            canvas.save();
            canvas.scale(scaleNewYear*0.6f,scaleNewYear*0.6f,0,0);//原点进行缩放
            canvas.translate(-translateNewYear/2,-translateNewYear/2);//移动
            canvas.rotate(-45);
            canvas.drawCircle(-translateNewYear/2,translateNewYear/2,10,mPaint);
            canvas.drawCircle(-translateNewYear/2,-translateNewYear/5,15,mPaint);
            canvas.drawBitmap(mRedHeartBitmap, srcredHeart, dstredHeart,mPaint);
            canvas.restore();
            /*西南*/
            canvas.save();
            canvas.scale(scaleNewYear*0.6f,scaleNewYear*0.6f,0,0);//原点进行缩放
            canvas.translate(-translateNewYear/2,translateNewYear/2);//移动
            canvas.rotate(-135);
            canvas.drawCircle(-translateNewYear/2,translateNewYear/2,10,mPaint);
            canvas.drawCircle(-translateNewYear/2,-translateNewYear/5,15,mPaint);
            canvas.drawBitmap(mRedHeartBitmap, srcredHeart, dstredHeart,mPaint);
            canvas.restore();


    }
    /*绘制四个鞭炮的位置、与动画*/
    private void firecracker(Canvas canvas){
            /*重置画笔*/
            mPaint.reset();
            mPaint.setColor(Color.GREEN);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(3.5f);
            /*东北*/
            canvas.save();
            Rect src = new Rect(0, 0, mFirecrackerWidth, mFirecrackerHeight);// 指定图片绘制区域
            Rect dst = new Rect(-mFirecrackerWidth, -mFirecrackerWidth, mFirecrackerWidth, mFirecrackerWidth);// 指定图片在屏幕上显示的区域
            canvas.scale(scaleNewYear*0.9f,scaleNewYear*0.9f,0,0);//原点进行缩放
            canvas.translate(translateNewYear*2/3,-translateNewYear*2/3);//移动
            canvas.rotate(45);
            canvas.drawCircle(-translateNewYear/2,translateNewYear/2,25,mPaint);
            canvas.drawBitmap(mFirecrackerBitmap, src, dst,mPaint);
            canvas.restore();
            /*东南*/
            canvas.save();
            canvas.scale(scaleNewYear*0.9f,scaleNewYear*0.9f,0,0);//原点进行缩放
            canvas.translate(translateNewYear*2/3,translateNewYear*2/3);//移动
            canvas.rotate(135);
            canvas.drawCircle(-translateNewYear/2,translateNewYear/2,25,mPaint);
            canvas.drawBitmap(mFirecrackerBitmap, src, dst,mPaint);
            canvas.restore();
            /*西北*/
            canvas.save();
            canvas.scale(scaleNewYear*0.9f,scaleNewYear*0.9f,0,0);//原点进行缩放
            canvas.translate(-translateNewYear*2/3,-translateNewYear*2/3);//移动
            canvas.rotate(-45);
            canvas.drawCircle(-translateNewYear/2,translateNewYear/2,25,mPaint);
            canvas.drawBitmap(mFirecrackerBitmap, src, dst,mPaint);
            canvas.restore();
            /*西南*/
            canvas.save();
            canvas.scale(scaleNewYear*0.9f,scaleNewYear*0.9f,0,0);//原点进行缩放
            canvas.translate(-translateNewYear*2/3,translateNewYear*2/3);//移动
            canvas.rotate(-135);
            canvas.drawCircle(-translateNewYear/2,translateNewYear/2,25,mPaint);
            canvas.drawBitmap(mFirecrackerBitmap, src, dst,mPaint);
            canvas.restore();

            newYear(canvas);//绘制文本
    }

    /*绘制文本*/
    private void newYear(Canvas canvas){
        /*设置画笔基本属性*/
        mPaint.reset();
        mPaint.setColor(newYearColors[0]);
        mPaint.setFakeBoldText(true);
        mPaint.setTextSize(55);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextScaleX(1.5F);
        mPaint.setTypeface(typeFace);
        /*东北*/
        canvas.save();
        canvas.scale(scaleNewYear,scaleNewYear,0,0);//原点进行缩放
        canvas.translate(translateNewYear*3/4,-translateNewYear*3/4);//移动/
        canvas.rotate(45);
        canvas.drawText("2017",0,0,mPaint);
        mPaint.setColor(Color.YELLOW);
        canvas.drawCircle(0,15,15,mPaint);
        canvas.restore();
        /*东南*/
        canvas.save();
        mPaint.setColor(Color.YELLOW);
        canvas.scale(scaleNewYear,scaleNewYear,0,0);//原点进行缩放
        canvas.translate(translateNewYear*3/4,translateNewYear*3/4);//移动/
        canvas.rotate(135);
        canvas.drawText("2017",0,0,mPaint);
        mPaint.setColor(newYearColors[0]);
        canvas.drawCircle(0,15,15,mPaint);
        canvas.restore();
        /*西北*/
        canvas.save();
        mPaint.setColor(newYearColors[0]);
        canvas.scale(scaleNewYear,scaleNewYear,0,0);//原点进行缩放
        canvas.translate(-translateNewYear*3/4,-translateNewYear*3/4);//移动
        canvas.rotate(-45);
        canvas.drawText("2017",0,0,mPaint);
        mPaint.setColor(Color.YELLOW);
        canvas.drawCircle(0,15,15,mPaint);
        canvas.restore();
         /*西南*/
        canvas.save();
        mPaint.setColor(Color.YELLOW);
        canvas.scale(scaleNewYear,scaleNewYear,0,0);//原点进行缩放
        canvas.translate(-translateNewYear*3/4,translateNewYear*3/4);//移动/
        canvas.rotate(-135);
        canvas.drawText("2017",0,0,mPaint);
        mPaint.setColor(newYearColors[0]);
        canvas.drawCircle(0,15,15,mPaint);
        canvas.restore();
    }
    /**
     * 步骤 一
     * 开始中心灯笼的动画变换
     * 中心点 有小变大 进行 缩放 动画
     * Scale
     */
    private void startLenternAnimator(){
        isLanternScale = true;
        AnimatorSet animatorSet = new AnimatorSet();//动画集合
        List<Animator> animators = new ArrayList<>(2);
        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(mLenternScaleX,2.0f);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLenternScaleX = (float) animation.getAnimatedValue();
                mLenternScaleY= (float) animation.getAnimatedValue();
                invalidate();//重新调用onDraw();
            }
        });

        animators.add(scaleAnimator);//将scaleAnimator存入animators集合中

        animatorSet.setDuration(950);
        animatorSet.playTogether(animators);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();//开始动画
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startRedEnvelopeAnimator();//调用四个红包动画

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    /**
     * 步骤 二
     * 红包由中心点往东南西北四个方向 做动画
     * 中心点 有小变大 进行 缩放、平移动画
     * Scale、Translate
     */
    private void startRedEnvelopeAnimator(){
        AnimatorSet animatorSet = new AnimatorSet();
        List<Animator> animators = new ArrayList<>(1);
        ValueAnimator mRedEnvelopeAnimator = ValueAnimator.ofFloat(scaleNewYear,1.0f);
        mRedEnvelopeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scaleNewYear = (float) animation.getAnimatedValue();
                isredEnvelopeTraslate = true;
                isRedHeart = true;
                isFirecracker = true;
                invalidate();
            }
        });
        mRedEnvelopeAnimator.setRepeatCount(ValueAnimator.INFINITE); //无限循环
        mRedEnvelopeAnimator.setRepeatMode(ValueAnimator.RESTART);//       从头开始动画

        animators.add(mRedEnvelopeAnimator);

        animatorSet.setDuration(950);
        animatorSet.playTogether(animators);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rotateAnimator();//开始旋转 没用。。。。。。。。。。
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    /*整个画布旋转*/
    private void rotateAnimator(){
        isRotate = true;
        ValueAnimator rotateAnimator = ValueAnimator.ofInt(mRotate,360);
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRotate = (int)animation.getAnimatedValue();
                invalidate();
            }
        });
        rotateAnimator.setDuration(2500);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimator.start();
    }
    /*转换单位*/
    private int dp2px(float dp){
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    /*对外提供调用*/
    public void start(){
        startLenternAnimator();
    }

}
