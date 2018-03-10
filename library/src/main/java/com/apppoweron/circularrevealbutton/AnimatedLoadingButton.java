package com.apppoweron.circularrevealbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.apppoweron.circularrevealbutton.container.ButtonCRevealAnimationData;
import com.apppoweron.circularrevealbutton.container.CircularRevealContainer;
import com.apppoweron.circularrevealbutton.container.CircularRevealContainerNotFoundException;
import com.apppoweron.circularrevealbutton.util.UIHierarchyUtil;

public class AnimatedLoadingButton extends AppCompatButton implements View.OnClickListener {

    private static final String TAG = "AnimatedLoadingButton";

    private static final boolean isDebugMessagesEnabled = true;

    private static final byte DEFAULT_REQUIRED_OFFSET = 20;
    private static final byte DEFAULT_PROGRESS_WIDTH = 8;
    private static final short DEFAULT_EXPANSION_TIME_IN_MS = 3000;

    private enum State {
        PROGRESS, IDLE
    }

    private byte mRequiredOffset;
    private byte mProgressWidth;

    private int mOriginalHeight = 0;
    private int mOriginalWidth = 0;
    private int mRequiredSize = 0;

    private OnClickListener mOnClickListener;

    private State mState = State.IDLE;
    private Integer mCircularRevealAnimDuration;
    private int mExpansionAnimDuration;
    private boolean mIsSizingInProgress;
    private boolean mIsCircularRevealEnabled;
    private int mProgressColor;

    private GradientDrawable mGradientDrawable;
    private CircularAnimatedDrawable mAnimatedDrawable;

    private ButtonAnimationStartListener mAnimationStartListener;
    private ButtonAnimationEndListener mAnimationEndListener;

    private ButtonColorProvider mButtonColorProvider;

    private CircularRevealContainer mCircularRevealContainer;

    public AnimatedLoadingButton(Context context) {
        super(context);
        init(context, null);
    }

    public AnimatedLoadingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnimatedLoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        //Initializing ButtonColorProvider
        mButtonColorProvider = new ButtonColorProvider(this);

        //Required to inline the button's text
        setMaxLines(1);

