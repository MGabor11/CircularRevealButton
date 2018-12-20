package com.apppoweron.circularrevealbutton

import android.animation.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.apppoweron.circularrevealbutton.container.ButtonCRevealAnimationData
import com.apppoweron.circularrevealbutton.container.CircularRevealContainer
import com.apppoweron.circularrevealbutton.container.CircularRevealContainerNotFoundException
import com.apppoweron.circularrevealbutton.util.DebugUtil
import com.apppoweron.circularrevealbutton.util.UIHierarchyUtil

class AnimatedLoadingButton : AppCompatButton, View.OnClickListener {

    companion object {
        private const val TAG = "AnimatedLoadingButton"

        private const val DEFAULT_REQUIRED_OFFSET: Byte = 20
        private const val DEFAULT_PROGRESS_WIDTH: Byte = 8
        private const val DEFAULT_EXPANSION_TIME_IN_MS: Short = 3000
    }

    private var mRequiredOffset: Byte = 0
    private var mProgressWidth: Byte = 0

    private var mOriginalHeight = 0
    private var mOriginalWidth = 0
    private var mRequiredSize = 0

    private var mOnClickListener: View.OnClickListener? = null

    private var mState = State.IDLE
    private var mCircularRevealAnimDuration: Int? = null
    private var mExpansionAnimDuration: Int = 0
    private var mIsSizingInProgress: Boolean = false
    /**
     * It returns the state of circular reveal animation
     * @return the type of end animation, when button ends it's progress, it is a boolean which defines that, circular reveal is enabled
     */
    /**
     * Method, which set the state of
     * @param isCircularRevealEnabled it defines the state of circular reveal option
     */
    var isCircularRevealEnabled: Boolean = false
    private var mProgressColor: Int = 0

    private var mGradientDrawable: GradientDrawable? = null
    private var mAnimatedDrawable: CircularAnimatedDrawable? = null

    private var mAnimationStartListener: ((viewId: Int) -> Unit)? = null
    private var mAnimationEndListener: ((viewId: Int) -> Unit)? = null

    private var mButtonColorProvider: ButtonColorProvider? = null

    private var mCircularRevealContainer: CircularRevealContainer? = null

    private val cornerAnimator: ObjectAnimator
        @SuppressLint("ObjectAnimatorBinding")
        get() {
            var initialCornerRadius = 0f
            var finalCornerRadius = 1000f
            if (mState == State.IDLE) {
                initialCornerRadius = 1000f
                finalCornerRadius = 0f
            }
            return ObjectAnimator.ofFloat(mGradientDrawable,
                    "cornerRadius",
                    initialCornerRadius,
                    finalCornerRadius)
        }

    private enum class State {
        PROGRESS, IDLE
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        //Initializing ButtonColorProvider
        mButtonColorProvider = ButtonColorProvider(this)

        //Required to inline the button's text
        maxLines = 1

        //Creating gradient drawable from shape
        mGradientDrawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getDrawable(context, R.drawable.button_shape_default)!!.constantState!!.newDrawable().mutate() as GradientDrawable
        } else {
            ContextCompat.getDrawable(context, R.drawable.button_shape_default)!!.mutate() as GradientDrawable
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mButtonColorProvider?.buttonOriginalColorId?.let { mGradientDrawable?.setColor(it) }
        }

        val typedArray: TypedArray
        if (attrs != null) {
            typedArray = context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.CircularRevealButton,
                    0, 0)

            mRequiredOffset = typedArray.getInt(R.styleable.CircularRevealButton_requiredOffset, DEFAULT_REQUIRED_OFFSET.toInt()).toByte()
            mProgressWidth = typedArray.getInt(R.styleable.CircularRevealButton_progressWidth, DEFAULT_PROGRESS_WIDTH.toInt()).toByte()
            if (typedArray.getInt(R.styleable.CircularRevealButton_circularRevealAnimDuration, 0) != 0) {
                mCircularRevealAnimDuration = typedArray.getInt(R.styleable.CircularRevealButton_circularRevealAnimDuration, 0)
            }

            mExpansionAnimDuration = if (typedArray.getInt(R.styleable.CircularRevealButton_expansionAnimDuration, 0) != 0) {
                typedArray.getInt(R.styleable.CircularRevealButton_expansionAnimDuration, 0)
            } else {
                DEFAULT_EXPANSION_TIME_IN_MS.toInt()
            }

            mProgressColor = if (typedArray.getColor(R.styleable.CircularRevealButton_progressColor, 0) != 0) {
                typedArray.getColor(R.styleable.CircularRevealButton_progressColor, 0)
            } else {
                mButtonColorProvider!!.buttonTextColor
            }

