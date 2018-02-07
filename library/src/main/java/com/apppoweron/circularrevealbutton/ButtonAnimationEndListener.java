package com.apppoweron.circularrevealbutton;

@FunctionalInterface
public interface ButtonAnimationEndListener extends BaseAnimationListener {
    void onAnimationEnded(int viewId);

    @Override
    default boolean isExpandingAnimation() {
        return true;
    }
}
