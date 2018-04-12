package com.apppoweron.circularrevealbuttondemo

class NoContainerException : Exception() {

    override val message: String?
        get() = "There is no container for the Fragment!"

}
