package com.apppoweron.circularrevealbutton.container

import android.os.Build
import android.support.annotation.RequiresApi

import com.apppoweron.circularrevealbutton.ButtonAnimationEndListener

interface CircularRevealContainer {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun startCircularRevealAnimation(data: ButtonCRevealAnimationData, animEndListener: ButtonAnimationEndListener)

    fun removeAnimationMask()
    fun getLocationInWindow(outLocation: IntArray)
}
