package com.apppoweron.circularrevealbutton

internal class OSNotSupportedException : Exception() {

    override val message: String?
        get() = "This feature requires higher Android version!"

}
