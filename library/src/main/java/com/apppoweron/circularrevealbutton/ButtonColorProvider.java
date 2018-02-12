package com.apppoweron.circularrevealbutton;

import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.text.TextUtils;
import android.widget.Button;

import java.lang.reflect.Field;

/**
 * Collection of button regarded color methods
 */
class ButtonColorProvider {

    private Button button;

    private String originalTextColorHex;

    ButtonColorProvider(Button button) {
        this.button = button;
    }

    private String getButtonTextColorHex() {
        if (TextUtils.isEmpty(originalTextColorHex)) {
            originalTextColorHex = Integer.toHexString(button.getTextColors().getDefaultColor()).substring(2); //Cutting off the first two characters, because it is ARGB
        }
        return originalTextColorHex;
    }

    int translateAnimatedValueIntoFadingColor(ValueAnimator valueAnimator) {
        return Color.parseColor(getFirstAlphaTag((Integer) valueAnimator.getAnimatedValue()) + getButtonTextColorHex());
    }

    private String getFirstAlphaTag(int value) {
        String result = Integer.toHexString(value);
        if (result.length() < 2) {
            result = "0" + result;
        }
        return "#" + result;
    }

    int getButtonOriginalColorId() {
        int result = 0;
        if (button.getBackground() instanceof ColorDrawable) {
            ColorDrawable buttonColor = (ColorDrawable) button.getBackground();
            result = buttonColor.getColor();
        } else if (button.getBackground() instanceof RippleDrawable) {
            RippleDrawable buttonColor = (RippleDrawable) button.getBackground();
            Drawable.ConstantState state = buttonColor.getConstantState();
            try {
                Field colorField = state.getClass().getDeclaredField("mColor");
                colorField.setAccessible(true);
                ColorStateList colorStateList = (ColorStateList) colorField.get(state);
                result = colorStateList.getDefaultColor();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
