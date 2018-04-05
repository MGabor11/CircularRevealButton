package com.apppoweron.circularrevealbutton.container

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.Button

/**
 * This class responsible for get required data to create the circular reveal animation
 */
class ButtonCRevealAnimationData private constructor(builder: ButtonDataBuilder) {

    companion object {
        private val TAG = "ButtonCRevealAnimationData"
    }

    val buttonId: Int
    val x: Int
    val y: Int
    val animColor: Int?
    val animDuration: Int?
    val animStartRadius: Int

    init {
        this.buttonId = builder.buttonId
        this.x = builder.x
        this.y = builder.y
        this.animColor = builder.animColor
        this.animStartRadius = builder.buttonRadius
        this.animDuration = builder.animDuration
    }

    class ButtonDataBuilder(button: Button, container: CircularRevealContainer) {
        internal val buttonId: Int
        internal val x: Int
        internal var y: Int = 0
        internal var animColor: Int? = null
        internal var animDuration: Int? = null
        internal val buttonRadius: Int


        init {
            this.buttonRadius = button.height / 2
            this.buttonId = button.id

            this.x = button.left + button.width / 2
            this.y = button.top + this.buttonRadius

            val buttonLocation = intArrayOf(0, 0)
            val containerLocation = intArrayOf(0, 0)
            button.getLocationInWindow(buttonLocation)
            container.getLocationInWindow(containerLocation)

            this.y = buttonLocation[1] + this.buttonRadius - containerLocation[1]
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        fun animColor(button: Button): ButtonDataBuilder {
            val drawable = button.background as GradientDrawable
            animColor(drawable.color!!.defaultColor)
            return this
        }

        fun animColor(color: Int): ButtonDataBuilder {
            this.animColor = color
            return this
        }

        fun animDuration(animDuration: Int?): ButtonDataBuilder {
            this.animDuration = animDuration
            return this
        }

        fun build(): ButtonCRevealAnimationData {
            return ButtonCRevealAnimationData(this)
        }
    }




}
