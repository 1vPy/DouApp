package com.roy.douapp.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.roy.douapp.R;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBg;
    TextView tvSkip;
    AnimatorSet animSet;

    private CountDownTimer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        findView();
        initView();
        initEvent();
    }

    private void findView() {
        ivBg = (ImageView) findViewById(R.id.iv_bg);
        tvSkip = (TextView) findViewById(R.id.tv_skip);
    }

    private void initView() {
        timer = new CountDownTimer(3500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvSkip.setText(String.format(getResources().getString(R.string.skip), (int) (millisUntilFinished / 1000 + 0.1)));
            }

            @Override
            public void onFinish() {
                tvSkip.setText(String.format(getResources().getString(R.string.skip), 0));
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        };
        timer.start();
    }

    private void initEvent() {
        tvSkip.setOnClickListener(this);
        animSet = new AnimatorSet();
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(ivBg, "scaleX", 1f, 0.5f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(ivBg, "scaleY", 1f, 0.5f);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(ivBg, "rotation", 0f, 360f);
        animSet.setDuration(1500);
        animSet.play(anim1).with(anim2);
        animSet.play(anim3).after(anim2);
        animSet.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animSet != null) {
            animSet.cancel();
        }
        animSet.cancel();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_skip:
                if (animSet != null) {
                    animSet.cancel();
                }
                if (timer != null) {
                    timer.cancel();
                }
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
}
