package com.apppoweron.circularrevealbutton.container

import android.os.Build
import android.support.annotation.RequiresApi

interface CircularRevealContainer {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun startCircularRevealAnimation(data: ButtonCRevealAnimationData, listener: ((viewId: Int) -> Unit)?)

    fun removeAnimationMask()
    fun getLocationInWindow(outLocation: IntArray)
}
