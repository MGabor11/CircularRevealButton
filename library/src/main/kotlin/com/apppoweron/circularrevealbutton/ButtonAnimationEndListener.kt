package com.apppoweron.circularrevealbutton

@FunctionalInterface
interface ButtonAnimationEndListener : BaseAnimationListener {

    override val isExpandingAnimation: Boolean
        get() = true

    fun onAnimationEnded(viewId: Int)
}
