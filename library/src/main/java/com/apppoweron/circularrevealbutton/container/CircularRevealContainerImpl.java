package com.apppoweron.circularrevealbutton.container;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;

import com.apppoweron.circularrevealbutton.ButtonAnimationEndListener;
import com.apppoweron.circularrevealbutton.R;

public class CircularRevealContainerImpl extends FrameLayout implements CircularRevealContainer {

    private static final String TAG = "CircularRevealContainer";

    private static final boolean IS_DEBUG_MESSAGE_ENABLED = false;
    private static final int ANIM_DURATION = 2000;

    private boolean isDebugMessagesEnabled;
    private boolean isAnimInProgress;

    public CircularRevealContainerImpl(Context context) {
        super(context);
        init(context, null);
    }

    public CircularRevealContainerImpl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularRevealContainerImpl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircularRevealContainerImpl(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.circular_reveal_container_view, this);
        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.CircularRevealContainer,
                    0, 0);
            isDebugMessagesEnabled = typedArray.getBoolean(R.styleable.CircularRevealContainer_isDebuggable, IS_DEBUG_MESSAGE_ENABLED);
            typedArray.recycle();
        } else {
            isDebugMessagesEnabled = IS_DEBUG_MESSAGE_ENABLED;
        }

    }

    private View getAnimationMask() {
        return findViewById(R.id.button_circular_reveal_mask);
    }

    private void setAnimationColor(Integer color) {
        if (color != null) {
            getAnimationMask().setBackgroundColor(color);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void startCircularRevealAnimation(ButtonCRevealAnimationData data, ButtonAnimationEndListener animEndListener) {

        //Assemble animator
        Animator animator = assembleAnimator(data, animEndListener);

        //Set the color of the animation
        setAnimationColor(data.getAnimColor());

        //Disabling touch events when it does the animation
        isAnimInProgress = true;

        getAnimationMask().bringToFront();
        getAnimationMask().setVisibility(View.VISIBLE);

        //Start the animation!
        animator.start();
    }

    @Override
    public void removeAnimationMask() {
        isAnimInProgress = false;
        getAnimationMask().setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Animator assembleAnimator(ButtonCRevealAnimationData data, ButtonAnimationEndListener animEndListener) {

        int endRadius = (int) Math.hypot(getWidth(), getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(getAnimationMask(), data.getX(), data.getY(), data.getAnimStartRadius(), endRadius);


        if (data.getAnimDuration() != null) {
            anim.setDuration(data.getAnimDuration());
        } else {
            anim.setDuration(ANIM_DURATION);
        }

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (isDebugMessagesEnabled) {
                    Log.d(TAG, "onAnimationEnd AnimatedLoadingButton");
                }
                isAnimInProgress = false;
                if (animEndListener != null) {
                    animEndListener.onAnimationEnded(getId());
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                isAnimInProgress = false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        return anim;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isAnimInProgress) {
            return super.dispatchTouchEvent(ev);
        }
        return true;
    }
}
