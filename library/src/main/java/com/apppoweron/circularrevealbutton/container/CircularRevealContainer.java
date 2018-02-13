package com.apppoweron.circularrevealbutton.container;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.apppoweron.circularrevealbutton.ButtonAnimationEndListener;

public interface CircularRevealContainer {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void startCircularRevealAnimation(ButtonCRevealAnimationData data, ButtonAnimationEndListener animEndListener);
    void removeAnimationMask();
    void getLocationInWindow(int[] outLocation);
}
