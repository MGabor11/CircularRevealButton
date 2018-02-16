package com.apppoweron.circularrevealbuttondemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.apppoweron.circularrevealbutton.AnimatedLoadingButton;

public class SplashActivity extends BaseActivity {

    private static final short BUTTON_START_DELAY = 500;
    private static final short EXPANSION_ANIM_DURATION = 1500;
    private static final short PROGRESS_DURATION = 1000;

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

        splashButton.setExpansionAnimDuration(EXPANSION_ANIM_DURATION);
        mSplashDurationHandler.postDelayed(() -> {
            splashButton.callOnClick();
            mSplashDurationHandler.postDelayed(() -> splashButton.startProgressEndAnimation(viewId -> {
                // Start home activity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));

                // close splash activity
                finish();
            }), EXPANSION_ANIM_DURATION + PROGRESS_DURATION);
        }, BUTTON_START_DELAY);


    }

    @Override
    protected void onPause() {
        super.onPause();
        mSplashDurationHandler.removeCallbacksAndMessages(null);
    }


}
