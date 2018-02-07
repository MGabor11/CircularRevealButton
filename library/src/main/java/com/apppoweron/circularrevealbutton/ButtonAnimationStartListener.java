package com.apppoweron.circularrevealbutton;

@FunctionalInterface
interface ButtonAnimationStartListener extends BaseAnimationListener {
    void onAnimationStarted(int viewId);

    @Override
    default boolean isExpandingAnimation() {
        return false;
    }
}
