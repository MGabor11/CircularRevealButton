package com.apppoweron.circularrevealbuttondemo

import android.content.Intent
import android.os.Bundle
import android.os.Handler

import com.apppoweron.circularrevealbutton.AnimatedLoadingButton

class SplashActivity : BaseActivity() {

    companion object {
        private const val BUTTON_START_DELAY: Short = 500
        private const val EXPANSION_ANIM_DURATION: Short = 1500
        private const val PROGRESS_DURATION: Short = 1000
    }

    private var mSplashDurationHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        val splashButton = findViewById<AnimatedLoadingButton>(R.id.splash_anim_btn)

        mSplashDurationHandler = Handler()

        splashButton.setExpansionAnimDuration(EXPANSION_ANIM_DURATION.toInt())
        mSplashDurationHandler!!.postDelayed({
            splashButton.callOnClick()
            mSplashDurationHandler!!.postDelayed({
                splashButton.startProgressEndAnimation {
                    // Start home activity
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))

                    // close splash activity
                    finish()
                }
            }, (EXPANSION_ANIM_DURATION + PROGRESS_DURATION).toLong())
        }, BUTTON_START_DELAY.toLong())


    }

    override fun onPause() {
        super.onPause()
        mSplashDurationHandler!!.removeCallbacksAndMessages(null)
    }

}