        //Creating gradient drawable from shape
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mGradientDrawable = (GradientDrawable)
                    ContextCompat.getDrawable(context, R.drawable.button_shape_default).getConstantState().newDrawable().mutate();
        } else {
            mGradientDrawable = (GradientDrawable)
                    ContextCompat.getDrawable(context, R.drawable.button_shape_default).mutate();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mGradientDrawable.setColor(mButtonColorProvider.getButtonOriginalColorId());
        }

        TypedArray typedArray;
        if (attrs != null) {
            typedArray = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.CircularRevealButton,
                    0, 0);

            mRequiredOffset = (byte) typedArray.getInt(R.styleable.CircularRevealButton_requiredOffset, DEFAULT_REQUIRED_OFFSET);
            mProgressWidth = (byte) typedArray.getInt(R.styleable.CircularRevealButton_progressWidth, DEFAULT_PROGRESS_WIDTH);
            if (typedArray.getInt(R.styleable.CircularRevealButton_circularRevealAnimDuration, 0) != 0) {
                mCircularRevealAnimDuration = typedArray.getInt(R.styleable.CircularRevealButton_circularRevealAnimDuration, 0);
            }

            if (typedArray.getInt(R.styleable.CircularRevealButton_expansionAnimDuration, 0) != 0) {
                mExpansionAnimDuration = typedArray.getInt(R.styleable.CircularRevealButton_expansionAnimDuration, 0);
            } else {
                mExpansionAnimDuration = DEFAULT_EXPANSION_TIME_IN_MS;
            }

            if (typedArray.getColor(R.styleable.CircularRevealButton_progressColor, 0) != 0) {
                mProgressColor = typedArray.getColor(R.styleable.CircularRevealButton_progressColor, 0);
            } else {
                mProgressColor = mButtonColorProvider.getButtonTextColor();
            }

            mIsCircularRevealEnabled = typedArray.getBoolean(R.styleable.CircularRevealButton_isAnimEnabled, false);

            if (typedArray.getColor(R.styleable.CircularRevealButton_buttonBackgroundColor, 0) != 0) {
                int bgColor = typedArray.getColor(R.styleable.CircularRevealButton_buttonBackgroundColor, 0);
                mGradientDrawable.setColor(bgColor);
            }

            typedArray.recycle();
        } else {
            mRequiredOffset = DEFAULT_REQUIRED_OFFSET;
            mProgressWidth = DEFAULT_PROGRESS_WIDTH;
            mExpansionAnimDuration = DEFAULT_EXPANSION_TIME_IN_MS;
            mProgressColor = mButtonColorProvider.getButtonTextColor();
        }

        setBackground(mGradientDrawable);
        super.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (!mIsSizingInProgress && mState == State.IDLE) {
            startProgressAnimation();
            if (mOnClickListener != null) {
                mOnClickListener.onClick(view);
            }
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
        mOnClickListener = listener;
    }

    /**
     * Compressing button animation
     */
    private void startProgressAnimation() {
        startProgressAnimation(mAnimationStartListener);
    }

    /**
     * Compressing button animation
     *
     * @param listener animation start listener
     */
    private void startProgressAnimation(ButtonAnimationStartListener listener) {
        if (mState != State.IDLE) {
            return;
        }

        storeOriginalDimensions();

        mState = State.PROGRESS;
        setClickable(false);

        animateButtonDimensions(isExpansionAnim(listener), listener);
    }

    public void startProgressEndAnimation() {
        startProgressEndAnimation(mAnimationEndListener);
    }

    /**
     * Start expanding animation, it can be circular reveal or expanding
     */
    public void startProgressEndAnimation(ButtonAnimationEndListener listener) {
        try {
            startProgressEndAnimation(0, listener);
        } catch (CircularRevealContainerNotFoundException e) {
            if (isDebugMessagesEnabled) {
                Log.e(TAG, "Container not found!");
            }
        }
    }

    /**
     * Start expanding animation, it can be circular reveal or expanding
     */
    public void startProgressEndAnimation(int containerId) throws CircularRevealContainerNotFoundException {
        startProgressEndAnimation(containerId, mAnimationEndListener);
    }

    /**
     * Start expanding animation, it can be circular reveal or expanding
     *
     * @param listener animation end listener
     */
    public void startProgressEndAnimation(int containerId, ButtonAnimationEndListener listener) throws CircularRevealContainerNotFoundException {
        if (mIsCircularRevealEnabled) {
            startCircularReveal(containerId, listener);
        } else {
            startExpandAnimation(listener);
        }
    }

    /**
     * Expanding button animation
     *
     * @param listener listener for end of animation
     */
    private void startExpandAnimation(ButtonAnimationEndListener listener) {
        if (mState != State.PROGRESS) {
            return;
        }

        mState = State.IDLE;
        if (mAnimatedDrawable != null) {
            mAnimatedDrawable.stop();
        }
        setClickable(true);

        animateButtonDimensions(isExpansionAnim(listener), listener);
    }

    private void animateButtonDimensions(boolean isExpansion, BaseAnimationListener listener) {

        if (!isExpansion && listener != null && listener instanceof ButtonAnimationStartListener) {
            ((ButtonAnimationStartListener) listener).onAnimationStarted(getId());
        }
        mIsSizingInProgress = true;
        AnimatorSet animatorSet = getButtonSizingAnimatorSet(isExpansion);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mIsSizingInProgress = false;

                if (isExpansion && listener != null && listener instanceof ButtonAnimationEndListener) {
                    ((ButtonAnimationEndListener) listener).onAnimationEnded(getId());
                }
            }
        });
        animatorSet.start();
    }

    private <T extends BaseAnimationListener> boolean isExpansionAnim(T listener) {
        if (listener != null) {
            return listener.isExpandingAnimation();
        } else {
            return mState == State.IDLE;
        }
    }

    private AnimatorSet getButtonSizingAnimatorSet(boolean isExpansion) {
        AnimatorSet result = new AnimatorSet();
        result.setDuration(mExpansionAnimDuration);
        if (!isExpansion) {
            result.playTogether(getCornerAnimator(), getWidthAnimator(getWidth(), mRequiredSize),
                    getHeightAnimator(getHeight(), mRequiredSize), getTextAlphaAnimation(isExpansion));
        } else {
            result.playTogether(getCornerAnimator(), getWidthAnimator(mRequiredSize, mOriginalWidth),
                    getHeightAnimator(mRequiredSize, mOriginalHeight), getTextAlphaAnimation(isExpansion));
        }
        return result;
    }

    private ValueAnimator getTextAlphaAnimation(boolean isExpansion) {

        final short MAX_ALPHA_VALUE = 255;
        final short MIN_ALPHA_VALUE = 0;

        ValueAnimator animation;
        if (isExpansion) {
            animation = ValueAnimator.ofInt(MIN_ALPHA_VALUE, MAX_ALPHA_VALUE);
        } else {
            animation = ValueAnimator.ofInt(MAX_ALPHA_VALUE, MIN_ALPHA_VALUE);
        }

        animation.addUpdateListener(valueAnimator -> {
            setTextColor((mButtonColorProvider.translateAnimatedValueIntoFadingColor(valueAnimator)));
        });
        return animation;
    }

    private ValueAnimator getWidthAnimator(int initialWidth, int requiredSize) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(initialWidth, requiredSize);
        widthAnimation.addUpdateListener(valueAnimator -> setWidthOfButton((Integer) valueAnimator.getAnimatedValue()));
        return widthAnimation;
    }

    private ValueAnimator getHeightAnimator(int initialHeight, int requiredSize) {
        ValueAnimator heightAnimation = ValueAnimator.ofInt(initialHeight, requiredSize);
        heightAnimation.addUpdateListener(valueAnimator -> setHeightOfButton((Integer) valueAnimator.getAnimatedValue()));
        return heightAnimation;
    }

    private ObjectAnimator getCornerAnimator() {
        int initialCornerRadius = 0;
        int finalCornerRadius = 1000;
        if (mState == State.IDLE) {
            initialCornerRadius = 1000;
            finalCornerRadius = 0;
        }
        return ObjectAnimator.ofFloat(mGradientDrawable,
                "cornerRadius",
                initialCornerRadius,
                finalCornerRadius);
    }

    private void storeOriginalDimensions() {
        if (mOriginalHeight != 0) {
            return;
        }
        mOriginalHeight = getHeight();
        mOriginalWidth = getWidth();
        mRequiredSize = Math.min(mOriginalHeight, mOriginalWidth);
    }


    private void setWidthOfButton(int requiredWidth) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = requiredWidth;
        setLayoutParams(layoutParams);
    }

    private void setHeightOfButton(int requiredHeight) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = requiredHeight;
        setLayoutParams(layoutParams);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mState == State.PROGRESS && !mIsSizingInProgress) {
            drawIndeterminateProgress(canvas);
        }
    }

    private void drawIndeterminateProgress(Canvas canvas) {
        if (mAnimatedDrawable == null || !mAnimatedDrawable.isRunning()) {

            mAnimatedDrawable = new CircularAnimatedDrawable(this,
                    mProgressWidth,
                    mProgressColor);

            int left = mRequiredOffset;
            int right = getWidth() - mRequiredOffset;
            int bottom = getHeight() - mRequiredOffset;
            int top = mRequiredOffset;

            mAnimatedDrawable.setBounds(left, top, right, bottom);
            mAnimatedDrawable.setCallback(this);
            mAnimatedDrawable.start();
        } else {
            mAnimatedDrawable.draw(canvas);
        }
    }

    //TODO create a chain from these settings
    public void setExpansionAnimDuration(int duration) {
        mExpansionAnimDuration = duration;
    }

    public void setProgressColor(int progressColor) {
        mProgressColor = progressColor;
    }

    public void setCircularRevealAnimDuration(int duration) {
        mCircularRevealAnimDuration = duration;
    }

    public void setAnimationStartListener(ButtonAnimationStartListener listener) {
        mAnimationStartListener = listener;
    }

    public void setAnimationEndListener(ButtonAnimationEndListener listener) {
        mAnimationEndListener = listener;
    }

    public void setCircularRevealContainer(CircularRevealContainer container) {
        mCircularRevealContainer = container;
    }

    public void startCircularReveal() throws CircularRevealContainerNotFoundException {
        startCircularReveal(mCircularRevealContainer, null);
    }

    public void startCircularReveal(CircularRevealContainer container) throws CircularRevealContainerNotFoundException {
        startCircularReveal(container, null);
    }

    public void startCircularReveal(ButtonAnimationEndListener animEndListener) throws CircularRevealContainerNotFoundException {
        startCircularReveal(mCircularRevealContainer, animEndListener);
    }

    public void startCircularReveal(int containerId, ButtonAnimationEndListener animEndListener) throws CircularRevealContainerNotFoundException {
        startCircularReveal((CircularRevealContainer) UIHierarchyUtil.getViewByIdInParents(containerId, this), animEndListener);
    }

    public void startCircularReveal(CircularRevealContainer container, ButtonAnimationEndListener animEndListener) throws CircularRevealContainerNotFoundException {
        mState = State.IDLE;
        if (mAnimatedDrawable != null) {
            mAnimatedDrawable.stop();
        }
        try {
            startCircularRevealAnimationOnContainer(container, animEndListener);
        } catch (OSNotSupportedException e) {
            if (isDebugMessagesEnabled) {
                Log.e(TAG, "Version of the current device is lower than Lollipop!");
            }
            if (animEndListener != null) {
                animEndListener.onAnimationEnded(getId());
            }
            invalidate();
        }
    }

    private void startCircularRevealAnimationOnContainer(CircularRevealContainer container, ButtonAnimationEndListener animEndListener) throws CircularRevealContainerNotFoundException, OSNotSupportedException {
        if (container != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                container.startCircularRevealAnimation(new ButtonCRevealAnimationData.ButtonDataBuilder(this, container)
                        .animColor(this)
                        .animDuration(mCircularRevealAnimDuration)
                        .build(), animEndListener);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                container.startCircularRevealAnimation(new ButtonCRevealAnimationData.ButtonDataBuilder(this, container)
                        .animColor(mButtonColorProvider.getButtonOriginalColorId())
                        .animDuration(mCircularRevealAnimDuration)
                        .build(), animEndListener);
            } else {
                throw new OSNotSupportedException();
            }
        } else {
            throw new CircularRevealContainerNotFoundException();
        }
    }
}
