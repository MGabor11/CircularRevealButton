package com.apppoweron.circularrevealbutton.container;

public class CircularRevealContainerNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "Button needs a container to inflate the circular reveal effect!";
    }

}
