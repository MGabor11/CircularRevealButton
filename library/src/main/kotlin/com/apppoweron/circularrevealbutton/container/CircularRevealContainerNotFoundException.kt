package com.apppoweron.circularrevealbutton.container

class CircularRevealContainerNotFoundException : Exception() {

    override val message: String?
        get() = "Button needs a container to inflate the circular reveal effect!"

}
