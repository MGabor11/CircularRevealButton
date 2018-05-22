package com.apppoweron.circularrevealbuttondemo.common

class NoContainerException : Exception() {

    override val message: String?
        get() = "There is no container for the Fragment!"

}