            isCircularRevealEnabled = typedArray.getBoolean(R.styleable.CircularRevealButton_isAnimEnabled, false)

            if (typedArray.getColor(R.styleable.CircularRevealButton_buttonBackgroundColor, 0) != 0) {
                val bgColor = typedArray.getColor(R.styleable.CircularRevealButton_buttonBackgroundColor, 0)
                mGradientDrawable?.setColor(bgColor)
            }

            typedArray.recycle()
        } else {
            mRequiredOffset = DEFAULT_REQUIRED_OFFSET
            mProgressWidth = DEFAULT_PROGRESS_WIDTH
            mExpansionAnimDuration = DEFAULT_EXPANSION_TIME_IN_MS.toInt()
            mProgressColor = mButtonColorProvider!!.buttonTextColor
        }

        background = mGradientDrawable
        super.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        if (!mIsSizingInProgress && mState == State.IDLE) {
            startProgressAnimation()
            if (mOnClickListener != null) {
                mOnClickListener!!.onClick(view)
            }
        }
    }

    override fun setOnClickListener(listener: View.OnClickListener?) {
        mOnClickListener = listener
    }

    /**
     * Compressing button animation
     *
     * @param listener animation start listener
     */
    fun startProgressAnimation(listener: ((viewId: Int) -> Unit)? = mAnimationStartListener) {
        if (mState != State.IDLE) {
            return
        }

        storeOriginalDimensions()

        mState = State.PROGRESS
        isClickable = false

        animateButtonDimensions(isExpansionAnim(), listener)
    }

    /**
     * Start expanding animation, it can be circular reveal or expanding
     */
    @JvmOverloads
    fun startProgressEndAnimation(listener: ((viewId: Int) -> Unit)? = mAnimationEndListener) {
        try {
            startProgressEndAnimation(0, listener)
        } catch (e: CircularRevealContainerNotFoundException) {
            DebugUtil.debug {
                Log.e(TAG, "Container not found!")
            }
        }

    }

    /**
     * Start expanding animation, it can be circular reveal or expanding
     *
     * @param listener animation end listener
     */
    @Throws(CircularRevealContainerNotFoundException::class)
    @JvmOverloads
    fun startProgressEndAnimation(containerId: Int, listener: ((viewId: Int) -> Unit)? = mAnimationEndListener) {
        if (isCircularRevealEnabled) {
            startCircularReveal(containerId, listener)
        } else {
            startExpandAnimation(listener)
        }
    }

    /**
     * Expanding button animation
     *
     * @param listener listener for end of animation
     */
    private fun startExpandAnimation(listener: ((viewId: Int) -> Unit)?) {
        if (mState != State.PROGRESS) {
            return
        }

        mState = State.IDLE
        if (mAnimatedDrawable != null) {
            mAnimatedDrawable!!.stop()
        }
        isClickable = true

        animateButtonDimensions(isExpansionAnim(), listener)
    }

    private fun animateButtonDimensions(isExpansion: Boolean, listener: ((viewId: Int) -> Unit)?) {

        if (!isExpansion) {
            listener?.invoke(id)
        }
        mIsSizingInProgress = true
        val animatorSet = getButtonSizingAnimatorSet(isExpansion)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mIsSizingInProgress = false

                if (isExpansion) {
                    listener?.invoke(id)
                }
            }
        })
        animatorSet.start()
    }

    private fun isExpansionAnim(): Boolean {
        return mState == State.IDLE
    }

    private fun getButtonSizingAnimatorSet(isExpansion: Boolean): AnimatorSet {
        val result = AnimatorSet()
        result.duration = mExpansionAnimDuration.toLong()
        if (!isExpansion) {
            result.playTogether(cornerAnimator, getWidthAnimator(width, mRequiredSize),
                    getHeightAnimator(height, mRequiredSize), getTextAlphaAnimation(isExpansion))
        } else {
            result.playTogether(cornerAnimator, getWidthAnimator(mRequiredSize, mOriginalWidth),
                    getHeightAnimator(mRequiredSize, mOriginalHeight), getTextAlphaAnimation(isExpansion))
        }
        return result
    }

    private fun getTextAlphaAnimation(isExpansion: Boolean): ValueAnimator {

        val maxAlphaValue = 255
        val minAlphaValue = 0

        val animation: ValueAnimator = if (isExpansion) {
            ValueAnimator.ofInt(minAlphaValue, maxAlphaValue)
        } else {
            ValueAnimator.ofInt(maxAlphaValue, minAlphaValue)
        }

        animation.addUpdateListener { valueAnimator -> setTextColor(mButtonColorProvider!!.translateAnimatedValueIntoFadingColor(valueAnimator)) }
        return animation
    }

    private fun getWidthAnimator(initialWidth: Int, requiredSize: Int): ValueAnimator {
        val widthAnimation = ValueAnimator.ofInt(initialWidth, requiredSize)
        widthAnimation.addUpdateListener { valueAnimator -> setWidthOfButton(valueAnimator.animatedValue as Int) }
        return widthAnimation
    }

    private fun getHeightAnimator(initialHeight: Int, requiredSize: Int): ValueAnimator {
        val heightAnimation = ValueAnimator.ofInt(initialHeight, requiredSize)
        heightAnimation.addUpdateListener { valueAnimator -> setHeightOfButton(valueAnimator.animatedValue as Int) }
        return heightAnimation
    }

    private fun storeOriginalDimensions() {
        if (mOriginalHeight != 0) {
            return
        }
        mOriginalHeight = height
        mOriginalWidth = width
        mRequiredSize = Math.min(mOriginalHeight, mOriginalWidth)
    }


    private fun setWidthOfButton(requiredWidth: Int) {
        val layoutParams = layoutParams
        layoutParams.width = requiredWidth
        setLayoutParams(layoutParams)
    }

    private fun setHeightOfButton(requiredHeight: Int) {
        val layoutParams = layoutParams
        layoutParams.height = requiredHeight
        setLayoutParams(layoutParams)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mState == State.PROGRESS && !mIsSizingInProgress) {
            drawIndeterminateProgress(canvas)
        }
    }

    private fun drawIndeterminateProgress(canvas: Canvas) {
        if (mAnimatedDrawable == null || !mAnimatedDrawable!!.isRunning) {

            mAnimatedDrawable = CircularAnimatedDrawable(this,
                    mProgressWidth.toFloat(),
                    mProgressColor)

            val left = mRequiredOffset.toInt()
            val right = width - mRequiredOffset
            val bottom = height - mRequiredOffset
            val top = mRequiredOffset.toInt()

            mAnimatedDrawable!!.setBounds(left, top, right, bottom)
            mAnimatedDrawable!!.callback = this
            mAnimatedDrawable!!.start()
        } else {
            mAnimatedDrawable!!.draw(canvas)
        }
    }

    //TODO create a chain from these settings
    fun setExpansionAnimDuration(duration: Int) {
        mExpansionAnimDuration = duration
    }

    fun setProgressColor(progressColor: Int) {
        mProgressColor = progressColor
    }

    fun setCircularRevealAnimDuration(duration: Int) {
        mCircularRevealAnimDuration = duration
    }

    fun setAnimationStartListener(listener: ((viewId: Int) -> Unit)?) {
        mAnimationStartListener = listener
    }

    fun setAnimationEndListener(listener: ((viewId: Int) -> Unit)?) {
        mAnimationEndListener = listener
    }

    fun setCircularRevealContainer(container: CircularRevealContainer) {
        mCircularRevealContainer = container
    }

    @Throws(CircularRevealContainerNotFoundException::class)
    fun startCircularReveal(containerId: Int, listener: ((viewId: Int) -> Unit)?) {
        startCircularReveal(UIHierarchyUtil.getViewByIdInParents(containerId, this) as CircularRevealContainer, listener)
    }

    @Throws(CircularRevealContainerNotFoundException::class)
    @JvmOverloads
    fun startCircularReveal(container: CircularRevealContainer? = mCircularRevealContainer, listener: ((viewId: Int) -> Unit)?) {
        mState = State.IDLE
        if (mAnimatedDrawable != null) {
            mAnimatedDrawable!!.stop()
        }
        try {
            startCircularRevealAnimationOnContainer(container, listener)
        } catch (e: OSNotSupportedException) {
            DebugUtil.debug {
                Log.e(TAG, "Version of the current device is lower than Lollipop!")
            }
            listener?.invoke(id)
            invalidate()
        }

    }

    @Throws(CircularRevealContainerNotFoundException::class, OSNotSupportedException::class)
    private fun startCircularRevealAnimationOnContainer(container: CircularRevealContainer?, listener: ((viewId: Int) -> Unit)?) {
        if (container != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                container.startCircularRevealAnimation(ButtonCRevealAnimationData.create(this, container) {
                    animColor { mButtonColorProvider?.buttonOriginalColorId }
                    animDuration { mCircularRevealAnimDuration }
                }, listener)
            } else {
                throw OSNotSupportedException()
            }

        } else {
            throw CircularRevealContainerNotFoundException()
        }
    }



}
