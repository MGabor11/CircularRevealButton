package com.apppoweron.circularrevealbutton.container;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Button;

/**
 * This class responsible for get required data to create the circular reveal animation
 */
public class ButtonCRevealAnimationData {

    private static final String TAG = "ButtonCRevealAnimationData";

    private int buttonId;
    private int x;
    private int y;
    private Integer animColor;
    private Integer animDuration;
    private int buttonRadius;


    private ButtonCRevealAnimationData(ButtonDataBuilder builder) {
        this.buttonId = builder.buttonId;
        this.x = builder.x;
        this.y = builder.y;
        this.animColor = builder.animColor;
        this.buttonRadius = builder.buttonRadius;
        this.animDuration = builder.animDuration;
    }

    public int getButtonId() {
        return this.buttonId;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Integer getAnimColor() {
        return this.animColor;
    }

    public int getAnimStartRadius() {
        return this.buttonRadius;
    }

    public Integer getAnimDuration() {
        return this.animDuration;
    }

    public static class ButtonDataBuilder {
        private int buttonId;
        private int x;
        private int y;
        private Integer animColor;
        private Integer animDuration;
        private int buttonRadius;


        public ButtonDataBuilder(Button button, CircularRevealContainer container) {
            this.buttonRadius = button.getHeight() / 2;
            this.buttonId = button.getId();

            this.x = button.getLeft() + button.getWidth() / 2;
            this.y = button.getTop() + this.buttonRadius;

            int[] buttonLocation = {0, 0};
            int[] containerLocation = {0, 0};
            button.getLocationInWindow(buttonLocation);
            container.getLocationInWindow(containerLocation);

            this.y = buttonLocation[1] + this.buttonRadius - containerLocation[1];
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        public ButtonDataBuilder animColor(Button button) {
            GradientDrawable drawable = (GradientDrawable) button.getBackground();
            animColor(drawable.getColor().getDefaultColor());
            return this;
        }

        public ButtonDataBuilder animColor(int color) {
            this.animColor = color;
            return this;
        }

        public ButtonDataBuilder animDuration(Integer animDuration) {
            this.animDuration = animDuration;
            return this;
        }

        public ButtonCRevealAnimationData build() {
            return new ButtonCRevealAnimationData(this);
        }
    }


}
