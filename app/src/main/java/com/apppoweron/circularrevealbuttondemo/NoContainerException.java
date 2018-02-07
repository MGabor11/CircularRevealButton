package com.apppoweron.circularrevealbuttondemo;

public class NoContainerException extends Exception{

    @Override
    public String getMessage() {
        return "There is no container for the Fragment!";
    }

}
