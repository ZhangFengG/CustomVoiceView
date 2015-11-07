package com.example.test.customvoiceview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description: 自定义语音View
 * Created by Zhangfeng on 2015/11/4.
 * ModifiedBy: Clowire51
 * ModifiedTime: 2015/11/4 14:13
 * ModifiedNotes:
 * Version 1.0
 */
public class VoiceView extends View implements Runnable{

    private Paint mCirclePaint;   //圆框

    private RectF mCircleRect;

    private int mLogoLeft,mLogoTop,mLogoRight,mLogoBottom,mRadius,mMaxRadius;
    private int mMinRadius;
    private int mInterval;
    private int mLogoWidth = 100;
    private int mLogoHeight = 100;

    private int[] mRadiusArray;

    private static final int BORDER_COUNT = 4;

    private int mode = 1;   //模式，默认对话模式

    private Bitmap mLogo;
    private Bitmap mLogoBackground;

    private boolean isRunAble;  //线程是否可运行
    private boolean isDestroy;  //销毁线程标志
    private boolean hasOneCycle = false;    //是否执行了一个周期
    private boolean isShowBack = false;    //是否被点击，显示背景

    public static final int SESSION_MODE = 1;
    public static final int MONITOR_MODE = 2;

    public VoiceView(Context context) {
        super(context);

    }

    public VoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttribute(context.obtainStyledAttributes(attrs, R.styleable.VoiceView));
    }

    public VoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttribute(context.obtainStyledAttributes(attrs,R.styleable.VoiceView));

    }

    private void initAttribute(TypedArray tp) {

        mLogo = BitmapFactory.decodeResource(this.getResources(), R.drawable.voice_logo);
        mLogoBackground = BitmapFactory.decodeResource(this.getResources(), R.drawable.voice_background);

        /*
         *由于Logo的高度大于宽度，而最小半径由大的属性作为参数。所以应把高度作为布局的属性来设置
         *
         */
        mLogoHeight = (int) tp.getDimension(R.styleable.VoiceView_logo_height,mLogoHeight);
        mLogoWidth = (int) (((float)mLogoHeight/mLogo.getHeight())*mLogo.getWidth());   //按比例缩放
//        mLogoHeight = (int) tp.getDimension(R.styleable.VoiceView_logo_height,mLogoHeight);   //按比例缩放更合理

        //框设置
        mCirclePaint = new Paint();
        mCirclePaint.setColor(0xFF00CBFE);
        mCirclePaint.setStyle(Paint.Style.STROKE);  //空心
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStrokeWidth(2);    //边框宽度
        mMinRadius = (mLogoWidth>mLogoHeight?mLogoWidth:mLogoHeight)/2+10;
        mMaxRadius = 300;
        mInterval = (mMaxRadius-mMinRadius)/BORDER_COUNT;  //获取间隔值
        mRadiusArray = new int[BORDER_COUNT];
        for (int i=0; i<mRadiusArray.length; i++){
            if(i==0){
                mRadiusArray[0]=mMinRadius;
            }else {
                mRadiusArray[i] = mRadiusArray[i-1]+mInterval;
            }
        }
        setMode(SESSION_MODE);  //初始化模式为对话模式
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mLogoLeft = (getWidth()-mLogoWidth)/2;    //居中
        mLogoTop = (getHeight()-mLogoHeight)/2;
        mLogoRight = mLogoLeft + mLogoWidth;
        mLogoBottom = mLogoTop + mLogoHeight;

        initRadius();


    }

    private void initRadius() {
        mRadius = mMinRadius;
        hasOneCycle = false;    //是否已经经过一次标志
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLogo(canvas, mLogo);    //画标志
        if(isShowBack){
            drawLogoBackgroud(canvas);
        }
        if(isRunAble){
            drawCircleBorders(canvas);
        }


    }
    /**
     * 画外框圆
     * @param canvas view的画布
     * @description: Created by Zhangfeng on 2015/11/5 14:43
     */
    private void drawCircleBorders(Canvas canvas) {
        int radius = (mRadius-mMinRadius)%mInterval+mMinRadius;
        if(hasOneCycle){
            //周期执行
            for (int i=0; i<4; i++){
                drawCircleBorder(canvas,radius+i*mInterval);
            }
        }else{
            //首次执行，从一条开始
            for (int i=0; i<4; i++){
                drawCircleBorder(canvas,mRadius-i*mInterval);
            }
        }

    }

    private void drawCircleBorder(Canvas canvas,int radius) {
        if(radius>mMinRadius&&radius<mMaxRadius){
            mCircleRect = new RectF(getWidth()/2-radius,getHeight()/2-radius,getWidth()/2+radius,getHeight()/2+radius);
            mCirclePaint.setAlpha((255 - (255*(radius - mMinRadius) /(mMaxRadius-mMinRadius))));
            canvas.drawArc(mCircleRect,360,360,false,mCirclePaint);
        }
    }

    private void drawLogo(Canvas canvas,Bitmap logo) {
        RectF rectF = new RectF();
        rectF.left = mLogoLeft;    //居中
        rectF.top = mLogoTop;
        rectF.right = mLogoRight;
        rectF.bottom = mLogoBottom;
        canvas.drawBitmap(logo,null,rectF,null);

    }

    private void drawLogoBackgroud(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.left = getWidth()/2-mMinRadius;    //居中
        rectF.top = getHeight()/2-mMinRadius;
        rectF.right = getWidth()/2+mMinRadius;
        rectF.bottom = getHeight()/2+mMinRadius;
        canvas.drawBitmap(mLogoBackground,null,rectF,null);
    }


    @Override
    public void run() {
        while (!isDestroy){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(isRunAble){
                mRadius+=3;
                if(mRadius>=mMaxRadius){
                    mRadius=mMinRadius;
                    hasOneCycle = true;
                }
                postInvalidate();
            }
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isDestroy = true;   //接受线程
    }

    public void setRunable(boolean isRun){
        this.isRunAble = isRun;
    }
    /**
     * 模式设置
     * @param mode 对应VoiceView中的两种模式VoiceView.SESSION_MODE和VoiceView.MONITOR_MODE
     * @description: Created by Zhangfeng on 2015/11/6 10:57
     */
    public void setMode(int mode){
        this.mode = mode;
        switch (mode){
            case SESSION_MODE:
                setRunable(false);
                setVisiableBackground(false);
                break;
            case MONITOR_MODE:
                setRunable(true);
                setVisiableBackground(false);
                break;
        }
    }

    public void setVisiableBackground(boolean isVisiable){
        if(mode==SESSION_MODE){
            isShowBack = isVisiable;
            initRadius();
            setRunable(isVisiable);
        }else{
            isShowBack = false;
        }
        invalidate();
    }
}
