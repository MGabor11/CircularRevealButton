package com.apppoweron.circularrevealbuttondemo.viewmodel.impl

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.apppoweron.circularrevealbuttondemo.viewmodel.SplashViewModel
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class SplashViewModelImpl(application: Application) : AndroidViewModel(application), SplashViewModel {

    companion object {
        private const val BUTTON_START_DELAY: Short = 500
        private const val EXPANSION_ANIM_DURATION: Short = 1500
        private const val PROGRESS_DURATION: Short = 1000
    }

    private val buttonAnimCompletable: Completable? = Completable.complete()
            .doOnSubscribe { triggerButtonClick() }
            .delay(3, TimeUnit.SECONDS)
            .doOnComplete { triggerButtonCompressAnimation() }

    private var disposable: Disposable? = null

    override fun startButtonAnimation() {
        disposable = buttonAnimCompletable?.subscribe()
    }

    private fun triggerButtonClick() {

    }

    private fun triggerButtonCompressAnimation() {

    }

    override fun onViewAttached() {
        startButtonAnimation()
    }

    override fun onViewDetached() {
        if (this.disposable != null && !this.disposable?.isDisposed!!) {
            this.disposable?.dispose()
        }
    }

    //override fun ondDetach() {
    //if (this.buttonAnimDisposable != null && !buttonAnimDisposable.isDisposed) this.buttonAnimDisposable.dispose()
    //}

    //private fun


}/*Completable.complete()
         .doOnSubscribe{}
                .delay(3,TimeUnit.SECONDS)
                .doOnComplete {  }*//*Completable.complete()
        .delay(3,TimeUnit.SECONDS)
     .doOnComplete(() -> System.out.println("Time to complete " + (System.currentTimeMillis() - start)))
            .subscribe();*//*override fun onResume() {
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
    }*/
