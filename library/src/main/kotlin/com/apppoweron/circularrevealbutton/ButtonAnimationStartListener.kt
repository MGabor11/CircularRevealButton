package com.apppoweron.circularrevealbutton

@FunctionalInterface
interface ButtonAnimationStartListener : BaseAnimationListener {

    override val isExpandingAnimation: Boolean
        get() = false

    fun onAnimationStarted(viewId: Int)
}
