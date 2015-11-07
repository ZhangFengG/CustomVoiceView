package com.example.test.customvoiceview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Description: 语音控件组合
 * Created by Zhangfeng on 2015/11/6.
 * ModifiedBy: Clowire51
 * ModifiedTime: 2015/11/6 10:34
 * ModifiedNotes:
 * Version
 */
public class VoiceLayout extends FrameLayout {

    private VoiceView mVoiceView;
    private ImageView mImageView;
    private ImageView mBlankView;   //承载点击事件

    private Animation rotateAnimation;

    public VoiceLayout(Context context) {
        super(context);
    }

    public VoiceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.voice_layout, this);
        mVoiceView = (VoiceView) findViewById(R.id.voice_view_id);
        mImageView = (ImageView) findViewById(R.id.voice_progress_id);
        mBlankView = (ImageView) findViewById(R.id.voice_blank_iv);
        rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.voice_progress_anim);
        mImageView.startAnimation(rotateAnimation);
        isDealCommand(false);
        mBlankView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mVoiceView.setVisiableBackground(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        mVoiceView.setVisiableBackground(false);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mVoiceView.setVisiableBackground(false);
                        break;
                }
                return true;    //必须要设置为true才能获取up等事件
            }
        });
    }

    public VoiceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 是否处理指令
     * @param isDeal true:正在处理，显示处理动画; false:不在处理
     * @description: Created by Zhangfeng on 2015/11/6 11:29
     */
    public boolean isDealCommand(boolean isDeal){
        if(isDeal){
            mImageView.setVisibility(View.VISIBLE);
            mImageView.startAnimation(rotateAnimation);
            mBlankView.setVisibility(INVISIBLE);
        }else {
            mImageView.clearAnimation();
            mImageView.setVisibility(View.INVISIBLE);
            mBlankView.setVisibility(VISIBLE);
        }
        return true;
    }
    /**
     * @see VoiceView#setMode
     * @description: Created by Zhangfeng on 2015/11/6 13:59
     */
    public void setMode(int mode){
        mVoiceView.setMode(mode);
    }

    public VoiceView getRunnable(){
        return mVoiceView;
    }
}
