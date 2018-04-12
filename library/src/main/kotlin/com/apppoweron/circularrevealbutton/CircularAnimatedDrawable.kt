package com.apppoweron.circularrevealbutton

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator

/**
 * @param view        View to be animated
 * @param borderWidth The width of the spinning bar
 * @param arcColor    The color of the spinning bar
 */
class CircularAnimatedDrawable constructor(private val mAnimatedView: View, private val mBorderWidth: Float, arcColor: Int) : Drawable(), Animatable {

    companion object {
        private val ANGLE_INTERPOLATOR = LinearInterpolator()
        private val SWEEP_INTERPOLATOR = DecelerateInterpolator()
        private const val ANGLE_ANIMATOR_DURATION = 2000
        private const val SWEEP_ANIMATOR_DURATION = 900
        private const val MIN_SWEEP_ANGLE = 30f
    }

    private val mValueAnimatorAngle: ValueAnimator = ValueAnimator.ofFloat(0f, 360f)
    private val mValueAnimatorSweep: ValueAnimator = ValueAnimator.ofFloat(0f, 360f - 2 * MIN_SWEEP_ANGLE)

    private val fBounds = RectF()
    private val mPaint: Paint
    private var mCurrentGlobalAngle: Float = 0.toFloat()
    private var mCurrentSweepAngle: Float = 0.toFloat()
    private val mCurrentGlobalAngleOffset: Float = 0.toFloat()

    private var mModeAppearing: Boolean = false
    private var mRunning: Boolean = false


    init {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mBorderWidth
        mPaint.color = arcColor

        setupAnimations()
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        fBounds.left = bounds.left.toFloat() + mBorderWidth / 2f + .5f
        fBounds.right = bounds.right.toFloat() - mBorderWidth / 2f - .5f
        fBounds.top = bounds.top.toFloat() + mBorderWidth / 2f + .5f
        fBounds.bottom = bounds.bottom.toFloat() - mBorderWidth / 2f - .5f
    }

    override fun start() {
        if (mRunning) {
            return
        }

        mRunning = true
        mValueAnimatorAngle.start()
        mValueAnimatorSweep.start()
    }


    override fun stop() {
        if (!mRunning) {
            return
        }

        mRunning = false
        mValueAnimatorAngle.cancel()
        mValueAnimatorSweep.cancel()
    }

    override fun isRunning(): Boolean {
        return mRunning
    }

    /**
     * Method called when the drawable is going to draw itself.
     *
     * @param canvas
     */
    override fun draw(canvas: Canvas) {
        var startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset
        var sweepAngle = mCurrentSweepAngle
        if (!mModeAppearing) {
            startAngle += sweepAngle
            sweepAngle = 360f - sweepAngle - MIN_SWEEP_ANGLE
        } else {
            sweepAngle += MIN_SWEEP_ANGLE
        }

        canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint)
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    private fun setupAnimations() {

        mValueAnimatorAngle.interpolator = ANGLE_INTERPOLATOR
        mValueAnimatorAngle.duration = ANGLE_ANIMATOR_DURATION.toLong()
        mValueAnimatorAngle.repeatCount = ValueAnimator.INFINITE
        mValueAnimatorAngle.addUpdateListener { animation ->
            mCurrentGlobalAngle = animation.animatedValue as Float
            mAnimatedView.invalidate()
        }

        mValueAnimatorSweep.interpolator = SWEEP_INTERPOLATOR
        mValueAnimatorSweep.duration = SWEEP_ANIMATOR_DURATION.toLong()
        mValueAnimatorSweep.repeatCount = ValueAnimator.INFINITE
        mValueAnimatorSweep.addUpdateListener { animation ->
            mCurrentSweepAngle = animation.animatedValue as Float
            invalidateSelf()
        }

        mValueAnimatorSweep.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationRepeat(animation: Animator) {
                toggleAppearingMode()
            }
        })

    }

    private fun toggleAppearingMode() {
        mModeAppearing = !mModeAppearing
    }


}