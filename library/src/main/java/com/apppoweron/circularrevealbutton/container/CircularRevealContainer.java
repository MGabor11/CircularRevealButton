package com.apppoweron.circularrevealbutton.container;

import com.apppoweron.circularrevealbutton.ButtonAnimationEndListener;

public interface CircularRevealContainer {
    void startCircularRevealAnimation(ButtonCRevealAnimationData data, ButtonAnimationEndListener animEndListener);
    void removeAnimationMask();
    void getLocationInWindow(int[] outLocation);
}
