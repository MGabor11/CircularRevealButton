package com.apppoweron.circularrevealbutton.container;

class OSNotSupportedException extends Exception {

    @Override
    public String getMessage() {
        return "This feature requires higher Android version!";
    }
}
