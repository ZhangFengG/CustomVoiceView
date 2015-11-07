package com.example.test.customvoiceview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private VoiceLayout mVoiceLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVoiceLayout = (VoiceLayout) findViewById(R.id.voice_layout);
        findViewById(R.id.session_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVoiceLayout.setMode(VoiceView.SESSION_MODE);
            }
        });
        findViewById(R.id.monitor_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVoiceLayout.setMode(VoiceView.MONITOR_MODE);
            }
        });
        new Thread(mVoiceLayout.getRunnable()).start();
    }

}
