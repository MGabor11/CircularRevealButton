package com.apppoweron.circularrevealbuttondemo

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.apppoweron.circularrevealbuttondemo.common.BaseActivity
import com.apppoweron.circularrevealbuttondemo.viewmodel.SplashViewModel
import com.apppoweron.circularrevealbuttondemo.viewmodel.impl.SplashViewModelImpl

class SplashActivity : BaseActivity() {

    companion object {
        private const val TAG = "SplashActivity"
    }

    private var mSplashViewModel: SplashViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

       //val splashButton = findViewById<AnimatedLoadingButton>(R.id.splash_anim_btn)

        mSplashViewModel = ViewModelProviders.of(this).get(SplashViewModelImpl::class.java)

        /*splashButton.setExpansionAnimDuration(EXPANSION_ANIM_DURATION.toInt())
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
        }, BUTTON_START_DELAY.toLong())*/

    }

    override fun onDestroy() {
        mSplashViewModel?.onViewDetached()
        super.onDestroy()
    }

}
