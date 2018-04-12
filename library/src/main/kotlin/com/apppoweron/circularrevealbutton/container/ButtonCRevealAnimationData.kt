package com.apppoweron.circularrevealbutton.container

import android.widget.Button

/**
 * This class responsible for get required data to create the circular reveal animation
 */
class ButtonCRevealAnimationData private constructor(val buttonId: Int,
                                                     val x: Int,
                                                     val y: Int,
                                                     val animColor: Int?,
                                                     val animDuration: Int?,
                                                     val animStartRadius: Int) {


    private constructor(builder: Builder) : this(builder.buttonId, builder.x, builder.y, builder.animColor, builder.animDuration, builder.buttonRadius)

    companion object {
        private val TAG = "ButtonCRevealAnimationData"
        fun create(button: Button, container: CircularRevealContainer, init: Builder.() -> Unit) = Builder(button, container, init).build()
    }

    class Builder private constructor(button: Button, container: CircularRevealContainer) {
        val buttonId: Int
        val x: Int
        val y: Int
        val buttonRadius: Int

        var animColor: Int? = null
        var animDuration: Int? = null


        init {
            this.buttonRadius = button.height / 2
            this.buttonId = button.id

            this.x = button.left + button.width / 2

            val buttonLocation = intArrayOf(0, 0)
            val containerLocation = intArrayOf(0, 0)
            button.getLocationInWindow(buttonLocation)
            container.getLocationInWindow(containerLocation)

            this.y = buttonLocation[1] + buttonRadius - containerLocation[1]
        }

        constructor(button: Button, container: CircularRevealContainer, init: Builder.() -> Unit) : this(button, container) {
            init()
        }

        fun animColor(init: Builder.() -> Int?) = apply { animColor = init() }

        fun animDuration(init: Builder.() -> Int?) = apply { animDuration = init() }

        fun build() = ButtonCRevealAnimationData(this)
    }

}
