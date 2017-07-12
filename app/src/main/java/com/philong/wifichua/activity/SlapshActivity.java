package com.philong.wifichua.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.philong.wifichua.R;

public class SlapshActivity extends AppCompatActivity {

    private ImageView imgLogo;
    private Animation mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slapsh);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.slapsh_animation);
        imgLogo.startAnimation(mAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(DangNhapActivity.newIntent(SlapshActivity.this));
                finish();
            }
        }, 3000);
    }
}
