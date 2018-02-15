package com.apppoweron.circularrevealbuttondemo;

import android.os.Bundle;
import android.os.Handler;

import com.apppoweron.circularrevealbutton.AnimatedLoadingButton;

public class SplashActivity extends BaseActivity {

    private static final short BUTTON_START_DELAY = 300;

    private Handler mSplashDurationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnimatedLoadingButton splashButton = findViewById(R.id.splash_anim_btn);


        mSplashDurationHandler = new Handler();




        mSplashDurationHandler.postDelayed(splashButton::callOnClick, BUTTON_START_DELAY);



    }

    @Override
    protected void onPause() {
        super.onPause();
        mSplashDurationHandler.removeCallbacksAndMessages(null);
    }


}
