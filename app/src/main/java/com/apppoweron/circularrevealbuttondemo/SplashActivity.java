package com.apppoweron.circularrevealbuttondemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {

    private static final short SPLASH_DURATION = 5000;

    private Handler mSplashDurationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSplashDurationHandler = new Handler();
        mSplashDurationHandler.postDelayed(() -> {
            // Start home activity
            startActivity(new Intent(SplashActivity.this, MainActivity.class));

            // close splash activity
            finish();
        }, SPLASH_DURATION);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSplashDurationHandler.removeCallbacksAndMessages(null);
    }


}
